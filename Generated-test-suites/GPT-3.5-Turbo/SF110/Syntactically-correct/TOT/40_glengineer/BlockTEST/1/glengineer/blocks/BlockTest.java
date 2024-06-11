import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testBlockConstructorWithValidArguments() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithNegativeX1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 1, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithNegativeY1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, -1, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidX1GreaterThanX2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 5, 1, 1, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidY1GreaterThanY2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 5, 5, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidX2GreaterThanSchemeX2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 15, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidY2GreaterThanSchemeY2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 15);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        Block subblock = block.subblock(2, 2, 4, 4);
        
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidCharPosition() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        char c = block.charAt(10, 10);
    }
    
    @Test
    public void testTextAtWithValidSublineCoodinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        String text = block.textAt(2, 4, 2);
        
        assertEquals("cd", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidSublineCoodinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        String text = block.textAt(4, 2, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 1, 1);
        
        boolean containsOneWord = block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        boolean containsOneWord = block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWordWithNonEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        scheme.lines[1] = "hello world";
        scheme.lines[2] = "hello world";
        scheme.lines[3] = "hello world";
        
        boolean containsOneWord = block.containsOnlyOneWord();
        
        assertTrue(containsOneWord);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        scheme.lines[1] = "hello world";
        scheme.lines[2] = "hello world";
        scheme.lines[3] = "hello world";
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(3, subblocks.size());
        assertEquals("hello world", subblocks.get(0).getUpperLine());
        assertEquals("hello world", subblocks.get(1).getUpperLine());
        assertEquals("hello world", subblocks.get(2).getUpperLine());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        scheme.lines[1] = "hello";
        scheme.lines[2] = "world";
        scheme.lines[3] = "hello";
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(2, subblocks.size());
        assertEquals("hello", subblocks.get(0).getUpperLine());
        assertEquals("world", subblocks.get(1).getUpperLine());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        scheme.lines[1] = "hello world";
        scheme.lines[2] = "hello world";
        scheme.lines[3] = "hello world";
        
        String str = block.toString();
        
        String expected = "Block(1,1, 5,5):\n" +
                          "##################\n" +
                          " # hello world #\n" +
                          " # hello world #\n" +
                          " # hello world #\n" +
                          "##################";
        
        assertEquals(expected, str);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        scheme.lines[1] = "hello world";
        
        String upperLine = block.getUpperLine();
        
        assertEquals("hello world", upperLine);
    }
}