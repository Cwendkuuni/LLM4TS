
package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("test");
    }

    @After
    public void tearDown() {
        tag = null;
    }

    @Test
    public void testName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p", ParseSettings.preserveCase);

        assertNotNull(tag1);
        assertNotNull(tag2);
        assertEquals("p", tag1.getName());
        assertEquals("p", tag2.getName());
    }

    @Test
    public void testValueOfUnknown() {
        Tag tag = Tag.valueOf("unknown");

        assertNotNull(tag);
        assertEquals("unknown", tag.getName());
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag selfClosingTag = tag.setSelfClosing();

        assertTrue(selfClosingTag.isSelfClosing());
        assertFalse(selfClosingTag.isEmpty());
    }

    @Test
    public void testEquals() {
        Tag tagCopy = new Tag("test");
        assertTrue(tag.equals(tagCopy));
    }

    @Test
    public void testNotEquals() {
        Tag anotherTag = new Tag("another");
        assertFalse(tag.equals(anotherTag));
    }

    @Test
    public void testHashCode() {
        Tag tagCopy = new Tag("test");
        assertEquals(tag.hashCode(), tagCopy.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }
}
