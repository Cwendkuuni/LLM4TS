package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNumber_ReturnsExpectedString() {
        int axisNum = Axis.CHILD;
        String expected = "child";
        String result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.DESCENDANT;
        expected = "descendant";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.PARENT;
        expected = "parent";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.ANCESTOR;
        expected = "ancestor";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.FOLLOWING_SIBLING;
        expected = "following-sibling";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.PRECEDING_SIBLING;
        expected = "preceding-sibling";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.FOLLOWING;
        expected = "following";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.PRECEDING;
        expected = "preceding";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.ATTRIBUTE;
        expected = "attribute";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.NAMESPACE;
        expected = "namespace";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.SELF;
        expected = "self";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.DESCENDANT_OR_SELF;
        expected = "descendant-or-self";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);

        axisNum = Axis.ANCESTOR_OR_SELF;
        expected = "ancestor-or-self";
        result = Axis.lookup(axisNum);
        assertEquals(expected, result);
    }

    @Test
    public void testLookup_InvalidAxisNumber_ReturnsNull() {
        int axisNum = Axis.INVALID_AXIS;
        String result = Axis.lookup(axisNum);
        assertNull(result);
    }

    @Test
    public void testLookup_ValidAxisName_ReturnsExpectedNumber() {
        String axisName = "child";
        int expected = Axis.CHILD;
        int result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "descendant";
        expected = Axis.DESCENDANT;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "parent";
        expected = Axis.PARENT;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "ancestor";
        expected = Axis.ANCESTOR;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "following-sibling";
        expected = Axis.FOLLOWING_SIBLING;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "preceding-sibling";
        expected = Axis.PRECEDING_SIBLING;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "following";
        expected = Axis.FOLLOWING;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "preceding";
        expected = Axis.PRECEDING;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "attribute";
        expected = Axis.ATTRIBUTE;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "namespace";
        expected = Axis.NAMESPACE;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "self";
        expected = Axis.SELF;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "descendant-or-self";
        expected = Axis.DESCENDANT_OR_SELF;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);

        axisName = "ancestor-or-self";
        expected = Axis.ANCESTOR_OR_SELF;
        result = Axis.lookup(axisName);
        assertEquals(expected, result);
    }

    @Test
    public void testLookup_InvalidAxisName_ReturnsZero() {
        String axisName = "invalid";
        int result = Axis.lookup(axisName);
        assertEquals(0, result);
    }
}