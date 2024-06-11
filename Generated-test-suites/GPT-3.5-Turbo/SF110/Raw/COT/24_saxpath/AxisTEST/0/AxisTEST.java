import org.junit.Test;

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
        int axisNum = 14;
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
        String axisName = "invalidAxis";
        int expected = 0;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }
  
}