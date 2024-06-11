
package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNum_WithValidAxisNum_ReturnsCorrespondingAxisName() {
        int[] validAxisNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        String[] expectedAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (int i = 0; i < validAxisNums.length; i++) {
            int axisNum = validAxisNums[i];
            String expectedAxisName = expectedAxisNames[i];

            String actualAxisName = Axis.lookup(axisNum);

            Assert.assertEquals(expectedAxisName, actualAxisName);
        }
    }

    @Test
    public void testLookupByAxisNum_WithInvalidAxisNum_ReturnsNull() {
        int invalidAxisNum = -1;

        String actualAxisName = Axis.lookup(invalidAxisNum);

        Assert.assertNull(actualAxisName);
    }

    @Test
    public void testLookupByAxisName_WithValidAxisName_ReturnsCorrespondingAxisNum() {
        String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        int[] expectedAxisNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        for (int i = 0; i < validAxisNames.length; i++) {
            String axisName = validAxisNames[i];
            int expectedAxisNum = expectedAxisNums[i];

            int actualAxisNum = Axis.lookup(axisName);

            Assert.assertEquals(expectedAxisNum, actualAxisNum);
        }
    }

    @Test
    public void testLookupByAxisName_WithInvalidAxisName_ReturnsZero() {
        String invalidAxisName = "invalidAxisName";

        int actualAxisNum = Axis.lookup(invalidAxisName);

        Assert.assertEquals(0, actualAxisNum);
    }
}
