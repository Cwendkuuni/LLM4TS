package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testCanContain() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag head = Tag.valueOf("HEAD");

        assertTrue(html.canContain(body));
        assertTrue(html.canContain(p));
        assertTrue(html.canContain(div));
        assertTrue(body.canContain(p));
        assertTrue(body.canContain(div));
        assertTrue(head.canContain(scriptTag()));
        assertTrue(head.canContain(titleTag()));
        assertFalse(p.canContain(p));
        assertFalse(p.canContain(head));
        assertFalse(div.canContain(head));
        assertFalse(div.canContain(head));
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertTrue(html.isBlock());
        assertTrue(body.isBlock());
        assertTrue(p.isInline());
        assertTrue(div.isBlock());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("SCRIPT");
        Tag titleTag = Tag.valueOf("TITLE");

        assertTrue(scriptTag.isData());
        assertFalse(titleTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("IMG");

        assertFalse(imgTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("PRE");

        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");
        Tag html2 = Tag.valueOf("HTML");

        assertEquals(html, html2);
        assertNotEquals(html, body);
    }

    @Test
    public void testHashCode() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");

        int htmlHashCode = html.hashCode();
        int bodyHashCode = body.hashCode();

        assertNotEquals(htmlHashCode, bodyHashCode);
    }

    @Test
    public void testToString() {
        Tag html = Tag.valueOf("HTML");
        Tag body = Tag.valueOf("BODY");

        assertEquals("HTML", html.toString());
        assertEquals("BODY", body.toString());
    }

    private Tag scriptTag() {
        return Tag.valueOf("SCRIPT");
    }

    private Tag titleTag() {
        return Tag.valueOf("TITLE");
    }
}