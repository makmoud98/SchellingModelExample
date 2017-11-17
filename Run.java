import java.util.*;
import print.Printer;
import print.Printer.Types;
import print.color.*;
import print.color.Ansi.*;
import print.exception.InvalidArgumentsException;

public class Run
{
	public static Cell[][] cells;
	public static final int SIZE = 50;
	public static final double SIMILAR = .8;
	public static void main(String[] args) throws InvalidArgumentsException
	{
		cells = new Cell[SIZE][SIZE];
		addCells();
		emptyCells();
		printcells();
		try{Thread.sleep(3000);}
		catch(Exception e){}
		boolean someonenothappy = false;
		int counter = 0;
		while(true)
		{
			counter++;
			someonenothappy = false;
			for(int i = 0; i < cells.length; i++)
			{
				for(int j = 0; j < cells[0].length; j++)
				{
					Cell cell = cells[i][j];
					if(cell.state != 0)
					{
						if(!isCellHappy(cell))
						{
							someonenothappy = true;
							moveCellSomewhereElse(cell);
						}
					}
				}
			}
			if(!someonenothappy)
			{
				printcells();
				System.out.println("all cells are happy!!   " +counter+" rounds");
				return;
			}
		}
	}
	public static void printcells()
	{
		ColoredPrinterWIN cp = new ColoredPrinterWIN.Builder(1, false).build();
		for(int i = 0; i < cells.length; i++)
		{
			for(int j = 0; j < cells[0].length; j++)
			{
				Cell cell = cells[i][j];
				if(cell.state == 0)
				{
					cp.print("  ", Attribute.NONE, FColor.BLACK, BColor.WHITE);
				}
				else if(cell.state == 1)
				{
					cp.print("X ", Attribute.NONE, FColor.BLUE, BColor.WHITE);
				}
				else
				{
					cp.print("O ", Attribute.NONE, FColor.RED, BColor.WHITE);
				}
			}
			System.out.println("");
		}
		cp.clear();
	}
	public static void addCells()
	{
		for(int i = 0; i < cells.length; i++)
		{
			for(int j = 0; j < cells[0].length; j++)
			{
				cells[i][j] = new Cell(new Random().nextInt(2)+1, new Pos(i,j));
			}
		}
	}
	public static void emptyCells()
	{
		int counter = 0;
		while(counter < 90)
		{
			Random r = new Random();
			Cell cell = cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)];
			if(cell.state != 0)
			{
				counter++;
				cell.state = 0;
			}
		}
	}
	public static boolean isCellHappy(Cell cell)
	{
		ArrayList<Cell> neighbors = getCellNeighbors(cell);
		int state = cell.state;
		int total = neighbors.size();
		int same = 0;
		for(int i = 0; i < total; i++)
		{
			if(neighbors.get(i).state == state)
				same++;
		}
		double percentsame = (((double)same)/((double)total));
		return percentsame >= SIMILAR;
	}
	public static ArrayList<Cell> getCellNeighbors(Cell cell)
	{
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		int x = cell.pos.x;
		int y = cell.pos.y;
		for(int i = 0; i < cells.length; i++)
		{
			for(int j = 0; j < cells[0].length; j++)
			{
				Cell cell_ = cells[i][j];
				int x_ = cell_.pos.x;
				int y_ = cell_.pos.y;
				int deltax = Math.abs(x_-x);
				int deltay = Math.abs(y_-y);
				if(cell_.state != 0 && ((deltax == 0 && deltay == 1) || (deltax == 1 && deltay == 0) || (deltax == 1 && deltay == 1)))
				{
					neighbors.add(cell_);
				}
			}
		}
		return neighbors;
	}
	public static void moveCellSomewhereElse(Cell cell)
	{
		ArrayList<Cell> empty = new ArrayList<Cell>();
		for(int i = 0; i < cells.length; i++)
		{
			for(int j = 0; j < cells[0].length; j++)
			{
				Cell cell_ = cells[i][j];
				if(cell_.state == 0)
				{
					empty.add(cell_);
				}
			}
		}
		Cell target = empty.get(new Random().nextInt(empty.size()));
		target.state = cell.state;
		cell.state = 0;
		System.out.println("cell was moved from ("+cell.pos.x+","+cell.pos.y+") to ("+target.pos.x+","+target.pos.y+")");
	}
}