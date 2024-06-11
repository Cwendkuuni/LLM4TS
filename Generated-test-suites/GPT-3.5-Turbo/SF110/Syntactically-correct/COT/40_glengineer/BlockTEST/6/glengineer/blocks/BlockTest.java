import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test
    public void testConstructor1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor2() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor3() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 0, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidCoordinates2() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 20);
        Block block = new ConcreteBlock(scheme, p1, p2);
    }
    
    // Add more test cases for each method
    
}