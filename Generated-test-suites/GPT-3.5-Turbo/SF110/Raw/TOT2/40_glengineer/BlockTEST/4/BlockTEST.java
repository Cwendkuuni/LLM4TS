import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new SampleBlock(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new SampleBlock(scheme);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            block.containsOnlyOneWord();
        });
        assertEquals("cannot search in a trivial block", exception.getMessage());
    }
    
    // More test cases can be added here for other methods

}