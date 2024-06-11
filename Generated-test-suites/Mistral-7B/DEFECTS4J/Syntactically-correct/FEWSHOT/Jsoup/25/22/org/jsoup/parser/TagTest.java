package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag unknown = Tag.valueOf("unkownTag");

        assertEquals("html", html.getName());
        assertEquals("p", p.getName());
        assertEquals("unkownTag", unknown.getName());
        assertEquals(html, Tag.isKnownTag("html"));
        assertEquals(p, Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unkownTag"));
    }

    @Test
    public void testIsBlockAndFormatAsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        assertTrue(html.isBlock());
        assertTrue(html.formatAsBlock());
        assertTrue(p.isBlock());
        assertTrue(p.formatAsBlock());
        assertFalse(img.isBlock());
        assertFalse(img.formatAsBlock());
    }

    @Test
    public void testCanContainBlockAndCanContainInline() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        assertTrue(html.canContainBlock());
        assertTrue(html.canContainInline());
        assertTrue(p.canContainBlock());
        assertTrue(p.canContainInline());
        assertFalse(img.canContainBlock());
        assertFalse(img.canContainInline());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");

        assertFalse(img.isEmpty());
        assertFalse(input.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");
        Tag selfClosingUnknown = new Tag("selfClosingUnknown").setSelfClosing();

        assertFalse(img.isSelfClosing());
        assertFalse(input.isSelfClosing());
        assertTrue(selfClosingUnknown.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag unknown = new Tag("unknownTag");

        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        Tag div = Tag.valueOf("div");

        assertFalse(pre.preserveWhitespace());
        assertFalse(div.preserveWhitespace());
    }

    @Test
    public void testPreserveWhitespaceForSpecifiedTags() {
        Tag pre = Tag.valueOf("pre");

        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testRegisterTag() {
        Tag newTag = new Tag("newTag");

        assertNull(Tag.isKnownTag("newTag"));
        Tag registeredTag = Tag.register(newTag);
        assertEquals("newTag", registeredTag.getName());
        assertTrue(Tag.isKnownTag("newTag"));
    }
}