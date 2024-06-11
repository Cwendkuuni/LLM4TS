
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    
    // Test Block constructor with scheme, x1, y1, x2, y2 parameters
    @Test
    public void testConstructorWithParameters() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 10, 10);
        
        assertEquals("x1 is incorrect", 1, block.x1);
        assertEquals("y1 is incorrect", 1, block.y1);
        assertEquals("x2 is incorrect", 10, block.x2);
        assertEquals("y2 is incorrect", 10, block.y2);
        assertEquals("scheme is incorrect", scheme, block.scheme);
    }
    
    // Test Block constructor with scheme, p1, p2 parameters
    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(10, 10);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals("x1 is incorrect", 1, block.x1);
        assertEquals("y1 is incorrect", 1, block.y1);
        assertEquals("x2 is incorrect", 10, block.x2);
        assertEquals("y2 is incorrect", 10, block.y2);
        assertEquals("scheme is incorrect", scheme, block.scheme);
    }
    
    // Test Block constructor with scheme
    @Test
    public void testConstructorWithScheme() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme);
        
        assertEquals("x1 is incorrect", 0, block.x1);
        assertEquals("y1 is incorrect", 0, block.y1);
        assertEquals("x2 is incorrect", scheme.x2, block.x2);
        assertEquals("y2 is incorrect", scheme.y2, block.y2);
        assertEquals("scheme is incorrect", scheme, block.scheme);
    }
    
    // Test the subblock method
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 10, 10);
        Block subblock = block.subblock(2, 2, 9, 9);
        
        assertEquals("x1 is incorrect", 2, subblock.x1);
        assertEquals("y1 is incorrect", 2, subblock.y1);
        assertEquals("x2 is incorrect", 9, subblock.x2);
        assertEquals("y2 is incorrect", 9, subblock.y2);
        assertEquals("scheme is incorrect", scheme, subblock.scheme);
    }
    
    // Test the charAt method
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 10, 10);
        char ch = block.charAt(5, 5);
        
        assertEquals("char is incorrect", '#', ch);
    }
    
    // Test the textAt method
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 10, 10);
        String text = block.textAt(2, 7, 2);
        
        assertEquals("text is incorrect", "#####", text);
    }
    
    // Test the containsOnlyOneWord method with a trivial block
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme);
        
        block.containsOnlyOneWord();
    }
    
    // Test the containsOnlyOneWord method with an empty block
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 1, 1);
        
        block.containsOnlyOneWord();
    }
    
    // Test the extractSequentialSubblocks method
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 10, 10);
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals("number of subblocks is incorrect", 0, subblocks.size());
    }
    
    // Test the extractParallelSubblocks method
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 10, 10);
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals("number of subblocks is incorrect", 0, subblocks.size());
    }
    
    // Test the toString method
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        String expected = "Block(1,1, 5,5):\n ######\n #     \n #     \n #     \n ######";
        
        assertEquals("toString is incorrect", expected, block.toString());
    }
    
    // Test the getUpperLine method
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        String line = block.getUpperLine();
        
        assertEquals("line is incorrect", "######", line);
    }
}

