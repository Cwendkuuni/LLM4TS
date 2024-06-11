package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Tag.
 */
public class TagTest {

    @Test
    public void testValueOf() {
        // Test case for valid tag name
        Tag tag1 = Tag.valueOf("p");
        assertEquals("p", tag1.getName());

        // Test case for existing tag name
        Tag tag2 = Tag.valueOf("BODY");
        assertEquals("BODY", tag2.getName());

        // Test case for null tag name
        try {
            Tag tag3 = Tag.valueOf(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test case for empty tag name
        try {
            Tag tag4 = Tag.valueOf("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test case for tag name with whitespaces
        Tag tag5 = Tag.valueOf("   tag   ");
        assertEquals("tag", tag5.getName());
    }

    @Test
    public void testCanContain() {
        // Test case for valid child tag that can be contained
        Tag parentTag = Tag.valueOf("div");
        Tag childTag1 = Tag.valueOf("p");
        assertTrue(parentTag.canContain(childTag1));

        // Test case for invalid child tag that cannot be contained
        Tag childTag2 = Tag.valueOf("div");
        assertFalse(parentTag.canContain(childTag2));

        // Test case for child tag that is the same as parent tag
        Tag childTag3 = Tag.valueOf("div");
        assertFalse(parentTag.canContain(childTag3));

        // Test case for empty parent tag
        Tag emptyParentTag = Tag.valueOf("");
        Tag childTag4 = Tag.valueOf("p");
        assertFalse(emptyParentTag.canContain(childTag4));

        // Test case for parent tag that holds only specific tags
        Tag headTag = Tag.valueOf("head");
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(headTag.canContain(scriptTag));

        Tag linkTag = Tag.valueOf("link");
        assertFalse(headTag.canContain(linkTag));
    }

    @Test
    public void testIsBlock() {
        // Test case for tag that is block
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isBlock());

        // Test case for tag that is not block
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        // Test case for tag that can contain block tags
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());

        // Test case for tag that cannot contain block tags
        Tag tag2 = Tag.valueOf("input");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsData() {
        // Test case for tag that is data only
        Tag tag1 = Tag.valueOf("script");
        assertTrue(tag1.isData());

        // Test case for tag that is not data only
        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        // Test case for tag that is empty
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isEmpty());

        // Test case for tag that is not empty
        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        // Test case for tag that preserves whitespace
        Tag tag1 = Tag.valueOf("pre");
        assertTrue(tag1.preserveWhitespace());

        // Test case for tag that does not preserve whitespace
        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.preserveWhitespace());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAncestorWithNull() {
        Tag tag = new Tag("tag");
        tag.setAncestor(null);
    }

    @Test
    public void testSetAncestorWithEmptyList() {
        Tag tag = new Tag("tag");
        List<Tag> emptyList = Collections.emptyList();
        tag.setAncestor(emptyList.toArray(new Tag[0]));
        assertTrue(tag.isValidParent(new Tag("html")));
    }

    @Test
    public void testSetAncestorWithValidList() {
        Tag tag = new Tag("tag");
        List<Tag> validList = Arrays.asList(Tag.valueOf("html"), Tag.valueOf("head"), Tag.valueOf("body"));
        tag.setAncestor(validList.toArray(new Tag[0]));
        assertFalse(tag.isValidParent(new Tag("html"))); // tag is already an ancestor
        assertFalse(tag.isValidParent(new Tag("head"))); // not an ancestor
        assertTrue(tag.isValidParent(new Tag("body"))); // is an ancestor
    }

    @Test
    public void testEquals() {
        // Test case for equal objects
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        assertTrue(tag1.equals(tag2));

        // Test case for non-equal objects with same values
        Tag tag3 = new Tag("tag1");
        Tag tag4 = new Tag("tag1");
        assertTrue(tag3.equals(tag4));

        // Test case for non-equal objects with different values
        Tag tag5 = new Tag("tag1");
        Tag tag6 = new Tag("tag2");
        assertFalse(tag5.equals(tag6));

        // Test case for non-equal objects with different classes
        Tag tag7 = new Tag("tag1");
        Object obj = new Object();
        assertFalse(tag7.equals(obj));

        // Test case for null object
        Tag tag8 = new Tag("tag1");
        assertFalse(tag8.equals(null));
    }

    @Test
    public void testHashCode() {
        // Test case for equal objects with same values
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test case for non-equal objects with different values
        Tag tag3 = new Tag("tag1");
        Tag tag4 = new Tag("tag2");
        int hashCode3 = tag3.hashCode();
        int hashCode4 = tag4.hashCode();
        assertNotEquals(hashCode3, hashCode4);
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.toString());
    }
}