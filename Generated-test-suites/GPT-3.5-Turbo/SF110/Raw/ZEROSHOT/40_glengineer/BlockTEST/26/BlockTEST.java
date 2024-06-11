import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testConstructor1() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        assertNotNull(block);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor2() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new TestBlock(scheme, p1, p2);
        assertNotNull(block);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor3() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme);
        assertNotNull(block);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 2, 2, 0, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCoordinates2() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, -1, 0, 0, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCoordinates3() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, scheme.x2 + 1, scheme.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCoordinates4() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, scheme.x2, scheme.y2 + 1);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertNotNull(subblock);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        char result = block.charAt(1, 1);
        assertEquals('e', result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        char result = block.charAt(2, 2);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        String result = block.textAt(0, 2, 1);
        assertEquals("de", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtOutOfBounds() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        String result = block.textAt(0, 3, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 2, 1, 2);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 1, 2, 2);
        boolean result = block.containsOnlyOneWord();
        assertFalse(result);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNotNull(subblocks);
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertNotNull(subblocks);
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        String result = block.toString();
        assertEquals("Block(0,0, 2,2):\n ######\n # ab #\n # de #\n ######", result);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new TestBlock(scheme, 0, 0, 2, 2);
        String result = block.getUpperLine();
        assertEquals("abc", result);
    }
}

class TestBlock extends Block {
    public TestBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    public TestBlock(Scheme scheme, CharPosition p1, CharPosition p2) {
        super(scheme, p1, p2);
    }
    
    public TestBlock(Scheme scheme) {
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
    
    public char charAt(int x, int y) {
        return '\0';
    }
    
    public String textAt(int x1, int x2, int y) {
        return "";
    }
    
    public String getUpperLine() {
        return "";
    }
}