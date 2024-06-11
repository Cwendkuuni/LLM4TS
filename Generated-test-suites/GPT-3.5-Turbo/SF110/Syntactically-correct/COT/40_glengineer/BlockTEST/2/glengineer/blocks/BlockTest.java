
package glengineer.blocks;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        
        Block subblock = block.subblock(1, 1, 3, 3);
        
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(3, subblock.getX2());
        assertEquals(3, subblock.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubblock_OutOfBounds() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        
        Block subblock = block.subblock(1, 1, 5, 5);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def", "ghi"});
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));
        assertEquals('d', block.charAt(0, 1));
        assertEquals('e', block.charAt(1, 1));
        assertEquals('f', block.charAt(2, 1));
        assertEquals('g', block.charAt(0, 2));
        assertEquals('h', block.charAt(1, 2));
        assertEquals('i', block.charAt(2, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_OutOfBlock() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def"});
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        
        char c = block.charAt(3, 1);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def", "ghi"});
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("bc", block.textAt(1, 3, 0));
        assertEquals("de", block.textAt(0, 2, 1));
        assertEquals("ef", block.textAt(1, 3, 1));
        assertEquals("gh", block.textAt(0, 2, 2));
        assertEquals("hi", block.textAt(1, 3, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_ImproperSublineCoordinates() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def", "ghi"});
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        
        String text = block.textAt(0, 4, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 0, 0);
        
        boolean result = block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def"});
        Block block = new ConcreteBlock(scheme, 0, 2, 0, 2);
        
        boolean result = block.containsOnlyOneWord();
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def", "ghi"});
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        
        String expected = "Block(0,0, 3,3):\n" +
                          " ####\n" +
                          " # abc #\n" +
                          " # def #\n" +
                          " # ghi #\n" +
                          " ####";
                          
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.setLines(new String[] {"abc", "def", "ghi"});
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        
        assertEquals("abc", block.getUpperLine());
    }
}

class ConcreteBlock extends Block {
    public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
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

class Scheme extends Block {
    public Scheme() {
        super(null);
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

class CharTable {
    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;
    public int getX1() {
        return x1;
    }
    public int getY1() {
        return y1;
    }
    public int getX2() {
        return x2;
    }
    public int getY2() {
        return y2;
    }
}
