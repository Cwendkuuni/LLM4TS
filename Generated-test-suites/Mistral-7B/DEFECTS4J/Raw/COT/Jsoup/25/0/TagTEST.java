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
    public void testConstructor() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    /**
     * Test method for {@link Tag#getName()}.
     */
    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String)}.
     */
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        Tag unknownTag = Tag.valueOf("unknown");
        assertNotEquals("p", unknownTag.getName());
        assertEquals("unknown", unknownTag.getName());
    }

    /**
     * Test method for {@link Tag#isBlock()}.
     */
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()}.
     */
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#canContainBlock()}.
     */
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.canContainBlock());

        Tag blockQuoteTag = new Tag("blockquote");
        assertTrue(blockQuoteTag.canContainBlock());
    }

    /**
     * Test method for {@link Tag#isInline()}.
     */
    @Test
    public void testIsInline() {
        Tag tag = new Tag("p");
        assertFalse(tag.isInline());

        Tag spanTag = new Tag("span");
        assertTrue(spanTag.isInline());
    }

    /**
     * Test method for {@link Tag#isData()}.
     */
    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        assertFalse(tag.isData());

        Tag scriptTag = new Tag("script");
        assertTrue(scriptTag.isData());
    }

    /**
     * Test method for {@link Tag#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());

        Tag imgTag = new Tag("img");
        assertFalse(imgTag.isEmpty());

        Tag metaTag = new Tag("meta");
        assertTrue(metaTag.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()}.
     */
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());

        Tag imgTag = new Tag("img");
        assertTrue(imgTag.isSelfClosing());

        Tag selfClosingTag = new Tag("input").setSelfClosing();
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
        Tag tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());

        Tag preTag = new Tag("pre");
        assertTrue(preTag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#equals(Object)}.
     */
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1, tag2);

        Tag tag3 = new Tag("span");
        assertNotEquals(tag1, tag3);

        Map<String, Tag> tags = new HashMap<>();
        Tag tag4 = new Tag("p");
        tags.put("p", tag4);
        assertEquals(tag4, tags.get("p"));
    }

    /**
     * Test method for {@link Tag#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("span");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    /**
     * Test method for {@link Tag#toString()}.
     */
    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    /**
     * Test method for {@link Tag#setSelfClosing()}.
     */
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("input");
        Tag selfClosingTag = tag.setSelfClosing();
        assertTrue(selfClosingTag.isSelfClosing());
    }
}