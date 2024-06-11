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
    public void testTagValues() {
        // Test getting a tag by name
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertEquals(true, pTag.isBlock());

        // Test getting a non-existent tag
        Tag nonExistentTag = Tag.valueOf("nonExistentTag");
        assertNotNull(nonExistentTag);
        assertEquals("nonExistentTag", nonExistentTag.getName());
        assertFalse(nonExistentTag.isBlock());

        // Test getting a known tag with settings
        Tag pTagWithSettings = Tag.valueOf("p", null);
        assertEquals("p", pTagWithSettings.getName());
        assertEquals(true, pTagWithSettings.isBlock());

        // Test getting a known tag with case sensitive settings
        Tag PTag = Tag.valueOf("P");
        assertEquals("p", PTag.getName());
        assertEquals(true, PTag.isBlock());
    }

    @Test
    public void testIsBlock() {
        // Test block tags
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(htmlTag.isBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        // Test inline tags
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());

        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.isBlock());

        // Test empty tags
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.isBlock());
        assertTrue(metaTag.isEmpty());

        // Test self-closing tags
        Tag imgSelfClosingTag = Tag.valueOf("img", null).setSelfClosing();
        assertFalse(imgSelfClosingTag.isEmpty());
        assertTrue(imgSelfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsFormListed() {
        // Test form listed tags
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormListed());

        Tag textareaTag = Tag.valueOf("textarea");
        assertTrue(textareaTag.isFormListed());

        // Test non-form listed tags
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isFormListed());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        // Test form submittable tags
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormSubmittable());

        Tag selectTag = Tag.valueOf("select");
        assertTrue(selectTag.isFormSubmittable());

        // Test non-form submittable tags
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isFormSubmittable());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormSubmittable());
    }

    @Test
    public void testHashCodeAndEquals() {
        // Test equal tags have same hashcode
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        int hashcode1 = tag1.hashCode();
        int hashcode2 = tag2.hashCode();
        assertEquals(hashcode1, hashcode2);

        // Test not equal tags have different hashcodes
        Tag tag3 = new Tag("tag3");
        int hashcode3 = tag3.hashCode();
        assertNotSame(hashcode1, hashcode3);

        // Test equals method
        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

}