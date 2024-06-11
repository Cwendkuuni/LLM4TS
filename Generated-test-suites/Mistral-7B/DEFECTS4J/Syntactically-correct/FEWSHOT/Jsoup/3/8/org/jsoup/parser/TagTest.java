package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreationAndBasicProperties() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");

        assertEquals("html", html.tagName);
        assertEquals("HTML", html.toString());
        assertEquals(true, html.isBlock());
        assertEquals(true, html.canContainBlock());

        assertEquals("head", head.tagName);
        assertEquals("HEAD", head.toString());
        assertEquals(false, head.isBlock());
        assertEquals(true, head.canContainBlock());

        assertEquals("body", body.tagName);
        assertEquals("BODY", body.toString());
        assertEquals(true, body.isBlock());
        assertEquals(true, body.canContainBlock());

        assertEquals("p", p.tagName);
        assertEquals("P", p.toString());
        assertEquals(true, p.isBlock());
        assertEquals(true, p.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag img = Tag.valueOf("IMG");

        assertTrue(html.canContain(body));
        assertTrue(body.canContain(p));
        assertTrue(body.canContain(img));
        assertFalse(p.canContain(p));
        assertFalse(p.canContain(html));
        assertFalse(img.canContain(html));
    }

    @Test
    public void testIsData() {
        Tag p = Tag.valueOf("P");
        Tag script = Tag.valueOf("SCRIPT");
        Tag textarea = Tag.valueOf("TEXTAREA");

        assertFalse(p.isData());
        assertTrue(script.isData());
        assertTrue(textarea.isData());
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

        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag p = Tag.valueOf("P").setAncestor("BODY");
        assertEquals("body", p.getImplicitParent().tagName);
    }

    @Test
    public void testIsValidParent() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag body = Tag.valueOf("BODY");

        assertFalse(p.isValidParent(div));
        assertTrue(p.isValidParent(body));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag p1 = Tag.valueOf("P");
        Tag p2 = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertTrue(p1.equals(p2));
        assertTrue(p1.hashCode() == p2.hashCode());
        assertFalse(p1.equals(div));
    }
}