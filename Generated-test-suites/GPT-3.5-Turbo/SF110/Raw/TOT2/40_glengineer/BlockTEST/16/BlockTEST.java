import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testConstructor1() {
        // Test constructor with proper block definition
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructor2() {
        // Test constructor with invalid block definition
        Scheme scheme = new Scheme();
        try {
            Block block = new ConcreteBlock(scheme, 2, 2, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 2,2, 0,0.", e.getMessage());
        }
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        char result = block.charAt(1, 1);
        assertEquals(' ', result);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        String result = block.textAt(0, 2, 1);
        assertEquals(" ", result);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        boolean result = block.containsOnlyOneWord();
        assertTrue(result);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        List<Block> result = block.extractSequentialSubblocks();
        assertEquals(0, result.size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        List<Block> result = block.extractParallelSubblocks();
        assertEquals(0, result.size());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        String result = block.toString();
        assertEquals("Block(0,0, 2,2):\n #  #\n #  #\n ####", result);
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2); 
        String result = block.getUpperLine();
        assertEquals(" ", result);
    }
}