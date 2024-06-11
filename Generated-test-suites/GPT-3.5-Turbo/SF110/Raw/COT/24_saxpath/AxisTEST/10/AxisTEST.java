
import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNum() {
        String expectedResult = "child";
        int axisNum = 1;
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLookupWithInvalidAxisNum() {
        int invalidAxisNum = 100;
        String result = Axis.lookup(invalidAxisNum);
        Assert.assertNull(result);
    }

    @Test
    public void testLookupWithValidAxisName() {
        int expectedResult = 2;
        String axisName = "descendant";
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        String invalidAxisName = "invalid";
        int result = Axis.lookup(invalidAxisName);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testLookupWithNullAxisName() {
        int result = Axis.lookup(null);
        Assert.assertEquals(0, result);
    }
}