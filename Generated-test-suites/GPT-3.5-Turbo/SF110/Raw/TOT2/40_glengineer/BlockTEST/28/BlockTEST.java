import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }
    
    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCoordinatesConstructor() {
        Scheme scheme = new Scheme(10, 10);
        new ConcreteBlock(scheme, -1, -1, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfRange() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        
        block.charAt(15, 15);
    }
    
    // More test cases for other methods
    
}