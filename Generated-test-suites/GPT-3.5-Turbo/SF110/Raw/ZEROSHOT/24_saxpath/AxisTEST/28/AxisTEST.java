package org.saxpath;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testLookupChild() {
        assertEquals(1, Axis.lookup("child"));
    }

    @Test
    public void testLookupDescendant() {
        assertEquals(2, Axis.lookup("descendant"));
    }

    @Test
    public void testLookupParent() {
        assertEquals(3, Axis.lookup("parent"));
    }

    @Test
    public void testLookupAncestor() {
        assertEquals(4, Axis.lookup("ancestor"));
    }

    @Test
    public void testLookupFollowingSibling() {
        assertEquals(5, Axis.lookup("following-sibling"));
    }

    @Test
    public void testLookupPrecedingSibling() {
        assertEquals(6, Axis.lookup("preceding-sibling"));
    }

    @Test
    public void testLookupFollowing() {
        assertEquals(7, Axis.lookup("following"));
    }

    @Test
    public void testLookupPreceding() {
        assertEquals(8, Axis.lookup("preceding"));
    }

    @Test
    public void testLookupAttribute() {
        assertEquals(9, Axis.lookup("attribute"));
    }

    @Test
    public void testLookupNamespace() {
        assertEquals(10, Axis.lookup("namespace"));
    }

    @Test
    public void testLookupSelf() {
        assertEquals(11, Axis.lookup("self"));
    }

    @Test
    public void testLookupDescendantOrSelf() {
        assertEquals(12, Axis.lookup("descendant-or-self"));
    }

    @Test
    public void testLookupAncestorOrSelf() {
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }

    @Test
    public void testLookupUnknown() {
        assertEquals(0, Axis.lookup("unknown"));
    }
}