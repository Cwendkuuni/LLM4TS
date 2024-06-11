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

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("FRAMESET", frameSet.getName());
    }

    @Test
    public void testCanContain() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag span = Tag.valueOf("SPAN");
        Tag br = Tag.valueOf("BR");
        Tag input = Tag.valueOf("INPUT");

        assertTrue(p.canContain(div));
        assertTrue(p.canContain(span));
        assertFalse(p.canContain(p));
        assertFalse(p.canContain(br));
        assertFalse(p.canContain(input));

        assertTrue(div.canContain(p));
        assertTrue(div.canContain(span));
        assertTrue(div.canContain(div));
        assertFalse(div.canContain(br));
        assertFalse(div.canContain(input));

        assertTrue(span.canContain(p));
        assertTrue(span.canContain(span));
        assertFalse(span.canContain(br));
        assertFalse(span.canContain(input));

        assertTrue(br.isEmpty());

        assertTrue(input.isEmpty());
    }

    @Test
    public void testIsBlock() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag span = Tag.valueOf("SPAN");
        Tag br = Tag.valueOf("BR");
        Tag input = Tag.valueOf("INPUT");

        assertTrue(p.isBlock());
        assertTrue(div.isBlock());
        assertFalse(span.isBlock());
        assertFalse(br.isBlock());
        assertFalse(input.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag span = Tag.valueOf("SPAN");
        Tag br = Tag.valueOf("BR");
        Tag input = Tag.valueOf("INPUT");

        assertFalse(p.isInline());
        assertFalse(div.isInline());
        assertTrue(span.isInline());
        assertTrue(br.isInline());
        assertFalse(input.isInline());
    }

    @Test
    public void testIsData() {
        Tag script = Tag.valueOf("SCRIPT");
        Tag title = Tag.valueOf("TITLE");
        Tag link = Tag.valueOf("LINK");

        assertTrue(script.isData());
        assertTrue(title.isData());
        assertFalse(link.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("IMG");
        Tag br = Tag.valueOf("BR");
        Tag input = Tag.valueOf("INPUT");

        assertTrue(img.isEmpty());
        assertTrue(br.isEmpty());
        assertFalse(input.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("PRE");

        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertNull(p.getImplicitParent());
        assertEquals("DIV", div.getImplicitParent().getName());
    }

    @Test
    public void testIsValidParent() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");
        Tag p2 = Tag.valueOf("P");

        assertTrue(p.isValidParent(div));
        assertFalse(p.isValidParent(p2));
    }

    @Test
    public void testEquals() {
        Tag p1 = Tag.valueOf("P");
        Tag p2 = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertEquals(p1, p2);
        assertNotEquals(p1, div);
    }

    @Test
    public void testHashCode() {
        Tag p1 = Tag.valueOf("P");
        Tag p2 = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        int hashCodeP1 = p1.hashCode();
        int hashCodeP2 = p2.hashCode();
        int hashCodeDiv = div.hashCode();

        assertEquals(hashCodeP1, hashCodeP2);
        assertNotEquals(hashCodeP1, hashCodeDiv);
    }

    @Test
    public void testToString() {
        Tag p = Tag.valueOf("P");

        assertEquals("P", p.toString());
    }
}