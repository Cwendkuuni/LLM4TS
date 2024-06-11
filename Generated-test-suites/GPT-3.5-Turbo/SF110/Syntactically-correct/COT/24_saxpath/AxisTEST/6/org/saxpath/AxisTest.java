
import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    // Existing test methods
    
    // Additional test cases
    
    @Test
    public void testLookupWithNegativeAxisNum() {
        assertNull(Axis.lookup(-1));
    }

    @Test
    public void testLookupWithLargeAxisNum() {
        assertNull(Axis.lookup(20));
    }

    @Test
    public void testLookupWithEmptyAxisName() {
        assertEquals(0, Axis.lookup(""));
    }

    @Test
    public void testLookupWithNullAxisName() {
        assertEquals(0, Axis.lookup(null));
    }
}
