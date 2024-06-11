package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Tag.
 */
public class TagTest {

    @Test
    public void testValueOfWithKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        // Add more assertions as needed
    }

    @Test
    public void testValueOfWithUnknownTag() {
        ParseSettings settings = new ParseSettings();
        Tag tag = Tag.valueOf("UnknownTag", settings);
        assertEquals("UnknownTag", tag.getName());
        assertFalse(tag.isBlock());
        // Add more assertions as needed
    }

    @Test
    public void testValueOfWithCaseSensitiveTag() {
        Tag tag = Tag.valueOf("P");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        // Add more assertions as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithNullTagName() {
        Tag.valueOf(null); // Add more assertions as needed
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("UnknownTag"));
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertTrue(pTag.canContainBlock());
        assertTrue(divTag.canContainBlock());

        // Test with empty and self-closing tags
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isInline());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isInline());
        // Add more tests as needed
    }

    @Test
    public void testIsData() {
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isData());

        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());
        // Add more tests as needed
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isEmpty());
        // Add more tests as needed
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isSelfClosing());
        // Add more tests as needed
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
        // Add more tests as needed
    }

    @Test
    public void testIsFormListed() {
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormListed());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormListed());
        // Add more tests as needed
    }

    @Test
    public void testIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input[type=text]");
        assertTrue(inputTag.isFormSubmittable());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormSubmittable());
        // Add more tests as needed
    }

}