import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructor1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertNotNull(block);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructor2() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertNotNull(block);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructor3() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        assertNotNull(block);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor4() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 0, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor5() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, -1, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor6() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 11, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor7() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 11);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor8() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 5, 5, 0, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor9() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 5, 5, 0);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 4, 4);
        assertNotNull(subblock);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        char c = block.charAt(6, 6);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String text = block.textAt(1, 3, 2);
        assertNotNull(text);
        assertEquals("ef", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtOutOfBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String text = block.textAt(1, 6, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 1, 1);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNotNull(subblocks);
        assertEquals(1, subblocks.size());
        Block subblock = subblocks.get(0);
        assertNotNull(subblock);
        assertEquals(0, subblock.x1);
        assertEquals(0, subblock.y1);
        assertEquals(5, subblock.x2);
        assertEquals(5, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertNotNull(subblocks);
        assertEquals(1, subblocks.size());
        Block subblock = subblocks.get(0);
        assertNotNull(subblock);
        assertEquals(0, subblock.x1);
        assertEquals(0, subblock.y1);
        assertEquals(5, subblock.x2);
        assertEquals(5, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String str = block.toString();
        assertNotNull(str);
        assertTrue(str.contains("Block(0,0, 5,5)"));
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String line = block.getUpperLine();
        assertNotNull(line);
        assertEquals("abcde", line);
    }
}