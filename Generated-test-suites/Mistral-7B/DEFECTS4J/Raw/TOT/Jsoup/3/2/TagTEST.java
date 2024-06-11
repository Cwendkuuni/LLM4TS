package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class TagTest {

    @Test
    public void testValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("unknown");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertNotSame(pTag, divTag);
        assertTrue(pTag.equals(pTag));
        assertTrue(pTag.equals(Tag.valueOf("P")));
        assertFalse(pTag.equals(unknownTag));
    }

    @Test
    public void testCanContain() {
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag h1 = Tag.valueOf("H1");
        Tag span = Tag.valueOf("SPAN");
        Tag head = Tag.valueOf("HEAD");
        Tag script = Tag.valueOf("SCRIPT");
        Tag base = Tag.valueOf("BASE");

        assertTrue(body.canContain(p));
        assertTrue(body.canContain(div));
        assertTrue(body.canContain(h1));
        assertFalse(body.canContain(head));
        assertFalse(body.canContain(script));
        assertFalse(body.canContain(base));

        assertTrue(p.canContain(span));
        assertTrue(h1.canContain(p));
        assertFalse(h1.canContain(script));
        assertFalse(head.canContain(p));
        assertFalse(div.canContain(script));
        assertFalse(script.canContain(p));
    }

    @Test
    public void testIsData() {
        Tag script = Tag.valueOf("SCRIPT");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertFalse(p.isData());
        assertFalse(div.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testIsBlock() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag h1 = Tag.valueOf("H1");
        Tag span = Tag.valueOf("SPAN");

        assertTrue(p.isBlock());
        assertTrue(div.isBlock());
        assertFalse(h1.isBlock());
        assertFalse(span.isBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("IMG");
        Tag p = Tag.valueOf("P");
        Tag br = Tag.valueOf("BR");

        assertFalse(img.isEmpty());
        assertFalse(p.isEmpty());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("PRE");
        Tag p = Tag.valueOf("P");

        assertFalse(p.preserveWhitespace());
        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag body = Tag.valueOf("BODY");

        assertEquals(null, p.getImplicitParent());
        assertEquals(p, div.getImplicitParent());
        assertEquals(body, body.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag body = Tag.valueOf("BODY");
        Tag head = Tag.valueOf("HEAD");

        assertTrue(p.isValidParent(body));
        assertTrue(p.isValidParent(div));
        assertFalse(p.isValidParent(head));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNullName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyName() {
        Tag.valueOf("");
    }
}