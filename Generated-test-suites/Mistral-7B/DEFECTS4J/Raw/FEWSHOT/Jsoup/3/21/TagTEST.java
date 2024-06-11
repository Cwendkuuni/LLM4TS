package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag frameSet = Tag.valueOf("FRAMESET");

        assertEquals("html", html.tagName);
        assertEquals("HEAD", head.tagName);
        assertEquals("BODY", body.tagName);
        assertEquals("FRAMESET", frameSet.tagName);
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("HTML");
        Tag p = Tag.valueOf("P");
        Tag img = Tag.valueOf("IMG");

        assertTrue(html.isBlock());
        assertTrue(p.isBlock());
        assertFalse(img.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag img = Tag.valueOf("IMG");

        assertTrue(html.canContainBlock());
        assertTrue(body.canContainBlock());
        assertFalse(p.canContainBlock());
        assertFalse(img.canContainBlock());
    }

    @Test
    public void testIsData() {
        Tag script = Tag.valueOf("SCRIPT");
        Tag textarea = Tag.valueOf("TEXTAREA");

        assertTrue(script.isData());
        assertTrue(textarea.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("IMG");
        Tag br = Tag.valueOf("BR");
        Tag input = Tag.valueOf("INPUT");

        assertFalse(img.isEmpty());
        assertFalse(br.isEmpty());
        assertTrue(input.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("PRE");

        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testCanContain() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag span = Tag.valueOf("SPAN");
        Tag script = Tag.valueOf("SCRIPT");

        assertTrue(p.canContain(div));
        assertTrue(p.canContain(span));
        assertFalse(p.canContain(script));
    }

    @Test
    public void testIsValidParent() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag div2 = Tag.valueOf("DIV");

        assertTrue(p.isValidParent(div));
        assertFalse(p.isValidParent(div2));
    }

    @Test
    public void testEqualsAndHashcode() {
        Tag p1 = Tag.valueOf("P");
        Tag p2 = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, div);
    }
}