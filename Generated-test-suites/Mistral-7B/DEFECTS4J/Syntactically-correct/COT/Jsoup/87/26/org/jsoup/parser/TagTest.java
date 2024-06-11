package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Tag class.
 */
public class TagTest {

    /**
     * Test method for {@link Tag#Tag(String)} and {@link Tag#getName()}.
     */
    @Test
    public void testTagConstructorAndgetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String)} and {@link Tag#isKnownTag(String)}.
     */
    @Test
    public void testValueOfAndIsKnownTag() {
        Tag htmlTag = Tag.valueOf("html");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("unknown");

        assertTrue(Tag.isKnownTag("html"));
        assertTrue(htmlTag.isKnownTag());
        assertFalse(Tag.isKnownTag("unknown"));
        assertFalse(unknownTag.isKnownTag());
        assertEquals(htmlTag.tagName, "html");
        assertEquals(unknownTag.tagName, "unknown");
    }

    /**
     * Test method for {@link Tag#isBlock()}.
     */
    @Test
    public void testIsBlock() {
        Tag divTag = new Tag("div");
        Tag pTag = new Tag("p");
        assertTrue(divTag.isBlock());
        assertFalse(pTag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()}.
     */
    @Test
    public void testFormatAsBlock() {
        Tag divTag = new Tag("div");
        Tag pTag = new Tag("p");
        assertTrue(divTag.formatAsBlock());
        assertFalse(pTag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#isData()}.
     */
    @Test
    public void testIsData() {
        Tag divTag = new Tag("div");
        Tag scriptTag = new Tag("script");
        assertFalse(divTag.isData());
        assertTrue(scriptTag.isData());
    }

    /**
     * Test method for {@link Tag#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("img");
        Tag brTag = new Tag("br");
        assertFalse(imgTag.isEmpty());
        assertTrue(brTag.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()}.
     */
    @Test
    public void testIsSelfClosing() {
        Tag imgTag = new Tag("img");
        Tag brTag = new Tag("br");
        Tag inputTag = new Tag("input");
        Tag inputTypeImageTag = new Tag("input").setSelfClosing();

        assertFalse(imgTag.isSelfClosing());
        assertFalse(brTag.isSelfClosing());
        assertFalse(inputTag.isSelfClosing());
        assertTrue(inputTypeImageTag.isSelfClosing());
    }

    /**
     * Test method for {@link Tag#isKnownTag(String)}.
     */
    @Test
    public void testIsKnownTag_withEmptyTagName() {
        assertFalse(Tag.isKnownTag(""));
    }

    /**
     * Test method for {@link Tag#preserveWhitespace()}.
     */
    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("pre").setPreserveWhitespace(true);
        Tag textareaTag = new Tag("textarea").setPreserveWhitespace(true);

        assertTrue(preTag.preserveWhitespace());
        assertTrue(textareaTag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#isFormListed()}.
     */
    @Test
    public void testIsFormListed() {
        Tag inputTag = new Tag("input");
        Tag buttonTag = new Tag("button");
        assertTrue(inputTag.isFormListed());
        assertFalse(buttonTag.isFormListed());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()}.
     */
    @Test
    public void testIsFormSubmittable() {
        Tag inputTag = new Tag("input");
        Tag textareaTag = new Tag("textarea");
        Tag buttonTag = new Tag("button");
        assertTrue(inputTag.isFormSubmittable());
        assertTrue(textareaTag.isFormSubmittable());
        assertFalse(buttonTag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#equals(Object)}.
     */
    @Test
    public void testEquals() {
        Tag divTag1 = new Tag("div");
        Tag divTag2 = new Tag("div");
        Tag pTag = new Tag("p");

        assertTrue(divTag1.equals(divTag2));
        assertFalse(divTag1.equals(pTag));
    }

    /**
     * Test method for {@link Tag#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Map<Integer, Tag> tags = new HashMap<>();
        Tag divTag = new Tag("div");

        // Same tag should have the same hash code
        tags.put(divTag.hashCode(), divTag);
        assertEquals(divTag.hashCode(), divTag.hashCode());

        // Different tags should have different hash codes
        Tag pTag = new Tag("p");
        assertNotEquals(divTag.hashCode(), pTag.hashCode());
    }

    /**
     * Test method for {@link Tag#toString()}.
     */
    @Test
    public void testToString() {
        Tag divTag = new Tag("div");
        Tag pTag = new Tag("p");
        assertEquals("div", divTag.toString());
        assertEquals("p", pTag.toString());
    }

    /**
     * Test method for {@link Tag#valueOf(String, ParseSettings)} with null tagName.
     */
    @Test(expected = NullPointerException.class)
    public void testValueOfWithNullTagName() {
        Tag.valueOf(null);
    }

    /**
     * Test method for {@link Tag#valueOf(String, ParseSettings)} with empty tagName.
     */
    @Test
    public void testValueOfWithEmptyTagName() {
        Tag emptyTag = Tag.valueOf("");
        assertNull(emptyTag);
    }
}