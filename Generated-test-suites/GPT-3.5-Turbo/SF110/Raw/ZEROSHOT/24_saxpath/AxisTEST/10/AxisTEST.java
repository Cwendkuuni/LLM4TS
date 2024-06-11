package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupIntChildAxisValid() {
        int axisNum = 1;
        String expected = "child";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupIntChildAxisInvalid() {
        int axisNum = 14;
        String result = Axis.lookup(axisNum);
        Assert.assertNull(result);
    }
    
    @Test
    public void testLookupIntDescendantAxisValid() {
        int axisNum = 2;
        String expected = "descendant";
        String result = Axis.lookup(axisNum);
        Assert.assertEquals(expected, result);
    }
    
    // Continue with the rest of the lookup(int) tests
    
    @Test
    public void testLookupStringChildAxisValid() {
        String axisName = "child";
        int expected = 1;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookupStringChildAxisInvalid() {
        String axisName = "invalid";
        int result = Axis.lookup(axisName);
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testLookupStringDescendantAxisValid() {
        String axisName = "descendant";
        int expected = 2;
        int result = Axis.lookup(axisName);
        Assert.assertEquals(expected, result);
    }
    
    // Continue with the rest of the lookup(String) tests
    
}