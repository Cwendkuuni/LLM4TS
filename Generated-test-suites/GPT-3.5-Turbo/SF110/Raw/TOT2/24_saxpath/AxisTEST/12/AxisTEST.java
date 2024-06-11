
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupInt() {
        assertEquals("child", Axis.lookup(1));
        assertEquals("descendant", Axis.lookup(2));
        assertEquals("parent", Axis.lookup(3));
        assertEquals(null, Axis.lookup(0));
        // Additional edge cases can be added here
    }

    @Test
    public void testLookupString() {
        assertEquals(1, Axis.lookup("child"));
        assertEquals(2, Axis.lookup("descendant"));
        assertEquals(3, Axis.lookup("parent"));
        assertEquals(0, Axis.lookup("invalid"));
        // Additional edge cases can be added here
    }
}