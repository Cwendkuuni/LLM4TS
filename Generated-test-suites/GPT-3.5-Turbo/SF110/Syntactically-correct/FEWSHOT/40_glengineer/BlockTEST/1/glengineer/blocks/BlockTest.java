import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BlockTest {
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
        
        subblock = block.subblock(5, 5, 1, 1);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(5, subblock.getX2());
        assertEquals(5, subblock.getY2());
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[5] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 0, 0, 10, 10);
        
        assertEquals('a', block.charAt(0, 5));
        assertEquals('j', block.charAt(9, 5));
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[5] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 0, 0, 10, 10);
        
        assertEquals("abcdefghij", block.textAt(0, 10, 5));
        assertEquals("def", block.textAt(3, 6, 5));
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "aaaabbbb";
        scheme.lines[1] = "aaaabbbb";
        scheme.lines[2] = "aaaabbbb";
        scheme.lines[3] = "aaaabbbb";
        Block block = new ConcreteBlock(scheme, 0, 0, 8, 4);
        
        assertEquals(true, block.containsOnlyOneWord());
        
        scheme.lines[0] = "aaaabbbb";
        scheme.lines[1] = "aaaabbbb";
        scheme.lines[2] = "aaaabbbb";
        scheme.lines[3] = "aaaacccc";
        block = new ConcreteBlock(scheme, 0, 0, 8, 4);
        
        assertEquals(false, block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "aaaabbbb";
        scheme.lines[1] = "aaaabbbb";
        Block block = new ConcreteBlock(scheme, 0, 0, 8, 1);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(0, subblock1.getX1());
        assertEquals(0, subblock1.getY1());
        assertEquals(3, subblock1.getX2());
        assertEquals(1, subblock1.getY2());
        
        Block subblock2 = subblocks.get(1);
        assertEquals(4, subblock2.getX1());
        assertEquals(0, subblock2.getY1());
        assertEquals(7, subblock2.getX2());
        assertEquals(1, subblock2.getY2());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "aaaabbbb";
        scheme.lines[1] = "aaaacccc";
        Block block = new ConcreteBlock(scheme, 0, 0, 8, 1);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(0, subblock1.getX1());
        assertEquals(0, subblock1.getY1());
        assertEquals(8, subblock1.getX2());
        assertEquals(1, subblock1.getY2());
        
        Block subblock2 = subblocks.get(1);
        assertEquals(0, subblock2.getX1());
        assertEquals(1, subblock2.getY1());
        assertEquals(8, subblock2.getX2());
        assertEquals(1, subblock2.getY2());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "aaaabbbb";
        scheme.lines[1] = "aaaacccc";
        Block block = new ConcreteBlock(scheme, 0, 0, 8, 1);
        
        String expected = "Block(0,0, 8,1):\n" 
                        + " ####################\n"
                        + " #    aaaabbbb      #\n"
                        + " #    aaaacccc      #\n"
                        + " ####################";
                        
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "aaaabbbb";
        scheme.lines[1] = "aaaacccc";
        Block block = new ConcreteBlock(scheme, 0, 0, 8, 1);
        
        assertEquals("aaaabbbb", block.getUpperLine());
    }
}