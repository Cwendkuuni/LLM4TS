import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void lookup_ValidAxisNum_ReturnsCorrectAxisName() {
        // Typical case - Axis 1 (CHILD)
        String expectedAxisName1 = "child";
        int axisNum1 = 1;
        String actualAxisName1 = Axis.lookup(axisNum1);
        Assert.assertEquals(expectedAxisName1, actualAxisName1);

        // Typical case - Axis 5 (FOLLOWING_SIBLING)
        String expectedAxisName2 = "following-sibling";
        int axisNum2 = 5;
        String actualAxisName2 = Axis.lookup(axisNum2);
        Assert.assertEquals(expectedAxisName2, actualAxisName2);

        // Typical case - Axis 13 (ANCESTOR_OR_SELF)
        String expectedAxisName3 = "ancestor-or-self";
        int axisNum3 = 13;
        String actualAxisName3 = Axis.lookup(axisNum3);
        Assert.assertEquals(expectedAxisName3, actualAxisName3);

        // Edge case - Axis 0 (INVALID_AXIS)
        String expectedAxisName4 = null;
        int axisNum4 = 0;
        String actualAxisName4 = Axis.lookup(axisNum4);
        Assert.assertEquals(expectedAxisName4, actualAxisName4);

        // Edge case - Axis 14 (Any invalid axis number greater than 13)
        String expectedAxisName5 = null;
        int axisNum5 = 14;
        String actualAxisName5 = Axis.lookup(axisNum5);
        Assert.assertEquals(expectedAxisName5, actualAxisName5);
    }

    @Test
    public void lookup_ValidAxisName_ReturnsCorrectAxisNum() {
        // Typical case - axisName "following"
        int expectedAxisNum1 = 7;
        String axisName1 = "following";
        int actualAxisNum1 = Axis.lookup(axisName1);
        Assert.assertEquals(expectedAxisNum1, actualAxisNum1);

        // Typical case - axisName "namespace"
        int expectedAxisNum2 = 10;
        String axisName2 = "namespace";
        int actualAxisNum2 = Axis.lookup(axisName2);
        Assert.assertEquals(expectedAxisNum2, actualAxisNum2);

        // Edge case - axisName "invalid"
        int expectedAxisNum3 = 0;
        String axisName3 = "invalid";
        int actualAxisNum3 = Axis.lookup(axisName3);
        Assert.assertEquals(expectedAxisNum3, actualAxisNum3);
    }
}