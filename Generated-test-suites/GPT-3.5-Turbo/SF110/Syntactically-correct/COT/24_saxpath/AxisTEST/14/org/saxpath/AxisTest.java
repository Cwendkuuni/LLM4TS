
import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNum() {
        // Test lookup function with valid axisNum
        int axisNum = Axis.CHILD;
        String expectedResult = "child";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLookupWithInvalidAxisNum() {
        // Test lookup function with invalid axisNum
        int axisNum = Axis.INVALID_AXIS;
        String result = Axis.lookup(axisNum);
        Assert.assertNull(result);
    }

    @Test
    public void testLookupWithValidAxisName() {
        // Test lookup function with valid axisName
        String axisName = "child";
        int expectedResult = Axis.CHILD;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        // Test lookup function with invalid axisName
        String axisName = "invalid-axis";
        int result = Axis.lookup(axisName);
        Assert.assertEquals(0, result);
    }
}
