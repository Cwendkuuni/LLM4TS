import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new MockBlock(scheme, 0, 0, 5, 5);
        assertEquals("Scheme should be initialized", scheme, block.scheme);
        assertEquals("x1 should be initialized", 0, block.x1);
        assertEquals("y1 should be initialized", 0, block.y1);
        assertEquals("x2 should be initialized", 5, block.x2);
        assertEquals("y2 should be initialized", 5, block.y2);
    }

    // More test methods for other public methods in Block class

}