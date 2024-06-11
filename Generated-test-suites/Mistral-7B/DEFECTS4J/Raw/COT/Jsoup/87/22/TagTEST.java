package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.Tag.*;

public class TagTest {

    @Test
    public void testTagValueOfWithKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isEmpty());
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testTagValueOfWithUnknownTag() {
        ParseSettings settings = new ParseSettings();
        Tag tag = Tag.valueOf("mytag", settings);
        assertEquals("mytag", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.isEmpty());
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testTagValueOfWithLowerCaseKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isEmpty());
        assertFalse(tag.selfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());

        Tag tagLowerCase = Tag.valueOf("p", ParseSettings.preserveCase);
        assertEquals("p", tagLowerCase.getName());
        assertTrue(tagLowerCase.isBlock());
        assertTrue(tagLowerCase.formatAsBlock());
        assertFalse(tagLowerCase.isEmpty());
        assertFalse(tagLowerCase.selfClosing());
        assertFalse(tagLowerCase.preserveWhitespace());
        assertFalse(tagLowerCase.isFormListed());
        assertFalse(tagLowerCase.isFormSubmittable());
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("mytag"));
    }

    @Test
    public void testTagIsBlock() {
        assertTrue(Tag.valueOf("p").isBlock());
        assertFalse(Tag.valueOf("input").isBlock());
    }

    @Test
    public void testTagIsInline() {
        assertFalse(Tag.valueOf("p").isInline());
        assertTrue(Tag.valueOf("input").isInline());
    }

    @Test
    public void testTagIsData() {
        Tag input = Tag.valueOf("input");
        assertFalse(input.isData());

        Tag textarea = Tag.valueOf("textarea");
        assertTrue(textarea.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag img = Tag.valueOf("img");
        assertFalse(img.isEmpty());

        Tag imgEmpty = Tag.valueOf("img").setSelfClosing();
        assertTrue(imgEmpty.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        assertFalse(img.isSelfClosing());

        Tag imgEmpty = Tag.valueOf("img").setSelfClosing();
        assertTrue(imgEmpty.isSelfClosing());

        Tag selfClosingTag = Tag.valueOf("input");
        selfClosingTag.selfClosing = true;
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testTagIsKnownTagWithEmptyString() {
        assertFalse(Tag.isKnownTag(""));
    }

    @Test(expected = NullPointerException.class)
    public void testTagValueOfWithNullTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfWithEmptyTagName() {
        Tag.valueOf("");
    }

    @Test(expected = NullPointerException.class)
    public void testTagValueOfWithNullParseSettings() {
        Tag.valueOf("tag", null);
    }
}