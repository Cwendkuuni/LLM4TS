package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag html = Tag.valueOf("html");
        assertTrue(html.isKnownTag());

        Tag unknown = new Tag("unknown");
        assertFalse(unknown.isKnownTag());
    }

    @Test
    public void testIsKnownTag_givenTagName() {
        assertTrue(Tag.isKnownTag("html"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testBlockTags() {
        assertTrue(Tag.isBlock("html"));
        assertTrue(Tag.isBlock("p"));
        assertTrue(Tag.isBlock("div"));
        // Add more assertions for other block tags
    }

    @Test
    public void testInlineTags() {
        assertFalse(Tag.isBlock("a"));
        assertFalse(Tag.isBlock("span"));
        // Add more assertions for other inline tags
    }

    @Test
    public void testEmptyTags() {
        assertTrue(Tag.isEmpty("img"));
        assertTrue(Tag.isEmpty("input"));
        // Add more assertions for other empty tags
    }

    @Test
    public void testFormatAsBlockTags() {
        assertTrue(Tag.formatAsBlock("p"));
        assertTrue(Tag.formatAsBlock("pre"));
        // Add more assertions for other formatAsBlock tags
    }

    @Test
    public void testFormatAsInlineTags() {
        assertFalse(Tag.formatAsBlock("p"));
        assertFalse(Tag.formatAsBlock("a"));
        // Add more assertions for other formatAsInline tags
    }

    @Test
    public void testSelfClosingTags() {
        assertTrue(Tag.isSelfClosing("img"));
        assertTrue(Tag.isSelfClosing("input"));
        // Add more assertions for other self-closing tags
    }

    @Test
    public void testPreserveWhitespaceTags() {
        assertTrue(Tag.preserveWhitespace("pre"));
        assertTrue(Tag.preserveWhitespace("title"));
        // Add more assertions for other preserveWhitespace tags
    }

    @Test
    public void testNewTag() {
        Tag newTag = new Tag("newTag");
        assertEquals("newtag", newTag.getName());
        assertFalse(newTag.isBlock());
        assertFalse(newTag.formatAsBlock());
        assertFalse(newTag.canContainBlock());
        assertFalse(newTag.canContainInline());
        assertFalse(newTag.empty());
        assertFalse(newTag.selfClosing());
        assertFalse(newTag.preserveWhitespace());
    }

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        assertEquals("html", html.getName());
        assertTrue(html.isBlock());
        // Add more assertions for other properties

        Tag newTag = Tag.valueOf("newTag");
        assertEquals("newtag", newTag.getName());
        assertFalse(newTag.isBlock());
        // Add more assertions for other properties
    }
}