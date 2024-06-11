import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new MockBlock(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    // More test methods for other public methods can be added here
    
}

class MockBlock extends Block {
    
    public MockBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    // Implement abstract methods if needed for testing
    
}