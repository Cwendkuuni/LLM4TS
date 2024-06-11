
package org.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {
    @Test
    public void testLookup1() {
        int result = Axis.lookup("child");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testLookup2() {
        int result = Axis.lookup(2);
        Assert.assertEquals("descendant", Axis.lookup(result));
    }

    @Test
    public void testLookup3() {
        int result = Axis.lookup(3);
        Assert.assertEquals("parent", Axis.lookup(result));
    }

    @Test
    public void testLookup4() {
        int result = Axis.lookup(4);
        Assert.assertEquals("ancestor", Axis.lookup(result));
    }

    @Test
    public void testLookup5() {
        int result = Axis.lookup(5);
        Assert.assertEquals("following-sibling", Axis.lookup(result));
    }

    @Test
    public void testLookup6() {
        int result = Axis.lookup(6);
        Assert.assertEquals("preceding-sibling", Axis.lookup(result));
    }

    @Test
    public void testLookup7() {
        int result = Axis.lookup(7);
        Assert.assertEquals("following", Axis.lookup(result));
    }

    @Test
    public void testLookup8() {
        int result = Axis.lookup(8);
        Assert.assertEquals("preceding", Axis.lookup(result));
    }

    @Test
    public void testLookup9() {
        int result = Axis.lookup(9);
        Assert.assertEquals("attribute", Axis.lookup(result));
    }

    @Test
    public void testLookup10() {
        int result = Axis.lookup(10);
        Assert.assertEquals("namespace", Axis.lookup(result));
    }

    @Test
    public void testLookup11() {
        int result = Axis.lookup(11);
        Assert.assertEquals("self", Axis.lookup(result));
    }

    @Test
    public void testLookup12() {
        int result = Axis.lookup(12);
        Assert.assertEquals("descendant-or-self", Axis.lookup(result));
    }

    @Test
    public void testLookup13() {
        int result = Axis.lookup(13);
        Assert.assertEquals("ancestor-or-self", Axis.lookup(result));
    }
}
