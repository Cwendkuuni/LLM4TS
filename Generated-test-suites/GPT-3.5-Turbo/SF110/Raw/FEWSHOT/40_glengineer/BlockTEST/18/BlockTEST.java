package glengineer.blocks;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new Block(scheme, p1, p2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithoutParameters() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 4, 4);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));
        assertEquals('d', block.charAt(0, 1));
        assertEquals('e', block.charAt(1, 1));
        assertEquals('f', block.charAt(2, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBlock() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        block.charAt(3, 0);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("de", block.textAt(0, 2, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtImproperCoordinates() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        block.textAt(2, 0, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abcdef", "abcdef"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        boolean result = block.containsOnlyOneWord();
        assertTrue(result);
        
        scheme.lines = new String[]{"abefcd", "abcdef"};
        block = new Block(scheme, 0, 0, 3, 2);
        result = block.containsOnlyOneWord();
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        String expected = "Block(0,0, 3,2):\n" +
                          " #######\n" +
                          " # abc #\n" +
                          " # def #\n" +
                          " #######";
        String result = block.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abcdef", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        String result = block.getUpperLine();
        assertEquals("abc", result);
    }
}