package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {
    @Test
    public void testLookup1() {
        assertEquals("child", Axis.lookup(1));
    }

    @Test
    public void testLookup2() {
        assertEquals("descendant", Axis.lookup(2));
    }

    @Test
    public void testLookup3() {
        assertEquals("parent", Axis.lookup(3));
    }

    @Test
    public void testLookup4() {
        assertEquals("ancestor", Axis.lookup(4));
    }

    @Test
    public void testLookup5() {
        assertEquals("following-sibling", Axis.lookup(5));
    }

    @Test
    public void testLookup6() {
        assertEquals("preceding-sibling", Axis.lookup(6));
    }

    @Test
    public void testLookup7() {
        assertEquals("following", Axis.lookup(7));
    }

    @Test
    public void testLookup8() {
        assertEquals("preceding", Axis.lookup(8));
    }

    @Test
    public void testLookup9() {
        assertEquals("attribute", Axis.lookup(9));
    }

    @Test
    public void testLookup10() {
        assertEquals("namespace", Axis.lookup(10));
    }

    @Test
    public void testLookup11() {
        assertEquals("self", Axis.lookup(11));
    }

    @Test
    public void testLookup12() {
        assertEquals("descendant-or-self", Axis.lookup(12));
    }

    @Test
    public void testLookup13() {
        assertEquals("ancestor-or-self", Axis.lookup(13));
    }

    @Test
    public void testLookupInvalid() {
        assertNull(Axis.lookup(0));
    }

    @Test
    public void testReverseLookupChild() {
        assertEquals(1, Axis.lookup("child"));
    }

    @Test
    public void testReverseLookupDescendant() {
        assertEquals(2, Axis.lookup("descendant"));
    }

    @Test
    public void testReverseLookupParent() {
        assertEquals(3, Axis.lookup("parent"));
    }

    @Test
    public void testReverseLookupAncestor() {
        assertEquals(4, Axis.lookup("ancestor"));
    }

    @Test
    public void testReverseLookupFollowingSibling() {
        assertEquals(5, Axis.lookup("following-sibling"));
    }

    @Test
    public void testReverseLookupPrecedingSibling() {
        assertEquals(6, Axis.lookup("preceding-sibling"));
    }

    @Test
    public void testReverseLookupFollowing() {
        assertEquals(7, Axis.lookup("following"));
    }

    @Test
    public void testReverseLookupPreceding() {
        assertEquals(8, Axis.lookup("preceding"));
    }

    @Test
    public void testReverseLookupAttribute() {
        assertEquals(9, Axis.lookup("attribute"));
    }

    @Test
    public void testReverseLookupNamespace() {
        assertEquals(10, Axis.lookup("namespace"));
    }

    @Test
    public void testReverseLookupSelf() {
        assertEquals(11, Axis.lookup("self"));
    }

    @Test
    public void testReverseLookupDescendantOrSelf() {
        assertEquals(12, Axis.lookup("descendant-or-self"));
    }

    @Test
    public void testReverseLookupAncestorOrSelf() {
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }

    @Test
    public void testReverseLookupInvalid() {
        assertEquals(0, Axis.lookup("invalid"));
    }
}