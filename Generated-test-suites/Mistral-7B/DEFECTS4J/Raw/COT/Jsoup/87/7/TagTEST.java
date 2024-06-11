package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Tag.
 */
public class TagTest {

    /**
     * Test method for {@link Tag#Tag(String)}.
     */
    @Test
    public void testTagConstruction() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    /**
     * Test method for {@link Tag#getValueOf(String, ParseSettings)}.
     */
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        Tag unknownTag = Tag.valueOf("unknown");
        assertTrue(unknownTag.isUnknownTag());
        assertEquals("unknown", unknownTag.getName());
    }

    /**
     * Test method for {@link Tag#isBlock()}.
     */
    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()}.
     */
    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#isData()}.
     */
    @Test
    public void testIsData() {
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());

        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());
    }

    /**
     * Test method for {@link Tag#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());

        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.isEmpty());

        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()}.
     */
    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isSelfClosing());

        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.isSelfClosing());

        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isSelfClosing());

        Tag selfClosingTag = Tag.valueOf("img").setSelfClosing();
        assertTrue(selfClosingTag.isSelfClosing());
    }

    /**
     * Test method for {@link Tag#isKnownTag()}.
     */
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    /**
     * Test method for {@link Tag#preserveWhitespace()}.
     */
    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#isFormListed()}.
     */
    @Test
    public void testIsFormListed() {
        Tag buttonTag = Tag.valueOf("button");
        assertTrue(buttonTag.isFormListed());

        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isFormListed());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()}.
     */
    @Test
    public void testIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormSubmittable());

        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isFormSubmittable());
    }

    /**
     * Test edge cases and exception handling:
     * <ul>
     * <li>Null tag name</li>
     * <li>Empty tag name</li>
     * <li>Negative tag name</li>
     * <li>Unknown tag name with settings.normalizeTag = true</li>
     * <li>Unknown tag name with settings.normalizeTag = false</li>
     * </ul>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeCasesAndExceptionHandling() {
        Tag.valueOf(null);

        Tag.valueOf("");

        Tag.valueOf("-1");

        Tag.valueOf("unknown", null);

        Tag.valueOf("Unknown", new ParseSettings() {
            @Override
            public String normalizeTag(String tagName) {
                return tagName.toUpperCase();
            }
        });
    }
}