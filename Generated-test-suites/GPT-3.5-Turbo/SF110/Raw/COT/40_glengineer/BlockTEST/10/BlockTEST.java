
import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        
        assertEquals("Incorrect x1 value", 0, block.x1);
        assertEquals("Incorrect y1 value", 0, block.y1);
        assertEquals("Incorrect x2 value", 5, block.x2);
        assertEquals("Incorrect y2 value", 5, block.y2);
        assertEquals("Incorrect scheme value", scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals("Incorrect x1 value", 0, block.x1);
        assertEquals("Incorrect y1 value", 0, block.y1);
        assertEquals("Incorrect x2 value", 5, block.x2);
        assertEquals("Incorrect y2 value", 5, block.y2);
        assertEquals("Incorrect scheme value", scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorDefault() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        
        assertEquals("Incorrect x1 value", 0, block.x1);
        assertEquals("Incorrect y1 value", 0, block.y1);
        assertEquals("Incorrect x2 value", scheme.x2, block.x2);
        assertEquals("Incorrect y2 value", scheme.y2, block.y2);
        assertEquals("Incorrect scheme value", scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        int p0 = 1;
        int p1 = 1;
        int p2 = 2;
        int p3 = 2;
        Block subblock = block.subblock(p0, p1, p2, p3);
        
        assertEquals("Incorrect x1 value", p0, subblock.x1);
        assertEquals("Incorrect y1 value", p1, subblock.y1);
        assertEquals("Incorrect x2 value", p2, subblock.x2);
        assertEquals("Incorrect y2 value", p3, subblock.y2);
        assertEquals("Incorrect scheme value", scheme, subblock.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.charAt(20, 20);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        char expected = scheme.lines[0].charAt(0);
        char actual = block.charAt(0, 0);
        
        assertEquals("Incorrect char value", expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtImproperCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.textAt(20, 30, 0);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String expected = scheme.lines[0].substring(0, 5);
        String actual = block.textAt(0, 5, 0);
        
        assertEquals("Incorrect subline value", expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 0, 0);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.scheme.lines[0] = "wordwordword";
        block.scheme.lines[1] = "............";
        block.scheme.lines[2] = "............";
        block.scheme.lines[3] = "............";
        block.scheme.lines[4] = "............";
        
        boolean expected = false;
        boolean actual = block.containsOnlyOneWord();
        
        assertEquals("Incorrect containsOnlyOneWord value", expected, actual);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals("Incorrect number of subblocks", 1, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals("Incorrect number of subblocks", 1, subblocks.size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        
        String expected = "Block(0,0, 5,5):\n"
                          + " ######\n"
                          + " #      \n"
                          + " #      \n"
                          + " #      \n"
                          + " ######";
        String actual = block.toString();
        
        assertEquals("Incorrect block string representation", expected, actual);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        
        String expected = scheme.lines[0];
        String actual = block.getUpperLine();
        
        assertEquals("Incorrect upper line value", expected, actual);
    }
}
