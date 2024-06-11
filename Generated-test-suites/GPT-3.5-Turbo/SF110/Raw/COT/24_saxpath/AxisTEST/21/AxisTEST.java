
package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNumber() {
        String axisName = Axis.lookup(1);
        Assert.assertEquals("child", axisName);

        axisName = Axis.lookup(2);
        Assert.assertEquals("descendant", axisName);

        axisName = Axis.lookup(3);
        Assert.assertEquals("parent", axisName);

        axisName = Axis.lookup(4);
        Assert.assertEquals("ancestor", axisName);

        axisName = Axis.lookup(5);
        Assert.assertEquals("following-sibling", axisName);

        axisName = Axis.lookup(6);
        Assert.assertEquals("preceding-sibling", axisName);

        axisName = Axis.lookup(7);
        Assert.assertEquals("following", axisName);

        axisName = Axis.lookup(8);
        Assert.assertEquals("preceding", axisName);

        axisName = Axis.lookup(9);
        Assert.assertEquals("attribute", axisName);

        axisName = Axis.lookup(10);
        Assert.assertEquals("namespace", axisName);

        axisName = Axis.lookup(11);
        Assert.assertEquals("self", axisName);

        axisName = Axis.lookup(12);
        Assert.assertEquals("descendant-or-self", axisName);

        axisName = Axis.lookup(13);
        Assert.assertEquals("ancestor-or-self", axisName);
    }

    @Test
    public void testLookupWithInvalidAxisNumber() {
        String axisName = Axis.lookup(0);
        Assert.assertNull(axisName);
    }

    @Test
    public void testLookupWithValidAxisName() {
        int axisNum = Axis.lookup("child");
        Assert.assertEquals(1, axisNum);

        axisNum = Axis.lookup("descendant");
        Assert.assertEquals(2, axisNum);

        axisNum = Axis.lookup("parent");
        Assert.assertEquals(3, axisNum);

        axisNum = Axis.lookup("ancestor");
        Assert.assertEquals(4, axisNum);

        axisNum = Axis.lookup("following-sibling");
        Assert.assertEquals(5, axisNum);

        axisNum = Axis.lookup("preceding-sibling");
        Assert.assertEquals(6, axisNum);

        axisNum = Axis.lookup("following");
        Assert.assertEquals(7, axisNum);

        axisNum = Axis.lookup("preceding");
        Assert.assertEquals(8, axisNum);

        axisNum = Axis.lookup("attribute");
        Assert.assertEquals(9, axisNum);

        axisNum = Axis.lookup("namespace");
        Assert.assertEquals(10, axisNum);

        axisNum = Axis.lookup("self");
        Assert.assertEquals(11, axisNum);

        axisNum = Axis.lookup("descendant-or-self");
        Assert.assertEquals(12, axisNum);

        axisNum = Axis.lookup("ancestor-or-self");
        Assert.assertEquals(13, axisNum);
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        int axisNum = Axis.lookup("invalid-axis");
        Assert.assertEquals(0, axisNum);
    }
}
