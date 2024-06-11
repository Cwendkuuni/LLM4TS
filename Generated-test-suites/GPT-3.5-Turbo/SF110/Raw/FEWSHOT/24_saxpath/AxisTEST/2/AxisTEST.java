package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {
    @Test
    public void testLookup_axisNum_1() {
        assertEquals("child", Axis.lookup(1));
    }

    @Test
    public void testLookup_axisNum_2() {
        assertEquals("descendant", Axis.lookup(2));
    }

    @Test
    public void testLookup_axisNum_3() {
        assertEquals("parent", Axis.lookup(3));
    }

    @Test
    public void testLookup_axisNum_4() {
        assertEquals("ancestor", Axis.lookup(4));
    }

    @Test
    public void testLookup_axisNum_5() {
        assertEquals("following-sibling", Axis.lookup(5));
    }

    @Test
    public void testLookup_axisNum_6() {
        assertEquals("preceding-sibling", Axis.lookup(6));
    }

    @Test
    public void testLookup_axisNum_7() {
        assertEquals("following", Axis.lookup(7));
    }

    @Test
    public void testLookup_axisNum_8() {
        assertEquals("preceding", Axis.lookup(8));
    }

    @Test
    public void testLookup_axisNum_9() {
        assertEquals("attribute", Axis.lookup(9));
    }

    @Test
    public void testLookup_axisNum_10() {
        assertEquals("namespace", Axis.lookup(10));
    }

    @Test
    public void testLookup_axisNum_11() {
        assertEquals("self", Axis.lookup(11));
    }

    @Test
    public void testLookup_axisNum_12() {
        assertEquals("descendant-or-self", Axis.lookup(12));
    }

    @Test
    public void testLookup_axisNum_13() {
        assertEquals("ancestor-or-self", Axis.lookup(13));
    }

    @Test
    public void testLookup_axisNum_invalid() {
        assertNull(Axis.lookup(0));
    }

    @Test
    public void testLookup_axisName_child() {
        assertEquals(1, Axis.lookup("child"));
    }

    @Test
    public void testLookup_axisName_descendant() {
        assertEquals(2, Axis.lookup("descendant"));
    }

    @Test
    public void testLookup_axisName_parent() {
        assertEquals(3, Axis.lookup("parent"));
    }

    @Test
    public void testLookup_axisName_ancestor() {
        assertEquals(4, Axis.lookup("ancestor"));
    }

    @Test
    public void testLookup_axisName_followingSibling() {
        assertEquals(5, Axis.lookup("following-sibling"));
    }

    @Test
    public void testLookup_axisName_precedingSibling() {
        assertEquals(6, Axis.lookup("preceding-sibling"));
    }

    @Test
    public void testLookup_axisName_following() {
        assertEquals(7, Axis.lookup("following"));
    }

    @Test
    public void testLookup_axisName_preceding() {
        assertEquals(8, Axis.lookup("preceding"));
    }

    @Test
    public void testLookup_axisName_attribute() {
        assertEquals(9, Axis.lookup("attribute"));
    }

    @Test
    public void testLookup_axisName_namespace() {
        assertEquals(10, Axis.lookup("namespace"));
    }

    @Test
    public void testLookup_axisName_self() {
        assertEquals(11, Axis.lookup("self"));
    }

    @Test
    public void testLookup_axisName_descendantOrSelf() {
        assertEquals(12, Axis.lookup("descendant-or-self"));
    }

    @Test
    public void testLookup_axisName_ancestorOrSelf() {
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }

    @Test
    public void testLookup_axisName_invalid() {
        assertEquals(0, Axis.lookup(""));
        assertNull(Axis.lookup(null));
    }
}