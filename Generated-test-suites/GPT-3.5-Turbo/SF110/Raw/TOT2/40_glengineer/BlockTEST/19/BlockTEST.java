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
    }

    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
    }

    @Test
    public void testBlockConstructorWithScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(10, block.getX2());
        assertEquals(10, block.getY2());
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
    }

    // Add more test cases for other methods as needed
    
}