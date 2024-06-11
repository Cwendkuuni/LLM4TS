package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test cases for Tag class.
 */
public class TagTest {

    /**
     * Test method for {@link Tag#Tag(String)}.
     */
    @Test
    public void testTagConstructor() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test method for {@link Tag#getName()}.
     */
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String)}.
     */
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());

        Tag unknownTag = Tag.valueOf("unknown");
        assertNotEquals("div", unknownTag.getName());
        assertTrue(unknownTag.isData());
    }

    /**
     * Test method for {@link Tag#isBlock()}.
     */
    @Test
    public void testIsBlock() {
        Tag divTag = new Tag("div");
        assertTrue(divTag.isBlock());

        Tag spanTag = new Tag("span");
        assertFalse(spanTag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()}.
     */
    @Test
    public void testFormatAsBlock() {
        Tag pTag = new Tag("p");
        assertTrue(pTag.formatAsBlock());

        Tag imgTag = new Tag("img");
        assertFalse(imgTag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#canContainBlock()}.
     */
    @Test
    public void testCanContainBlock() {
        Tag divTag = new Tag("div");
        assertTrue(divTag.canContainBlock());

        Tag imgTag = new Tag("img");
        assertFalse(imgTag.canContainBlock());
    }

    /**
     * Test method for {@link Tag#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("img");
        assertFalse(imgTag.isEmpty());

        Tag metaTag = new Tag("meta");
        assertFalse(metaTag.isEmpty());

        Tag brTag = new Tag("br");
        assertTrue(brTag.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()}.
     */
    @Test
    public void testIsSelfClosing() {
        Tag imgTag = new Tag("img");
        assertTrue(imgTag.isSelfClosing());

        Tag divTag = new Tag("div");
        assertFalse(divTag.isSelfClosing());
    }

    /**
     * Test method for {@link Tag#isKnownTag()}.
     */
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    /**
     * Test method for {@link Tag#preserveWhitespace()}.
     */
    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag divTag = new Tag("div");
        assertFalse(divTag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#equals(Object)}.
     */
    @Test
    public void testEquals() {
        Tag div1 = new Tag("div");
        Tag div2 = new Tag("div");
        Tag span = new Tag("span");

        assertTrue(div1.equals(div1));
        assertTrue(div1.equals(div2));

        assertFalse(div1.equals(span));
    }

    /**
     * Test method for {@link Tag#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Tag div1 = new Tag("div");
        Tag div2 = new Tag("div");
        Tag span = new Tag("span");

        int hashCode1 = div1.hashCode();
        int hashCode2 = div2.hashCode();

        assertEquals(hashCode1, hashCode2);

        // different tags should have different hash codes
        int hashCode3 = span.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Test method for {@link Tag#toString()}.
     */
    @Test
    public void testToString() {
        Tag divTag = new Tag("div");
        assertEquals("div", divTag.toString());
    }

    /**
     * Test edge cases and exception handling.
     */
    @Test(expected = NullPointerException.class)
    public void testNullName() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        try {
            new Tag("");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceName() {
        try {
            new Tag("   ");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagName() {
        try {
            new Tag("InvalidTagName");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetBlockTags() {
        // The Tag class already has static initializers that populate the tags map.
        // We can just check that the map contains the expected block tags.

        Map<String, Tag> tags = Tag.tags;

        for (String tagName : Tag.blockTags) {
            Tag tag = tags.get(tagName);
            assertTrue(tag.canContainBlock());
        }
    }

    @Test
    public void testGetInlineTags() {
        // The Tag class already has static initializers that populate the tags map.
        // We can just check that the map contains the expected inline tags.

        Map<String, Tag> tags = Tag.tags;

        for (String tagName : Tag.inlineTags) {
            Tag tag = tags.get(tagName);
            assertFalse(tag.canContainBlock());
        }
    }

    @Test
    public void testGetEmptyTags() {
        // The Tag class already has static initializers that populate the tags map.
        // We can just check that the map contains the expected empty tags.

        Map<String, Tag> tags = Tag.tags;

        for (String tagName : Tag.emptyTags) {
            Tag tag = tags.get(tagName);
            assertTrue(tag.isEmpty());
        }
    }
}