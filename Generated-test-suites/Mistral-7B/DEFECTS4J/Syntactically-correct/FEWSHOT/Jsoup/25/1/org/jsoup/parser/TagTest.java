package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagRegistration() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertEquals("html", html.getName());
        assertEquals("html", html.toString());
        assertEquals("html", html.tagName);

        assertEquals("p", p.getName());
        assertEquals("p", p.toString());
        assertEquals("p", p.tagName);

        assertEquals("div", div.getName());
        assertEquals("div", div.toString());
        assertEquals("div", div.tagName);

        assertTrue(html.isKnownTag());
        assertTrue(p.isKnownTag());
        assertTrue(div.isKnownTag());

        assertSame(html, Tag.isKnownTag("html"));
        assertSame(p, Tag.isKnownTag("p"));
        assertSame(div, Tag.isKnownTag("div"));
    }

    @Test
    public void testTagEqualsHashcode() {
        Tag p = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertEquals(p, p);
        assertEquals(p, p2);
        assertEquals(p.hashCode(), p2.hashCode());

        assertNotEquals(p, div);
        assertNotEquals(p.hashCode(), div.hashCode());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.canContainBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.canContainInline());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testSetSelfClosing() {
        Tag p = Tag.valueOf("p");
        Tag selfClosingP = p.setSelfClosing();
        assertTrue(selfClosingP.isSelfClosing());
        assertFalse(p.isSelfClosing());
    }

    @Test
    public void testUnknownTag() {
        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertFalse(unknownTag.canContainBlock());
        assertFalse(unknownTag.canContainInline());
        assertFalse(unknownTag.isEmpty());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testIsDataTag() {
        Tag script = Tag.valueOf("script");
        assertTrue(script.isData());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag img = Tag.valueOf("img");
        assertTrue(img.isEmpty());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isEmpty());
    }

    @Test
    public void testIsInlineTag() {
        Tag p = Tag.valueOf("p");
        assertFalse(p.isInline());

        Tag span = Tag.valueOf("span");
        assertTrue(span.isInline());
    }

    @Test
    public void testIsKnownTagNull() {
        assertNull(Tag.isKnownTag(null));
    }
}