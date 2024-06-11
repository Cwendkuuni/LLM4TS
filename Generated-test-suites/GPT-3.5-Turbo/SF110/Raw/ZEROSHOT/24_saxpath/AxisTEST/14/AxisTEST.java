package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookup() {
        int axisNum = 1;
        assertEquals("child", Axis.lookup(axisNum));
        
        axisNum = 2;
        assertEquals("descendant", Axis.lookup(axisNum));
        
        axisNum = 3;
        assertEquals("parent", Axis.lookup(axisNum));
        
        axisNum = 4;
        assertEquals("ancestor", Axis.lookup(axisNum));
        
        axisNum = 5;
        assertEquals("following-sibling", Axis.lookup(axisNum));
        
        axisNum = 6;
        assertEquals("preceding-sibling", Axis.lookup(axisNum));
        
        axisNum = 7;
        assertEquals("following", Axis.lookup(axisNum));
        
        axisNum = 8;
        assertEquals("preceding", Axis.lookup(axisNum));
        
        axisNum = 9;
        assertEquals("attribute", Axis.lookup(axisNum));
        
        axisNum = 10;
        assertEquals("namespace", Axis.lookup(axisNum));
        
        axisNum = 11;
        assertEquals("self", Axis.lookup(axisNum));
        
        axisNum = 12;
        assertEquals("descendant-or-self", Axis.lookup(axisNum));
        
        axisNum = 13;
        assertEquals("ancestor-or-self", Axis.lookup(axisNum));
        
        axisNum = 0;
        assertNull(Axis.lookup(axisNum));
    }
    
    @Test
    public void testLookupString() {
        String axisName = "child";
        assertEquals(1, Axis.lookup(axisName));
        
        axisName = "descendant";
        assertEquals(2, Axis.lookup(axisName));
        
        axisName = "parent";
        assertEquals(3, Axis.lookup(axisName));
        
        axisName = "ancestor";
        assertEquals(4, Axis.lookup(axisName));
        
        axisName = "following-sibling";
        assertEquals(5, Axis.lookup(axisName));
        
        axisName = "preceding-sibling";
        assertEquals(6, Axis.lookup(axisName));
        
        axisName = "following";
        assertEquals(7, Axis.lookup(axisName));
        
        axisName = "preceding";
        assertEquals(8, Axis.lookup(axisName));
        
        axisName = "attribute";
        assertEquals(9, Axis.lookup(axisName));
        
        axisName = "namespace";
        assertEquals(10, Axis.lookup(axisName));
        
        axisName = "self";
        assertEquals(11, Axis.lookup(axisName));
        
        axisName = "descendant-or-self";
        assertEquals(12, Axis.lookup(axisName));
        
        axisName = "ancestor-or-self";
        assertEquals(13, Axis.lookup(axisName));
        
        axisName = "invalid";
        assertEquals(0, Axis.lookup(axisName));
    }
}