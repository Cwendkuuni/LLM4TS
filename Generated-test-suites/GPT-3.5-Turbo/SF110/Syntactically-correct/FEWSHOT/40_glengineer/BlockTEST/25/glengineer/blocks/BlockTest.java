package glengineer.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class BlockTest {
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block = new Block(scheme, 1, 1, 5, 5);
    }
    
    @Test
    public void testConstructor_ValidParameters() {
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor_InvalidParameters() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Block invalidBlock = new Block(scheme, 6, 6, 5, 5);
        });
        assertEquals("improper block definition: 6,6, 5,5.", exception.getMessage());
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt_ValidPosition() {
        char result = block.charAt(3, 3);
        assertEquals('a', result);
    }
    
    @Test
    public void testCharAt_InvalidPosition() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            char result = block.charAt(6, 6);
        });
        assertEquals("char position (6,6) out of block.\n" + block, exception.getMessage());
    }
    
    @Test
    public void testTextAt_ValidCoordinates() {
        String result = block.textAt(2, 4, 3);
        assertEquals("bcd", result);
    }
    
    @Test
    public void testTextAt_InvalidCoordinates() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            String result = block.textAt(5, 6, 3);
        });
        assertEquals("Improper subline coodinates.", exception.getMessage());
    }
    
    @Test
    public void testContainsOnlyOneWord_TrivialBlock() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            boolean result = block.containsOnlyOneWord();
        });
        assertEquals("cannot search in a trivial block", exception.getMessage());
    }
    
    @Test
    public void testContainsOnlyOneWord_EmptyBlock() {
        Block emptyBlock = new Block(scheme, 1, 5, 5, 5);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            boolean result = emptyBlock.containsOnlyOneWord();
        });
        assertEquals("cannot verify unicity of an word in an empty block", exception.getMessage());
    }
    
    @Test
    public void testToString() {
        String result = block.toString();
        String expected = "Block(1,1, 5,5):\n" +
                          " #######\n" +
                          " # ... #\n" +
                          " # ... #\n" +
                          " # ... #\n" +
                          " #######";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetUpperLine() {
        String result = block.getUpperLine();
        assertEquals(" ... ", result);
    }
}