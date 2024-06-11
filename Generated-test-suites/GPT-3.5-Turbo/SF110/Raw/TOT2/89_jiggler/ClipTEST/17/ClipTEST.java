
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {
    
    @Test
    public void testClipIntConstructor() {
        Clip clip = new Clip(1, 10);
        assertNotNull(clip);
    }
    
    @Test
    public void testClipIntConstructorEdgeCases() {
        Clip clip = new Clip(0, 0);
        assertEquals(0, clip.int_min);
        assertEquals(0, clip.int_max);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testClipIntConstructorNegativeValues() {
        new Clip(-1, -10);
    }
}
