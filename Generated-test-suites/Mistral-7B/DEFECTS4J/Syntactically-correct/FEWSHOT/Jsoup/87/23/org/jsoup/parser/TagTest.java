package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testValueOfWithPredefinedTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isData());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfWithUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainInline);
        assertFalse(tag.empty);
        assertFalse(tag.selfClosing);
        assertFalse(tag.preserveWhitespace);
        assertFalse(tag.formList);
        assertFalse(tag.formSubmit);
        assertEquals("unknown", tag.getName());
    }

    @Test
    public void testValueOfWithCaseSensitiveTag() {
        Tag tag = Tag.valueOf("P");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isData());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
        assertEquals("p", tag.getName());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
        assertFalse(Tag.isKnownTag("P"));
    }

    @Test
    public void testIsBlock() {
        assertTrue(Tag.valueOf("p").isBlock());
        assertFalse(Tag.valueOf("input").isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(Tag.valueOf("p").formatAsBlock());
        assertFalse(Tag.valueOf("input").formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(Tag.valueOf("div").canContainBlock());
        assertFalse(Tag.valueOf("input").canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(Tag.valueOf("p").isInline());
        assertTrue(Tag.valueOf("input").isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(Tag.valueOf("p").isData());
        assertTrue(Tag.valueOf("script").isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(Tag.valueOf("p").isEmpty());
        assertTrue(Tag.valueOf("img").isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(Tag.valueOf("p").isSelfClosing());
        assertTrue(Tag.valueOf("img").isSelfClosing());
    }

    @Test
    public void testPreserveWhitespace() {
        assertTrue(Tag.valueOf("pre").preserveWhitespace());
        assertFalse(Tag.valueOf("p").preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertTrue(Tag.valueOf("input").isFormListed());
        assertFalse(Tag.valueOf("p").isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertTrue(Tag.valueOf("input").isFormSubmittable());
        assertFalse(Tag.valueOf("p").isFormSubmittable());
    }

    @Test
    public void testHashCodeAndEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("unknown");
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("p", Tag.valueOf("p").toString());
    }

    @Test
    public void testRegisteredTags() {
        assertEquals(Tag.valueOf("p"), Tag.valueOf("P"));
        assertEquals(Tag.valueOf("p"), Tag.valueOf("p", ParseSettings.preserveCase));
        assertEquals(Tag.valueOf("p"), Tag.valueOf("p", ParseSettings.normalizeTag));
    }
}