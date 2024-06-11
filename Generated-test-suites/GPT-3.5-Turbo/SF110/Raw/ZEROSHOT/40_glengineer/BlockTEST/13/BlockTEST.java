package glengineer.blocks;

import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(subblock.getX1(), 2);
        assertEquals(subblock.getY1(), 2);
        assertEquals(subblock.getX2(), 4);
        assertEquals(subblock.getY2(), 4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubblock_invalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        block.subblock(5, 5, 2, 2);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        assertEquals(block.charAt(2, 2), ' ');
        assertEquals(block.charAt(5, 2), 'D');
        assertEquals(block.charAt(10, 10), '#');
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_invalidPosition() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        block.charAt(5, 5);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        assertEquals(block.textAt(2, 4, 2), "EF");
        assertEquals(block.textAt(6, 9, 1), "C  ");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_invalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        block.textAt(5, 2, 2);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_trivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 1, 1);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(subblocks.size(), 3);
        assertEquals(subblocks.get(0).getX1(), 1);
        assertEquals(subblocks.get(0).getY1(), 1);
        assertEquals(subblocks.get(0).getX2(), 3);
        assertEquals(subblocks.get(0).getY2(), 2);
        assertEquals(subblocks.get(1).getX1(), 1);
        assertEquals(subblocks.get(1).getY1(), 3);
        assertEquals(subblocks.get(1).getX2(), 3);
        assertEquals(subblocks.get(1).getY2(), 4);
        assertEquals(subblocks.get(2).getX1(), 1);
        assertEquals(subblocks.get(2).getY1(), 4);
        assertEquals(subblocks.get(2).getX2(), 2);
        assertEquals(subblocks.get(2).getY2(), 6);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(subblocks.size(), 3);
        assertEquals(subblocks.get(0).getX1(), 1);
        assertEquals(subblocks.get(0).getY1(), 1);
        assertEquals(subblocks.get(0).getX2(), 2);
        assertEquals(subblocks.get(0).getY2(), 3);
        assertEquals(subblocks.get(1).getX1(), 2);
        assertEquals(subblocks.get(1).getY1(), 1);
        assertEquals(subblocks.get(1).getX2(), 4);
        assertEquals(subblocks.get(1).getY2(), 3);
        assertEquals(subblocks.get(2).getX1(), 5);
        assertEquals(subblocks.get(2).getY1(), 1);
        assertEquals(subblocks.get(2).getX2(), 8);
        assertEquals(subblocks.get(2).getY2(), 3);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        String expected = "Block(1,1, 8,3):\n" +
                          " ############\n" +
                          " #   ABC    #\n" +
                          " #   DEF    #\n" +
                          " ############";
        assertEquals(block.toString(), expected);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[] {
            "##########",
            "#   ABC  #",
            "#   DEF  #",
            "##########"
        };
        Block block = new BlockImpl(scheme, 1, 1, 8, 3);
        assertEquals(block.getUpperLine(), "   ABC  ");
    }
}

class BlockImpl extends Block {
    
    public BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    public BlockImpl(Scheme scheme) {
        super(scheme);
    }
    
    public Block subblock(int p0, int p1, int p2, int p3) {
        return null;
    }
    
    public List<Block> extractSequentialSubblocks() {
        return null;
    }
    
    public List<Block> extractParallelSubblocks() {
        return null;
    }
}

class Scheme {
    int x2;
    int y2;
    String[] lines;
    
    public Scheme(int x2, int y2) {
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public char charAt(int x, int y) {
        return ' ';
    }
    
    public String textAt(int x1, int x2, int y) {
        return "";
    }
}