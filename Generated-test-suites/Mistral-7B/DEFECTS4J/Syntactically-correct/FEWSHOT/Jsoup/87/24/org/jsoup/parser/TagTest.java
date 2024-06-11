package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testNewTag() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty());
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testEmptyTag() {
        Tag tag = new Tag("img");
        tag.empty = true;
        assertEquals("img", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertTrue(tag.empty);
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testBlockTag() {
        Tag tag = new Tag("p");
        tag.isBlock = true;
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty());
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testInlineTag() {
        Tag tag = new Tag("input");
        tag.isBlock = false;
        tag.formatAsBlock = false;
        assertEquals("input", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty());
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testDataTag() {
        Tag tag = new Tag("script");
        tag.canContainInline = false;
        tag.empty = false;
        assertEquals("script", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty);
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testSelfClosingTag() {
        Tag tag = new Tag("img");
        tag.selfClosing = true;
        assertEquals("img", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty);
        assertTrue(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testPreserveWhitespaceTag() {
        Tag tag = new Tag("pre");
        tag.preserveWhitespace = true;
        assertEquals("pre", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty);
        assertFalse(tag.selfClosing());
        assertTrue(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testFormListedTag() {
        Tag tag = new Tag("input");
        tag.formList = true;
        assertEquals("input", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty);
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertTrue(tag.formList());
        assertFalse(tag.formSubmit());
    }

    @Test
    public void testFormSubmittableTag() {
        Tag tag = new Tag("input");
        tag.formSubmit = true;
        assertEquals("input", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.empty);
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.formList());
        assertTrue(tag.formSubmit());
    }

    @Test
    public void testGetTagByName() {
        assertEquals(Tag.valueOf("p"), Tag.valueOf("p"));
        assertEquals(Tag.valueOf("p"), Tag.valueOf("P"));
        assertNotNull(Tag.valueOf("div"));
        assertEquals(Tag.valueOf("div"), Tag.valueOf("DIV"));
        assertNull(Tag.valueOf("foo"));
        assertNull(Tag.valueOf(null));
    }

    @Test
    public void testKnownTags() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("FOO"));
    }

    @Test
    public void testIsBlockTag() {
        assertTrue(Tag.valueOf("p").isBlock());
        assertFalse(Tag.valueOf("input").isBlock());
    }

    @Test
    public void testFormatAsBlockTag() {
        assertTrue(Tag.valueOf("p").formatAsBlock());
        assertFalse(Tag.valueOf("input").formatAsBlock());
    }

    @Test
    public void testCanContainBlockTag() {
        assertTrue(Tag.valueOf("div").canContainBlock());
        assertFalse(Tag.valueOf("input").canContainBlock());
    }

    @Test
    public void testIsDataTag() {
        assertTrue(Tag.valueOf("script").isData());
        assertFalse(Tag.valueOf("div").isData());
    }

    @Test
    public void testIsEmptyTag() {
        assertTrue(Tag.valueOf("img").isEmpty());
        assertFalse(Tag.valueOf("div").isEmpty());
    }

    @Test
    public void testIsSelfClosingTag() {
        assertTrue(Tag.valueOf("img").isSelfClosing());
        assertFalse(Tag.valueOf("div").isSelfClosing());
    }

    @Test
    public void testIsKnownTagTag() {
        assertTrue(Tag.valueOf("p").isKnownTag());
        assertTrue(Tag.valueOf("div").isKnownTag());
        assertFalse(Tag.valueOf("FOO").isKnownTag());
    }

    @Test
    public void testPreserveWhitespaceTag() {
        assertTrue(Tag.valueOf("pre").preserveWhitespace());
        assertFalse(Tag.valueOf("div").preserveWhitespace());
    }

    @Test
    public void testIsFormListedTag() {
        assertTrue(Tag.valueOf("input").isFormListed());
        assertFalse(Tag.valueOf("div").isFormListed());
    }

    @Test
    public void testIsFormSubmittableTag() {
        assertTrue(Tag.valueOf("input").isFormSubmittable());
        assertFalse(Tag.valueOf("div").isFormSubmittable());
    }
}