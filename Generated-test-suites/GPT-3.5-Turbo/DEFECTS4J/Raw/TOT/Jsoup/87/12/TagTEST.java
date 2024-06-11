
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("div");
    }

    @Test
    public void testName() {
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p", new ParseSettings(true));
        Tag tag3 = Tag.valueOf("notDefined");

        assertEquals("p", tag1.getName());
        assertEquals("p", tag2.getName());
        assertEquals("notDefined", tag3.getName());

        assertFalse(tag1.isBlock());
        assertFalse(tag2.isBlock());
        assertFalse(tag3.isBlock());

        assertFalse(tag1.formatAsBlock());
        assertFalse(tag2.formatAsBlock());
        assertFalse(tag3.formatAsBlock());

        assertFalse(tag1.canContainBlock());
        assertFalse(tag2.canContainBlock());
        assertFalse(tag3.canContainBlock());

        assertTrue(tag1.isInline());
        assertTrue(tag2.isInline());
        assertTrue(tag3.isInline());

        assertFalse(tag1.isData());
        assertFalse(tag2.isData());
        assertFalse(tag3.isData());

        assertFalse(tag1.isEmpty());
        assertFalse(tag2.isEmpty());
        assertFalse(tag3.isEmpty());

        assertFalse(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
        assertFalse(tag3.isSelfClosing());

        assertFalse(tag1.isKnownTag());
        assertFalse(tag2.isKnownTag());
        assertFalse(tag3.isKnownTag());
    }

    @Test
    public void testValueOf_CaseSensitive() {
        Tag tag1 = Tag.valueOf("big");
        Tag tag2 = Tag.valueOf("BIG");

        assertEquals("big", tag1.getName());
        assertEquals("BIG", tag2.getName());

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("img");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("span");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("textarea");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());

        tag = Tag.valueOf("a");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());

        tag = Tag.valueOf("input");
        assertTrue(tag.isData());

        tag = Tag.valueOf("br");
        assertTrue(tag.isData());

        tag = Tag.valueOf("img");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("hr");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(tag.isKnownTag());

        tag = Tag.valueOf("div");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTag_NameCaseSensitive() {
        assertFalse(Tag.isKnownTag("u"));
        assertFalse(Tag.isKnownTag("U"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(tag.isFormListed());

        tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(tag.isFormSubmittable());

        tag = Tag.valueOf("input");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        assertFalse(tag.isSelfClosing());

        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("h2");
        Tag tag2 = new Tag("h2");

        assertTrue(tag1.equals(tag2));
        assertTrue(tag2.equals(tag1));

        tag1 = new Tag("p");
        tag2 = new Tag("div");

        assertFalse(tag1.equals(tag2));
        assertFalse(tag2.equals(tag1));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("h2");
        Tag tag2 = new Tag("h2");

        assertEquals(tag1.hashCode(), tag2.hashCode());

        tag1 = new Tag("p");
        tag2 = new Tag("div");

        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("div", tag.toString());

        tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}

