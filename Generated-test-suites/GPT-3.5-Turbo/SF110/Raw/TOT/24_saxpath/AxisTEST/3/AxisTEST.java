
package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {
    
    @Test
    public void testLookupByAxisNum_child() {
        int axisNum = 1;
        String expected = "child";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_descendant() {
        int axisNum = 2;
        String expected = "descendant";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_parent() {
        int axisNum = 3;
        String expected = "parent";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_ancestor() {
        int axisNum = 4;
        String expected = "ancestor";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_followingSibling() {
        int axisNum = 5;
        String expected = "following-sibling";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_precedingSibling() {
        int axisNum = 6;
        String expected = "preceding-sibling";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_following() {
        int axisNum = 7;
        String expected = "following";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_preceding() {
        int axisNum = 8;
        String expected = "preceding";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_attribute() {
        int axisNum = 9;
        String expected = "attribute";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_namespace() {
        int axisNum = 10;
        String expected = "namespace";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_self() {
        int axisNum = 11;
        String expected = "self";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_descendantOrSelf() {
        int axisNum = 12;
        String expected = "descendant-or-self";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_ancestorOrSelf() {
        int axisNum = 13;
        String expected = "ancestor-or-self";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisNum_invalidAxisNum() {
        int axisNum = -1;
        String expected = null;
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_child() {
        String axisName = "child";
        int expected = 1;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_descendant() {
        String axisName = "descendant";
        int expected = 2;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_parent() {
        String axisName = "parent";
        int expected = 3;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_ancestor() {
        String axisName = "ancestor";
        int expected = 4;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_followingSibling() {
        String axisName = "following-sibling";
        int expected = 5;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_precedingSibling() {
        String axisName = "preceding-sibling";
        int expected = 6;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_following() {
        String axisName = "following";
        int expected = 7;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_preceding() {
        String axisName = "preceding";
        int expected = 8;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_attribute() {
        String axisName = "attribute";
        int expected = 9;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_namespace() {
        String axisName = "namespace";
        int expected = 10;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_self() {
        String axisName = "self";
        int expected = 11;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_descendantOrSelf() {
        String axisName = "descendant-or-self";
        int expected = 12;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_ancestorOrSelf() {
        String axisName = "ancestor-or-self";
        int expected = 13;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupByAxisName_invalidAxisName() {
        String axisName = "invalid";
        int expected = 0;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
}
