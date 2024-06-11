package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag1 = Tag.valueOf("p", settings);
        assertTrue(tag1.isBlock());

        Tag tag2 = Tag.valueOf("unknown", settings);
        assertFalse(tag2.isBlock());

        Tag tag3 = Tag.valueOf("unknown");
        assertFalse(tag3.isBlock());
    }

    @Test
    public void testValueOfCaseSensitive() {
        Tag tag1 = Tag.valueOf("P");
        assertTrue(tag1.isBlock());

        Tag tag2 = Tag.valueOf("unknown");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.isBlock());

        Tag inlineTag = Tag.valueOf("b");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.formatAsBlock());

        Tag inlineTag = Tag.valueOf("b");
        assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.canContainBlock());

        Tag inlineTag = Tag.valueOf("b");
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isInline());

        Tag inlineTag = Tag.valueOf("b");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag dataTag = Tag.valueOf("img");
        assertFalse(dataTag.isData());

        Tag tag = Tag.valueOf("b");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag nonEmptyTag = Tag.valueOf("div");
        assertFalse(nonEmptyTag.isEmpty());

        Tag emptyTag = Tag.valueOf("img");
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag nonSelfClosingTag = Tag.valueOf("div");
        assertFalse(nonSelfClosingTag.isSelfClosing());

        Tag selfClosingTag = Tag.valueOf("img");
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("unknown"));

        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag nonPreserveWhitespaceTag = Tag.valueOf("div");
        assertFalse(nonPreserveWhitespaceTag.preserveWhitespace());

        Tag preserveWhitespaceTag = Tag.valueOf("pre");
        assertTrue(preserveWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag nonFormListedTag = Tag.valueOf("div");
        assertFalse(nonFormListedTag.isFormListed());

        Tag formListedTag = Tag.valueOf("input");
        assertTrue(formListedTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag nonFormSubmittableTag = Tag.valueOf("div");
        assertFalse(nonFormSubmittableTag.isFormSubmittable());

        Tag formSubmittableTag = Tag.valueOf("input");
        assertTrue(formSubmittableTag.isFormSubmittable());
    }
}