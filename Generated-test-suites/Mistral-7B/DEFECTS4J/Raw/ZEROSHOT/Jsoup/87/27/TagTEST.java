package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Tag.java
 */
public class TagTest {

    /**
     * Test method for {@link Tag#Tag(String)}
     */
    @Test
    public void testTagConstruction() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isBlock()}
     */
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());

        tag = new Tag("span");
        assertFalse(tag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()}
     */
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());

        tag = new Tag("span");
        assertFalse(tag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#canContainInline()}
     */
    @Test
    public void testCanContainInline() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainInline());

        tag = new Tag("script");
        assertFalse(tag.canContainInline());
    }

    /**
     * Test method for {@link Tag#isEmpty()}
     */
    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("img");
        assertFalse(imgTag.isEmpty());

        Tag metaTag = new Tag("meta");
        assertFalse(metaTag.isEmpty());

        Tag imgTagEmpty = new Tag("img").setEmpty();
        assertTrue(imgTagEmpty.isEmpty());

        Tag metaTagEmpty = new Tag("meta").setEmpty();
        assertTrue(metaTagEmpty.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()}
     */
    @Test
    public void testIsSelfClosing() {
        Tag imgTag = new Tag("img");
        assertFalse(imgTag.isSelfClosing());

        Tag imgTagSelfClosing = new Tag("img").setSelfClosing();
        assertTrue(imgTagSelfClosing.isSelfClosing());
    }

    /**
     * Test method for {@link Tag#preserveWhitespace()}
     */
    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = new Tag("p");
        assertFalse(pTag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#isFormListed()}
     */
    @Test
    public void testIsFormListed() {
        Tag inputTag = new Tag("input");
        assertTrue(inputTag.isFormListed());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isFormListed());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()}
     */
    @Test
    public void testIsFormSubmittable() {
        Tag inputTypeSubmitTag = new Tag("input").setType("submit");
        assertTrue(inputTypeSubmitTag.isFormSubmittable());

        Tag inputTypeTextTag = new Tag("input").setType("text");
        assertFalse(inputTypeTextTag.isFormSubmittable());

        Tag textareaTag = new Tag("textarea");
        assertTrue(textareaTag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#valueOf(String)}
     */
    @Test
    public void testValueOf() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertTrue(pTag.isBlock());
        assertTrue(pTag.formatAsBlock());
        assertTrue(pTag.canContainInline());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());
        assertFalse(pTag.isFormListed());
        assertFalse(pTag.isFormSubmittable());

        Tag imgTag = Tag.valueOf("img");
        assertEquals("img", imgTag.getName());
        assertFalse(imgTag.isBlock());
        assertFalse(imgTag.formatAsBlock());
        assertFalse(imgTag.canContainInline());
        assertFalse(imgTag.isEmpty());
        assertFalse(imgTag.isSelfClosing());
        assertFalse(imgTag.preserveWhitespace());
        assertFalse(imgTag.isFormListed());
        assertFalse(imgTag.isFormSubmittable());

        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        assertTrue(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.preserveWhitespace());
        assertFalse(unknownTag.isFormListed());
        assertFalse(unknownTag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#valueOf(String, ParseSettings)}
     */
    @Test
    public void testValueOfWithParseSettings() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertTrue(pTag.isBlock());
        assertTrue(pTag.formatAsBlock());
        assertTrue(pTag.canContainInline());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());
        assertFalse(pTag.isFormListed());
        assertFalse(pTag.isFormSubmittable());

        ParseSettings parseSettings = new ParseSettings();
        Tag pTagLowercase = Tag.valueOf("p", parseSettings);
        assertEquals("p", pTagLowercase.getName());
        assertTrue(pTagLowercase.isBlock());
        assertTrue(pTagLowercase.formatAsBlock());
        assertTrue(pTagLowercase.canContainInline());
        assertFalse(pTagLowercase.isEmpty());
        assertFalse(pTagLowercase.isSelfClosing());
        assertFalse(pTagLowercase.preserveWhitespace());
        assertFalse(pTagLowercase.isFormListed());
        assertFalse(pTagLowercase.isFormSubmittable());

        Tag unknownTag = Tag.valueOf("unknown", parseSettings);
        assertEquals("unknown", unknownTag.getName());
        assertTrue(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.preserveWhitespace());
        assertFalse(unknownTag.isFormListed());
        assertFalse(unknownTag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isKnownTag(String)}
     */
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("span"));
        assertFalse(Tag.isKnownTag("unknown"));
    }
}