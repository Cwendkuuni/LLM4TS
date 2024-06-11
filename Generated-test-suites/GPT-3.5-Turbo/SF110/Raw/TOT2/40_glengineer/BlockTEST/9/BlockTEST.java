import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testConstructorWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }

    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }

    @Test
    public void testConstructorWithDefaultValues() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
    }

    // Additional tests to be added for remaining methods in Block class
}