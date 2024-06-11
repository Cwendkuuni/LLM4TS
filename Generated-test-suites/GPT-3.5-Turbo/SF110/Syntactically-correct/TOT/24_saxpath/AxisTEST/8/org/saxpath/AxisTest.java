import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AxisTest {

    @Test
    public void testLookupForValidAxisNum() {
        // Typical use case
        assertEquals("child", Axis.lookup(1));

        // Edge case
        assertEquals("ancestor-or-self", Axis.lookup(13));
    }

    @Test
    public void testLookupForInvalidAxisNum() {
        // Typical use case
        assertNull(Axis.lookup(0));

        // Edge case
        assertNull(Axis.lookup(-1));
    }

    @Test
    public void testLookupForValidAxisName() {
        // Typical use case
        assertEquals(1, Axis.lookup("child"));

        // Edge case
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }

    @Test
    public void testLookupForInvalidAxisName() {
        // Typical use case
        assertEquals(0, Axis.lookup("invalid"));

        // Edge case
        assertEquals(0, Axis.lookup(""));
    }

}