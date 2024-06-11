package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag frameSet = new Tag("FRAMESET");

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("FRAMESET", frameSet.getName());

        Tag p = new Tag("P");
        Tag h1 = new Tag("H1");

        assertEquals("P", p.getName());
        assertEquals("H1", h1.getName());
    }

    @Test
    public void testDefaultAncestor() {
        Tag defaultAncestor = Tag.getDefaultAncestor();
        assertEquals("BODY", defaultAncestor.getName());
    }

    @Test
    public void testTagEquals() {
        Tag html1 = new Tag("HTML");
        Tag html2 = new Tag("HTML");
        Tag p = new Tag("P");
        Tag q = new Tag("Q");

        assertTrue(html1.equals(html2));
        assertTrue(html1.hashCode() == html2.hashCode());
        assertFalse(p.equals(q));
        assertFalse(p.hashCode() == q.hashCode());
    }

    @Test
    public void testTagCanContain() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag span = new Tag("SPAN");

        assertTrue(p.canContain(div));
        assertTrue(p.canContain(span));
        assertFalse(div.canContain(p));
        assertFalse(span.canContain(p));
    }

    @Test
    public void testTagIsValidParent() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");

        assertTrue(p.isValidParent(div));
        assertFalse(div.isValidParent(p));

        Tag head = new Tag("HEAD");
        assertTrue(p.isValidParent(head));
        assertFalse(head.isValidParent(p));
    }

    @Test
    public void testTagIsData() {
        Tag p = new Tag("P");
        Tag script = new Tag("SCRIPT");

        assertFalse(p.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag img = new Tag("IMG");
        Tag p = new Tag("P");

        assertFalse(img.isEmpty());
        assertFalse(p.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag pre = new Tag("PRE");

        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testTagToString() {
        Tag p = new Tag("P");

        assertEquals("P", p.toString());
    }

    @Test
    public void testTagCanContainBlockAndInline() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag span = new Tag("SPAN");

        assertTrue(p.canContain(div));
        assertTrue(p.canContain(span));
        assertTrue(div.canContain(span));
    }

    @Test
    public void testTagOptionalClosing() {
        Tag a = new Tag("A");

        assertTrue(a.isOptionalClosing());
    }

    @Test
    public void testTagAncestors() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");

        html.addAncestor(head);
        html.addAncestor(body);
        head.addAncestor(body);
        p.addAncestor(body);

        assertEquals(3, html.getNumAncestors());
        assertEquals(2, head.getNumAncestors());
        assertEquals(1, p.getNumAncestors());

        assertTrue(html.isValidParent(p));
        assertFalse(p.isValidParent(html));
    }

    @Test
    public void testTagValueOf() {
        Tag p = Tag.valueOf("P");

        assertEquals("P", p.getName());
    }

    @Test
    public void testTagValueOfUnknown() {
        Tag unknown = Tag.valueOf("UNKNOWN");

        assertEquals("UNKNOWN", unknown.getName());
        assertTrue(unknown.isData());
    }

    @Test
    public void testTagCreateBlock() {
        Tag html = Tag.createBlock("HTML");
        Tag head = Tag.createBlock("HEAD");
        Tag body = Tag.createBlock("BODY");
        Tag p = Tag.createBlock("P");

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("P", p.getName());
    }

    @Test
    public void testTagCreateInline() {
        Tag a = Tag.createInline("A");
        Tag span = Tag.createInline("SPAN");

        assertEquals("A", a.getName());
        assertEquals("SPAN", span.getName());
    }

    @Test
    public void testTagSetAncestor() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");

        html.setAncestor(head, body);
        head.setAncestor(body);
        p.setAncestor(body);

        assertTrue(html.isValidParent(p));
        assertFalse(p.isValidParent(html));
    }
}