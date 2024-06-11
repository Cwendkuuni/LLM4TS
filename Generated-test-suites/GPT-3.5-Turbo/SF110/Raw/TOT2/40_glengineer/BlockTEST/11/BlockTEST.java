import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testBlockConstructorWithCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testBlockConstructorWithDefaultValues() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(10, block.getX2());
        assertEquals(10, block.getY2());
        assertEquals(scheme, block.getScheme());
    }
    
    // Add more test cases for other public methods and edge cases
    
}