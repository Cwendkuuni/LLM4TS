import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }
    
    // Add more test cases for other methods
    
}