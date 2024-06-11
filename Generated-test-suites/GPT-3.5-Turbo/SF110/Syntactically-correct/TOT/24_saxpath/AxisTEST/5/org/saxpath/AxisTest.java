
package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup_WithValidAxisNum_ReturnsValidAxisName() {
        Assert.assertEquals("child", Axis.lookup(Axis.CHILD));
        Assert.assertEquals("descendant", Axis.lookup(Axis.DESCENDANT));
        Assert.assertEquals("parent", Axis.lookup(Axis.PARENT));
        Assert.assertEquals("ancestor", Axis.lookup(Axis.ANCESTOR));
        Assert.assertEquals("following-sibling", Axis.lookup(Axis.FOLLOWING_SIBLING));
        Assert.assertEquals("preceding-sibling", Axis.lookup(Axis.PRECEDING_SIBLING));
        Assert.assertEquals("following", Axis.lookup(Axis.FOLLOWING));
        Assert.assertEquals("preceding", Axis.lookup(Axis.PRECEDING));
        Assert.assertEquals("attribute", Axis.lookup(Axis.ATTRIBUTE));
        Assert.assertEquals("namespace", Axis.lookup(Axis.NAMESPACE));
        Assert.assertEquals("self", Axis.lookup(Axis.SELF));
        Assert.assertEquals("descendant-or-self", Axis.lookup(Axis.DESCENDANT_OR_SELF));
        Assert.assertEquals("ancestor-or-self", Axis.lookup(Axis.ANCESTOR_OR_SELF));
    }

    @Test
    public void testLookup_WithInvalidAxisNum_ReturnsNull() {
        Assert.assertNull(Axis.lookup(Axis.INVALID_AXIS));
    }

    @Test
    public void testLookup_WithValidAxisName_ReturnsValidAxisNum() {
        Assert.assertEquals(Axis.CHILD, Axis.lookup("child"));
        Assert.assertEquals(Axis.DESCENDANT, Axis.lookup("descendant"));
        Assert.assertEquals(Axis.PARENT, Axis.lookup("parent"));
        Assert.assertEquals(Axis.ANCESTOR, Axis.lookup("ancestor"));
        Assert.assertEquals(Axis.FOLLOWING_SIBLING, Axis.lookup("following-sibling"));
        Assert.assertEquals(Axis.PRECEDING_SIBLING, Axis.lookup("preceding-sibling"));
        Assert.assertEquals(Axis.FOLLOWING, Axis.lookup("following"));
        Assert.assertEquals(Axis.PRECEDING, Axis.lookup("preceding"));
        Assert.assertEquals(Axis.ATTRIBUTE, Axis.lookup("attribute"));
        Assert.assertEquals(Axis.NAMESPACE, Axis.lookup("namespace"));
        Assert.assertEquals(Axis.SELF, Axis.lookup("self"));
        Assert.assertEquals(Axis.DESCENDANT_OR_SELF, Axis.lookup("descendant-or-self"));
        Assert.assertEquals(Axis.ANCESTOR_OR_SELF, Axis.lookup("ancestor-or-self"));
    }

    @Test
    public void testLookup_WithInvalidAxisName_ReturnsZero() {
        Assert.assertEquals(Axis.INVALID_AXIS, Axis.lookup("unknown"));
    }
}
