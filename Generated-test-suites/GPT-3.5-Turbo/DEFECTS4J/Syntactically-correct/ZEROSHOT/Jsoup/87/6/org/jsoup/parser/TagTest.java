package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    // Test getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    // Test valueOf() method
    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p", ParseSettings.preserveCase);
        assertEquals("p", tag1.getName());
        assertEquals("p", tag2.getName());
    }

    // Test isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    // Test formatAsBlock() method
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }

    // Test canContainBlock() method
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }

    // Test isInline() method
    @Test
    public void testIsInline() {
        Tag tag = new Tag("p");
        assertFalse(tag.isInline());
    }

    // Test isData() method
    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        assertFalse(tag.isData());
    }

    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());
    }

    // Test isSelfClosing() method
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());
    }

    // Test isKnownTag() method
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("x"));
    }

    // Test preserveWhitespace() method
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());
    }

    // Test isFormListed() method
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("p");
        assertFalse(tag.isFormListed());
    }

    // Test isFormSubmittable() method
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("p");
        assertFalse(tag.isFormSubmittable());
    }

    // Test setSelfClosing() method
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    // Test equals() method
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));
    }

    // Test hashCode() method
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    // Test toString() method
    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}