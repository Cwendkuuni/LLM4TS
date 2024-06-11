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
 * Test cases for Tag class.
 */
public class TagTest {

    /**
     * Test method for {@link Tag#Tag(String)}
     */
    @Test
    public void testTagConstructor() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String)} and {@link Tag#isKnownTag(String)}
     */
    @Test
    public void testValueOfAndIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(Tag.isKnownTag("p"));

        tag = new Tag("unknown");
        assertEquals("unknown", tag.getName());
        assertFalse(Tag.isKnownTag("unknown"));
    }

    /**
     * Test method for {@link Tag#isBlock()}, {@link Tag#formatAsBlock()}, {@link Tag#canContainBlock()}, {@link Tag#isData()},
     * {@link Tag#isEmpty()}, {@link Tag#isSelfClosing()}, and {@link Tag#isKnownTag()}
     */
    @Test
    public void testBasicProperties() {
        Tag tag = Tag.valueOf("p");

        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(Tag.isKnownTag("p"));

        tag = Tag.valueOf("img");

        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.isData());
        assertTrue(tag.isEmpty());
        assertTrue(tag.isSelfClosing());
        assertTrue(Tag.isKnownTag("img"));
    }

    /**
     * Test method for {@link Tag#toString()}
     */
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }

    /**
     * Test method for {@link Tag#equals(Object)} and {@link Tag#hashCode()}
     */
    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = new Tag("div");

        assertTrue(tag1.equals(tag2));
        assertTrue(tag1.hashCode() == tag2.hashCode());
        assertFalse(tag1.equals(tag3));
        assertFalse(tag1.hashCode() == tag3.hashCode());
    }

    /**
     * Test method for {@link Tag#isBlock()} with null tagName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsBlockWithNullTagName() {
        new Tag(null);
    }

    /**
     * Test method for {@link Tag#isBlock()} with empty tagName
     */
    @Test
    public void testIsBlockWithEmptyTagName() {
        Tag tag = new Tag("");
        assertFalse(tag.isBlock());
    }

    /**
     * Test method for {@link Tag#isBlock()} with uppercase tagName
     */
    @Test
    public void testIsBlockWithUppercaseTagName() {
        Tag tag = new Tag("DIV");
        assertTrue(tag.isBlock());
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with null tagName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithNullTagName() {
        Tag.valueOf(null);
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with empty tagName
     */
    @Test
    public void testValueOfWithEmptyTagName() {
        Tag tag = Tag.valueOf("");
        assertEquals("", tag.getName());
        assertFalse(Tag.isKnownTag(""));
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with invalid tagName
     */
    @Test
    public void testValueOfWithInvalidTagName() {
        Tag tag = Tag.valueOf("&tag");
        assertEquals("tag", tag.getName());
        assertTrue(tag.isData());
    }

    /**
     * Test method for {@link Tag#getValue(String)} with null tagName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithNullTagNameInMap() {
        synchronized (Tag.tags) {
            Tag tag = Tag.tags.get(null);
            assertNull(tag);
        }
    }

    /**
     * Test method for {@link Tag#getValue(String)} with empty tagName
     */
    @Test
    public void testValueOfWithEmptyTagNameInMap() {
        synchronized (Tag.tags) {
            Tag tag = Tag.tags.get("");
            assertNull(tag);
        }
    }

    /**
     * Test method for {@link Tag#getValue(String)} with unknown tagName
     */
    @Test
    public void testValueOfWithUnknownTagNameInMap() {
        synchronized (Tag.tags) {
            Tag tag = Tag.tags.get("unknown");
            assertNotNull(tag);
            assertEquals("unknown", tag.getName());
            assertFalse(tag.isKnownTag());
        }
    }

    /**
     * Test method for {@link Tag#getValue(String)} with predefined tagName
     */
    @Test
    public void testValueOfWithPredefinedTagNameInMap() {
        synchronized (Tag.tags) {
            Tag tag = Tag.tags.get("p");
            assertNotNull(tag);
            assertEquals("p", tag.getName());
            assertTrue(tag.isKnownTag());
        }
    }
}