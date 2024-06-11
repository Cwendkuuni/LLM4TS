package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagTest {

    @Test
    public void testCreateAndGetTag() {
        Tag html = Tag.valueOf("HTML");
        assertEquals("html", html.getTagName());
        assertEquals(Tag.TagType.BLOCK, html.isBlock());
        Tag p = Tag.valueOf("P");
        assertEquals("p", p.getTagName());
        assertEquals(Tag.TagType.BLOCK, p.isBlock());
        assertEquals(Tag.TagType.INLINE, p.isInline());
        assertEquals(true, p.canContainBlock());
        assertEquals(true, p.canContainInline());
        assertFalse(p.isEmpty());
    }

    @Test
    public void testGetParentTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);

        Tag p = new Tag("P");
        p.setAncestor(body);

        assertEquals(html, p.getImplicitParent());
    }

    @Test
    public void testCanContainTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);

        Tag p = new Tag("P");
        Tag div = new Tag("DIV");

        assertTrue(body.canContain(p));
        assertTrue(body.canContain(div));
        assertTrue(p.canContain(div));

        assertFalse(html.canContain(p));
        assertFalse(html.canContain(div));
        assertFalse(p.canContain(html));
        assertFalse(div.canContain(html));

        Tag script = new Tag("SCRIPT");
        assertFalse(body.canContain(script));
        assertFalse(p.canContain(script));
        assertFalse(html.canContain(script));
        assertTrue(script.isValidParent(body));
    }

    @Test
    public void testIsDataTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);

        Tag p = new Tag("P");
        Tag script = new Tag("SCRIPT");
        Tag textarea = new Tag("TEXTAREA");

        assertFalse(p.isData());
        assertTrue(script.isData());
        assertTrue(textarea.isData());

        assertFalse(html.isData());
        assertFalse(body.isData());
        assertFalse(p.canContain(script));
        assertFalse(p.canContain(textarea));
        assertFalse(script.canContain(p));
        assertFalse(textarea.canContain(p));
    }

    @Test
    public void testIsEmptyTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);

        Tag img = new Tag("IMG");
        Tag br = new Tag("BR");
        Tag input = new Tag("INPUT");

        assertFalse(img.isEmpty());
        assertFalse(br.isEmpty());
        assertTrue(input.isEmpty());

        assertFalse(html.isEmpty());
        assertFalse(body.isEmpty());
        assertFalse(img.canContain(br));
        assertFalse(img.canContain(input));
        assertFalse(br.canContain(img));
        assertFalse(br.canContain(input));
        assertFalse(input.canContain(img));
        assertFalse(input.canContain(br));
    }

    @Test
    public void testPreserveWhitespaceTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);

        Tag pre = new Tag("PRE").setPreserveWhitespace();

        assertTrue(pre.preserveWhitespace());
        assertFalse(html.preserveWhitespace());
        assertFalse(body.preserveWhitespace());

        assertFalse(html.canContain(pre));
        assertFalse(body.canContain(pre));
        assertTrue(pre.canContain(html));
        assertTrue(pre.canContain(body));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag html = Tag.valueOf("HTML");
        Tag body =
            new Tag("BODY");
        body.setAncestor(html);
        Tag p = new Tag("P");
        Tag p2 = new Tag("P");
        Tag div = new Tag("DIV");

        assertEquals(html, html);
        assertEquals(html, html.toString());
        assertEquals(html, html.getValue());
        assertEquals(html.hashCode(), html.hashCode());

        assertEquals(p, p);
        assertEquals(p, p.toString());
        assertEquals(p, p.getValue());
        assertEquals(p.hashCode(), p.hashCode());

        assertEquals(p, p2);
        assertEquals(p, p2.toString());
        assertEquals(p, p2.getValue());
        assertEquals(p.hashCode(), p2.hashCode());

        assertEquals(p, p);
        assertEquals(p, p.toString());
        assertEquals(p, p.getValue());
        assertEquals(p.hashCode(), p.hashCode());

        assertNotEquals(html, body);
        assertNotEquals(html, p);
        assertNotEquals(html, div);
        assertNotEquals(body, p);
        assertNotEquals(body, div);
        assertNotEquals(p, div);

        assertFalse(html.equals("abc"));
        assertFalse(html.equals(new Object()));
        assertFalse(body.equals("abc"));
        assertFalse(body.equals(new Object()));
        assertFalse(p.equals("abc"));
        assertFalse(p.equals(new Object()));
    }

    @Test
    public void testToString() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");

        assertEquals("html", html.toString());
        assertEquals("body", body.toString());
        assertEquals("p", p.toString());
        assertEquals("div", div.toString());
    }

    @Test
    public void testCreateBlocks() {
        Tag html = Tag.valueOf("HTML");
        Tag head = new Tag("HEAD");
        head.setAncestor(html);
        Tag body = new Tag("BODY");
        body.setAncestor(html);
        Tag form = new Tag("FORM");
        form.setAncestor(body);
        Tag input = new Tag("INPUT");
        input.setAncestor(form);
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        List<Tag> expectedAncestors = Arrays.asList(html, body);

        assertEquals("html", html.getTagName());
        assertEquals(Tag.TagType.BLOCK, html.isBlock());
        assertEquals(expectedAncestors, html.getAncestors());

        assertEquals("head", head.getTagName());
        assertEquals(Tag.TagType.BLOCK, head.isBlock());
        assertEquals(expectedAncestors, head.getAncestors());

        assertEquals("body", body.getTagName());
        assertEquals(Tag.TagType.BLOCK, body.isBlock());
        assertEquals(expectedAncestors, body.getAncestors());

        assertEquals("form", form.getTagName());
        assertEquals(Tag.TagType.BLOCK, form.isBlock());
        assertEquals(expectedAncestors, form.getAncestors());

        assertEquals("input", input.getTagName());
        assertEquals(Tag.TagType.INLINE, input.isBlock());
        assertEquals(expectedAncestors, input.getAncestors());

        assertEquals("p", p.getTagName());
        assertEquals(Tag.TagType.BLOCK, p.isBlock());
        assertEquals(expectedAncestors, p.getAncestors());

        assertEquals("div", div.getTagName());
        assertEquals(Tag.TagType.BLOCK, div.isBlock());
        assertEquals(expectedAncestors, div.getAncestors());

        assertFalse(html.canContain(p));
        assertFalse(html.canContain(div));
        assertFalse(head.canContain(p));
        assertFalse(head.canContain(div));
        assertFalse(body.canContain(p));
        assertFalse(body.canContain(div));
        assertTrue(p.canContain(html));
        assertTrue(p.canContain(head));
        assertTrue(p.canContain(body));
        assertTrue(div.canContain(html));
        assertTrue(div.canContain(head));
        assertTrue(div.canContain(body));
    }

    @Test
    public void testCreateInlines() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);
        Tag p = new Tag("P");
        Tag a = new Tag("A");
        List<Tag> expectedAncestors = Arrays.asList(html, body);

        assertEquals("html", html.getTagName());
        assertEquals(Tag.TagType.BLOCK, html.isBlock());
        assertEquals(expectedAncestors, html.getAncestors());

        assertEquals("body", body.getTagName());
        assertEquals(Tag.TagType.BLOCK, body.isBlock());
        assertEquals(expectedAncestors, body.getAncestors());

        assertEquals("p", p.getTagName());
        assertEquals(Tag.TagType.BLOCK, p.isBlock());
        assertEquals(expectedAncestors, p.getAncestors());

        assertEquals("a", a.getTagName());
        assertEquals(Tag.TagType.INLINE, a.isBlock());
        assertEquals(expectedAncestors, a.getAncestors());

        assertFalse(html.canContain(a));
        assertFalse(body.canContain(a));
        assertFalse(p.canContain(a));
        assertTrue(a.canContain(html));
        assertTrue(a.canContain(body));
        assertTrue(a.canContain(p));
    }

    @Test
    public void testGetAncestors() {
        Tag html = Tag.valueOf("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        body.setAncestor(html);
        Tag form = new Tag("FORM");
        form.setAncestor(body);
        Tag input = new Tag("INPUT");
        input.setAncestor(form);
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        List<Tag> expectedAncestors = Arrays.asList(html, body);

        assertEquals(expectedAncestors, html.getAncestors());
        assertEquals(expectedAncestors, head.getAncestors());
        assertEquals(expectedAncestors, body.getAncestors());
        assertEquals(expectedAncestors, form.getAncestors());
        assertEquals(expectedAncestors, input.getAncestors());
        assertEquals(expectedAncestors, p.getAncestors());
        assertEquals(expectedAncestors, div.getAncestors());

        assertEquals(1, html.getAncestors().size());
        assertEquals(2, body.getAncestors().size());
    }

    @Test
    public void testCreateEmptyTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY").setEmpty();
        body.setAncestor(html);
        Tag img = new Tag("IMG").setEmpty();
        List<Tag> expectedAncestors = Arrays.asList(html, body);

        assertEquals("html", html.getTagName());
        assertEquals(Tag.TagType.BLOCK, html.isBlock());
        assertEquals(expectedAncestors, html.getAncestors());

        assertEquals("body", body.getTagName());
        assertEquals(Tag.TagType.BLOCK, body.isBlock());
        assertEquals(expectedAncestors, body.getAncestors());
        assertEquals(false, body.canContainBlock());
        assertEquals(false, body.canContainInline());
        assertEquals(true, body.isEmpty());

        assertEquals("img", img.getTagName());
        assertEquals(Tag.TagType.INLINE, img.isBlock());
        assertEquals(expectedAncestors, img.getAncestors());
        assertEquals(false, img.canContainBlock());
        assertEquals(false, img.canContainInline());
        assertEquals(true, img.isEmpty());
    }

    @Test
    public void testCreateOptionalClosingTag() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);
        Tag p = new Tag("P");
        Tag q = new Tag("Q");
        q.setOptionalClosing();
        List<Tag> expectedAncestors = Arrays.asList(html, body);

        assertEquals("html", html.getTagName());
        assertEquals(Tag.TagType.BLOCK, html.isBlock());
        assertEquals(expectedAncestors, html.getAncestors());

        assertEquals("body", body.getTagName());
        assertEquals(Tag.TagType.BLOCK, body.isBlock());
        assertEquals(expectedAncestors, body.getAncestors());

        assertEquals("p", p.getTagName());
        assertEquals(Tag.TagType.BLOCK, p.isBlock());
        assertEquals(expectedAncestors, p.getAncestors());

        assertEquals("q", q.getTagName());
        assertEquals(Tag.TagType.INLINE, q.isBlock());
        assertEquals(expectedAncestors, q.getAncestors());
        assertEquals(true, q.isOptionalClosing());

        assertFalse(html.canContain(p));
        assertFalse(html.canContain(q));
        assertFalse(body.canContain(p));
        assertFalse(body.canContain(q));
        assertTrue(p.canContain(html));
        assertTrue(p.canContain(body));
        assertTrue(q.canContain(html));
        assertTrue(q.canContain(body));
        assertTrue(p.canContain(q));
        assertTrue(q.canContain(p));
    }

    @Test
    public void testCreateDataOnlyTags() {
        Tag html = Tag.valueOf("HTML");
        Tag body = new Tag("BODY");
        body.setAncestor(html);
        Tag script = new Tag("SCRIPT").setContainDataOnly();
        List<Tag> expectedAncestors = Arrays.asList(html, body);

        assertEquals("html", html.getTagName());
        assertEquals(Tag.TagType.BLOCK, html.isBlock());
        assertEquals(expectedAncestors, html.getAncestors());

        assertEquals("body", body.getTagName());
        assertEquals(Tag.TagType.BLOCK, body.isBlock());
        assertEquals(expectedAncestors, body.getAncestors());

        assertEquals("script", script.getTagName());
        assertEquals(Tag.TagType.INLINE, script.isBlock());
        assertEquals(expectedAncestors, script.getAncestors());
        assertEquals(true, script.canContainDataOnly());
        assertEquals(false, script.canContainBlock());
        assertEquals(false, script.canContainInline());
        assertEquals(true, script.preserveWhitespace());

        assertFalse(html.canContain(script));
        assertFalse(body.canContain(script));
        assertFalse(script.canContain(html));
        assertFalse(script.canContain(body));
        assertTrue(html.isValidParent(script));
        assertTrue(body.isValidParent(script));
        assertTrue(script.isValidParent(html));
        assertTrue(script.isValidParent(body));
    }
}