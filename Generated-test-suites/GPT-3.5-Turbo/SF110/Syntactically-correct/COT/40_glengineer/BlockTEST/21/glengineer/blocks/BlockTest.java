import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {
    @Test
    public void testConstructorSchemeIntIntIntInt() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorSchemeCharPositionCharPosition() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new BlockImpl(scheme, p1, p2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorScheme() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Block block = new BlockImpl(new Scheme(5, 5), 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 4, 4);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "12345";
        Block block = new BlockImpl(scheme);
        
        assertEquals('1', block.charAt(0, 0));
        assertEquals('2', block.charAt(1, 0));
        assertEquals('3', block.charAt(2, 0));
        assertEquals('4', block.charAt(3, 0));
        assertEquals('5', block.charAt(4, 0));
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "12345";
        Block block = new BlockImpl(scheme);
        
        assertEquals("123", block.textAt(0, 3, 0));
        assertEquals("345", block.textAt(2, 5, 0));
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Block block = new BlockImpl(new Scheme(5, 5));
        
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
        
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[4] = "word";
        block = new BlockImpl(scheme);
        
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }
        
        scheme.lines[0] = "word";
        block = new BlockImpl(scheme);
        
        assertTrue(block.containsOnlyOneWord());
        
        scheme.lines[1] = "not";
        block = new BlockImpl(scheme);
        
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new BlockImpl(new Scheme(5, 5));
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertTrue(subblocks.isEmpty());
        
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "line1";
        scheme.lines[2] = "line2";
        block = new BlockImpl(scheme);
        
        subblocks = block.extractSequentialSubblocks();
        
        assertEquals(2, subblocks.size());
        assertEquals("line1", subblocks.get(0).scheme.lines[0]);
        assertEquals("line2", subblocks.get(1).scheme.lines[0]);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Block block = new BlockImpl(new Scheme(5, 5));
        List<Block> subblocks = bloc.extractParallelSubblocks();
        
        assertTrue(subblocks.isEmpty());
        
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "word1";
        scheme.lines[3] = "word2";
        block = new BlockImpl(scheme);
        
        subblocks = block.extractParallelSubblocks();
        
        assertEquals(2, subblocks.size());
        assertEquals("word1", subblocks.get(0).scheme.lines[0]);
        assertEquals("word2", subblocks.get(1).scheme.lines[0]);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "line1";
        scheme.lines[2] = "line2";
        Block block = new BlockImpl(scheme);
        
        String expected = "Block(0,0, 5,5):\n" +
                          " ###############\n" +
                          " # line1 #\n" +
                          " # line2 #\n" +
                          " ###############";
                          
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "word";
        Block block = new BlockImpl(scheme);
        
        assertEquals("word", block.getUpperLine());
    }
}