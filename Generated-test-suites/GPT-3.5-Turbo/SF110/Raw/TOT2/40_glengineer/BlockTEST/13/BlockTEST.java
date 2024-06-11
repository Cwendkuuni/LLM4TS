import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testBlockConstructorWithCoordinates() {
        // Test 1: Valid input coordinates
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }
    
    @Test
    public void testBlockSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 2, 6, 6);
        
        Block subblock = block.subblock(3, 3, 5, 5);
        
        assertEquals(3, subblock.getX1());
        assertEquals(3, subblock.getY1());
        assertEquals(5, subblock.getX2());
        assertEquals(5, subblock.getY2());
        assertEquals(scheme, subblock.getScheme());
    }
    
    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        char result = block.charAt(1, 1);
        
        assertEquals(scheme.lines[1].charAt(1), result);
    }
    
    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        
        String result = block.textAt(1, 3, 1);
        
        assertEquals(scheme.lines[1].substring(1, 3), result);
    }
    
    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        
        String result = block.getUpperLine();
        
        assertEquals(scheme.lines[0].substring(0, 4), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 5, 5, 2, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockContainsOnlyOneWordEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        List<Block> subblocks = block.extractSequentialSubblocks();

        // Assert specific conditions here
    }
    
    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        List<Block> subblocks = block.extractParallelSubblocks();

        // Assert specific conditions here
    }
}