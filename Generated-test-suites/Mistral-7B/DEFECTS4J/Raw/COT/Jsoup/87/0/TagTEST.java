package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class TagTest {

    

    @Test
    public void testTagNameGetter() {
        Tag tag = new Tag("p");
        String name = tag.getName();
        assertEquals("p", name);
    }

    @Test
    public void testTagNameValueOfCaseInsensitive() {
        Tag tag = Tag.valueOf("p");
        String name = tag.getName();
        assertEquals("p", name);
    }

    @Test
    public void testTagNameValueOfCaseSensitive() {
        Tag tag = Tag.valueOf("P");
        String name = tag.getName();
        assertEquals("p", name);
    }

    @Test
    public void testUnknownTagValueOf() {
        Tag tag = Tag.valueOf("unknownTag");
        String name = tag.getName();
        assertEquals("unknownTag", name);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsBlockDefault() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlockDefault() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainInlineDefault() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainInline());
    }

    @Test
    public void testEmptyDefault() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testSelfClosingDefault() {
        Tag tag = new Tag("img");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespaceDefault() {
        Tag tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListedDefault() {
        Tag tag = new Tag("p");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittableDefault() {
        Tag tag = new Tag("p");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("img");
        tag = tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEqualsSameTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("h1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCodeSameTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testHashCodeDifferentTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("h1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfNullTagName() {
        Tag tag = Tag.valueOf(null);
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfEmptyTagName() {
        Tag tag = Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyList() {
        Tag tag = Tag.valueOf(new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNullParseSettings() {
        Tag tag = Tag.valueOf("p", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyParseSettings() {
        Tag tag = Tag.valueOf("p", new ParseSettings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalidParseSettings() {
        Tag tag = Tag.valueOf("p", new ParseSettings() {
            @Override
            public String normalizeTag(String name) {
                throw new RuntimeException("Should not be called.");
            }
        });
    }

    @Test
    public void testIsBlockFalse() {
        Tag tag = new Tag("input");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlockFalse() {
        Tag tag = new Tag("input");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainInlineFalse() {
        Tag tag = new Tag("script");
        assertFalse(tag.canContainInline());
    }

    @Test
    public void testEmptyTrue() {
        Tag tag = new Tag("img");
        tag.empty = true;
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testSelfClosingTrue() {
        Tag tag = new Tag("img");
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testPreserveWhitespaceTrue() {
        Tag tag = new Tag("pre");
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListedTrue() {
        Tag tag = new Tag("input");
        tag.formList = true;
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittableTrue() {
        Tag tag = new Tag("input");
        tag.formSubmit = true;
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testEqualsWithSameObject() {
        Tag tag1 = new Tag("p");
        Tag tag2 = tag1;
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentObjectButSameValues() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1, tag2);
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentObjectButDifferentValues() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");
        assertFalse(tag1.equals(tag2));
    }

    
}