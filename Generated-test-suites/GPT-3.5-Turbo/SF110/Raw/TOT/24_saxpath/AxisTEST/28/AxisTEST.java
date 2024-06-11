import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookup_ByAxisNum_ValidAxis_ReturnsCorrectAxisName() {
        // Typical use case
        assertEquals("child", Axis.lookup(1));

        // Edge case
        assertEquals("ancestor-or-self", Axis.lookup(13));

        // Error scenario
        assertNull(Axis.lookup(14));
    }

    @Test
    public void testLookup_ByAxisName_ValidAxis_ReturnsCorrectAxisNum() {
        // Typical use case
        assertEquals(3, Axis.lookup("parent"));

        // Edge case
        assertEquals(9, Axis.lookup("attribute"));

        // Error scenario
        assertEquals(0, Axis.lookup("invalid-axis"));
    }

    @Test
    public void testLookup_ByAxisNum_InvalidAxis_ReturnsNull() {
        assertEquals(null, Axis.lookup(0));
    }

    @Test
    public void testLookup_ByAxisName_InvalidAxis_ReturnsZero() {
        assertEquals(0, Axis.lookup("invalid-axis"));
    }
}