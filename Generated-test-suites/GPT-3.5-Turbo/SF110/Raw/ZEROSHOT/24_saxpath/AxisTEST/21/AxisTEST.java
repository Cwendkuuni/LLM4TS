package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup() {
        Assert.assertEquals("child", Axis.lookup(1));
        Assert.assertEquals("descendant", Axis.lookup(2));
        Assert.assertEquals("parent", Axis.lookup(3));
        Assert.assertEquals("ancestor", Axis.lookup(4));
        Assert.assertEquals("following-sibling", Axis.lookup(5));
        Assert.assertEquals("preceding-sibling", Axis.lookup(6));
        Assert.assertEquals("following", Axis.lookup(7));
        Assert.assertEquals("preceding", Axis.lookup(8));
        Assert.assertEquals("attribute", Axis.lookup(9));
        Assert.assertEquals("namespace", Axis.lookup(10));
        Assert.assertEquals("self", Axis.lookup(11));
        Assert.assertEquals("descendant-or-self", Axis.lookup(12));
        Assert.assertEquals("ancestor-or-self", Axis.lookup(13));
        Assert.assertNull(Axis.lookup(0));
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        Assert.assertEquals(0, Axis.lookup("invalidAxis"));
    }

    @Test
    public void testLookupWithValidAxisName() {
        Assert.assertEquals(1, Axis.lookup("child"));
        Assert.assertEquals(2, Axis.lookup("descendant"));
        Assert.assertEquals(3, Axis.lookup("parent"));
        Assert.assertEquals(4, Axis.lookup("ancestor"));
        Assert.assertEquals(5, Axis.lookup("following-sibling"));
        Assert.assertEquals(6, Axis.lookup("preceding-sibling"));
        Assert.assertEquals(7, Axis.lookup("following"));
        Assert.assertEquals(8, Axis.lookup("preceding"));
        Assert.assertEquals(9, Axis.lookup("attribute"));
        Assert.assertEquals(10, Axis.lookup("namespace"));
        Assert.assertEquals(11, Axis.lookup("self"));
        Assert.assertEquals(12, Axis.lookup("descendant-or-self"));
        Assert.assertEquals(13, Axis.lookup("ancestor-or-self"));
    }
}