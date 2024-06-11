
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag1 = Tag.valueOf("p", settings);
        Tag tag2 = Tag.valueOf("P", settings);
        assertSame(tag1, tag2);
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("DIV");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("data");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = new Tag("img").setSelfClosing();
        assertTrue(tag1.isSelfClosing());

        Tag tag2 = new Tag("div");
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("span"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag1 = new Tag("input");
        assertTrue(tag1.isFormListed());

        Tag tag2 = new Tag("div");
        assertFalse(tag2.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag1 = new Tag("input");
        assertTrue(tag1.isFormSubmittable());

        Tag tag2 = new Tag("div");
        assertFalse(tag2.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = new Tag("span");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}
