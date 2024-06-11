import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        
        assertEquals(2, block.x1);
        assertEquals(3, block.y1);
        assertEquals(6, block.x2);
        assertEquals(8, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(2, 3);
        CharPosition p2 = new CharPosition(6, 8);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals(2, block.x1);
        assertEquals(3, block.y1);
        assertEquals(6, block.x2);
        assertEquals(8, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithoutCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 3, 6, 8);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        Block subblock = block.subblock(3, 4, 5, 6);
        
        assertEquals(3, subblock.x1);
        assertEquals(4, subblock.y1);
        assertEquals(5, subblock.x2);
        assertEquals(6, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        block.charAt(12, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        block.textAt(3, 9, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 8, 2, 8);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(4, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(2, subblock1.x1);
        assertEquals(3, subblock1.y1);
        assertEquals(6, subblock1.x2);
        assertEquals(4, subblock1.y2);
        assertEquals(scheme, subblock1.scheme);
        
        // Test other subblocks
        // ...
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(6, subblocks.size());
        
        // Test subblocks
        // ...
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        String expectedString = "Block(2,3, 6,8):\n" +
                                " ####\n" +
                                " #    #\n" +
                                " #    #\n" +
                                " #    #\n" +
                                " #    #\n" +
                                " #    #\n" +
                                " #    #\n" +
                                " #    #\n" +
                                " ####";
        
        assertEquals(expectedString, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 3, 6, 8);
        String upperLine = block.getUpperLine();
        
        assertEquals("    ", upperLine);
    }
    
}