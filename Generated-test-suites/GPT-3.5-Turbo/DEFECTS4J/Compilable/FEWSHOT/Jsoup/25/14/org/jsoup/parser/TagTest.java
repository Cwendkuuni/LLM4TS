
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TagTest {
    @Test
    public void testName() {
        Tag tag = Tag.valueOf("Test");

        assertEquals("Test", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("Test");

        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("Test");

        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("Test");

        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("Test");

        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("Test");

        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("Test");

        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("Test");

        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("Test"));
    }

    @Test
    public void testIsNotKnownTag() {
        assertFalse(Tag.isKnownTag("Unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("Test");

        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("Test");
        Tag tag2 = Tag.valueOf("Test");

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("Test");
        Tag tag2 = Tag.valueOf("Test");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("Test");

        assertEquals("Test", tag.toString());
    }
}
