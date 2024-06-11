package org.jsoup.parser;

import org.jsoup.helper.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * HTML Tag capabilities.
 *
 * @author Jonathan Hedley, jonathan@hedley.net
 */
public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("tag");
        assertEquals("tag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("tag", ParseSettings.preserveCase);
        assertEquals("tag", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("tag", new ParseSettings());
        assertEquals("tag", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("tag");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("tag");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("tag");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithString() {
        boolean knownTag = Tag.isKnownTag("tag");
        assertFalse(knownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("tag");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("tag");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("tag1");
        Tag tag2 = Tag.valueOf("tag1");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("tag1");
        Tag tag2 = Tag.valueOf("tag1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("tag");
        assertEquals("tag", tag.toString());
    }
}