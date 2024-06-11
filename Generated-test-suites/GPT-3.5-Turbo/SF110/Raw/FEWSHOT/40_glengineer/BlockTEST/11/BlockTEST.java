import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.blocks.*;

public class BlockTest {
    @Test
    public void testConstructor() {
        Scheme scheme = new Scheme(10, 10);
        
        Block block1 = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals(0, block1.x1);
        assertEquals(0, block1.y1);
        assertEquals(5, block1.x2);
        assertEquals(5, block1.y2);
        assertSame(scheme, block1.scheme);
        
        Block block2 = new ConcreteBlock(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        assertEquals(0, block2.x1);
        assertEquals(0, block2.y1);
        assertEquals(5, block2.x2);
        assertEquals(5, block2.y2);
        assertSame(scheme, block2.scheme);
        
        Block block3 = new ConcreteBlock(scheme);
        assertEquals(0, block3.x1);
        assertEquals(0, block3.y1);
        assertEquals(9, block3.x2);
        assertEquals(9, block3.y2);
        assertSame(scheme, block3.scheme);
        
        boolean exceptionThrown = false;
        try {
            Block block4 = new ConcreteBlock(scheme, -1, 0, 5, 5);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            Block block5 = new ConcreteBlock(scheme, 0, -1, 5, 5);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            Block block6 = new ConcreteBlock(scheme, 5, 5, 0, 0);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            Block block7 = new ConcreteBlock(scheme, 0, 0, 5, 11);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            Block block8 = new ConcreteBlock(scheme, 0, 0, 11, 5);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 9, 9);
        
        Block subblock = block.subblock(2, 2, 5, 5);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(5, subblock.x2);
        assertEquals(5, subblock.y2);
        assertSame(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcdefghij";
        scheme.lines[1] = "klmnopqrst";
        scheme.lines[2] = "uvwxyzabcd";
        
        Block block = new ConcreteBlock(scheme, 2, 1, 7, 3);
        
        assertEquals('a', block.charAt(2, 1));
        assertEquals('r', block.charAt(5, 2));
        assertEquals('d', block.charAt(2, 3));
        
        boolean exceptionThrown = false;
        try {
            block.charAt(9, 2);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            block.charAt(6, 4);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcdefghij";
        scheme.lines[1] = "klmnopqrst";
        scheme.lines[2] = "uvwxyzabcd";
        
        Block block = new ConcreteBlock(scheme, 2, 1, 7, 3);
        
        assertEquals("cde", block.textAt(2, 5, 1));
        assertEquals("mnop", block.textAt(3, 7, 2));
        assertEquals("uvw", block.textAt(0, 3, 3));
        
        boolean exceptionThrown = false;
        try {
            block.textAt(9, 12, 2);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            block.textAt(4, 6, 4);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        
        Block block1 = new ConcreteBlock(scheme, 1, 0, 4, 1);
        assertTrue(block1.containsOnlyOneWord());
        
        Block block2 = new ConcreteBlock(scheme, 0, 0, 4, 1);
        
        boolean exceptionThrown = false;
        try {
            block2.containsOnlyOneWord();
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        scheme.lines[2] = "";
        Block block3 = new ConcreteBlock(scheme, 1, 0, 4, 2);
        assertTrue(block3.containsOnlyOneWord());
        
        Block block4 = new ConcreteBlock(scheme, 0, 0, 4, 2);
        
        exceptionThrown = false;
        try {
            block4.containsOnlyOneWord();
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcdefghij";
        scheme.lines[1] = "klmnopqrst";
        scheme.lines[2] = "uvwxyzabcd";
        
        Block block = new ConcreteBlock(scheme, 1, 0, 4, 2);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.x1);
        assertEquals(0, subblock1.y1);
        assertEquals(4, subblock1.x2);
        assertEquals(1, subblock1.y2);
        assertSame(scheme, subblock1.scheme);
        
        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.x1);
        assertEquals(1, subblock2.y1);
        assertEquals(4, subblock2.x2);
        assertEquals(2, subblock2.y2);
        assertSame(scheme, subblock2.scheme);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcdefghij";
        scheme.lines[1] = "klmnopqrst";
        scheme.lines[2] = "uvwxyzabcd";
        
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(4, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(0, subblock1.x1);
        assertEquals(0, subblock1.y1);
        assertEquals(1, subblock1.x2);
        assertEquals(1, subblock1.y2);
        assertSame(scheme, subblock1.scheme);
        
        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.x1);
        assertEquals(0, subblock2.y1);
        assertEquals(2, subblock2.x2);
        assertEquals(1, subblock2.y2);
        assertSame(scheme, subblock2.scheme);
        
        Block subblock3 = subblocks.get(2);
        assertEquals(0, subblock3.x1);
        assertEquals(1, subblock3.y1);
        assertEquals(1, subblock3.x2);
        assertEquals(2, subblock3.y2);
        assertSame(scheme, subblock3.scheme);
        
        Block subblock4 = subblocks.get(3);
        assertEquals(1, subblock4.x1);
        assertEquals(1, subblock4.y1);
        assertEquals(2, subblock4.x2);
        assertEquals(2, subblock4.y2);
        assertSame(scheme, subblock4.scheme);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcdefghij";
        scheme.lines[1] = "klmnopqrst";
        scheme.lines[2] = "uvwxyzabcd";
        
        Block block = new ConcreteBlock(scheme, 3, 2, 6, 4);
        
        String expected = "Block(3,2, 6,4):\n" +
                          " ###### \n" +
                          " #qrs# \n" +
                          " #vwx# \n" +
                          " ###### ";
        
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "abcdefghij";
        scheme.lines[1] = "klmnopqrst";
        scheme.lines[2] = "uvwxyzabcd";
        
        Block block = new ConcreteBlock(scheme, 2, 1, 7, 3);
        
        assertEquals("efgh", block.getUpperLine());
    }
}