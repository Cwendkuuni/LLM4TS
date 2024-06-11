import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNum() {
        int axisNum = Axis.CHILD;
        String expected = "child";
        String actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithInvalidAxisNum() {
        int axisNum = Axis.INVALID_AXIS;
        String expected = null;
        String actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithValidAxisName() {
        String axisName = "child";
        int expected = Axis.CHILD;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        String axisName = "invalid";
        int expected = 0;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithNullAxisName() {
        String axisName = null;
        int expected = 0;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }

    // Add additional test cases for edge cases and exception handling scenarios here

}

