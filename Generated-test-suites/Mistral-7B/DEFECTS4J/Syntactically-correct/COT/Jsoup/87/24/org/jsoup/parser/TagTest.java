package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Tag.
 */
public class TagTest {

    @Test
    public void testTagName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testTagValueOfWithSettings() {
        Tag tag = Tag.valueOf("P", ParseSettings.preserveCase);
        assertEquals("p", tag.getName());
    }

    @Test
    public void testTagIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag pTag = new Tag("p");
        assertTrue(pTag.formatAsBlock());

        Tag spanTag = new Tag("span");
        assertFalse(spanTag.formatAsBlock());
    }

    @Test
    public void testTagIsData() {
        Tag scriptTag = new Tag("script");
        assertTrue(scriptTag.isData());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = new Tag("img");
        assertTrue(imgTag.isEmpty());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag imgTag = new Tag("img");
        assertTrue(imgTag.isSelfClosing());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("UnknownTag"));
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = new Tag("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = new Tag("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag inputTag = new Tag("input");
        assertTrue(inputTag.isFormListed());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag inputTag = new Tag("input");
        assertTrue(inputTag.isFormSubmittable());

        Tag labelTag = new Tag("label");
        assertFalse(labelTag.isFormSubmittable());
    }

    @Test(expected = NullPointerException.class)
    public void testTagNullTagName() {
        new Tag(null);
    }

    @Test
    public void testTagNormalizedTagName() {
        Tag pTag = Tag.valueOf("p");
        Tag PTag = Tag.valueOf("P");
        assertEquals(pTag, PTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagEmptyTagName() {
        new Tag("");
    }

    @Test
    public void testTagEquals() {
        Tag p1 = new Tag("p");
        Tag p2 = new Tag("p");
        Tag span1 = new Tag("span");
        Tag span2 = new Tag("span");

        assertEquals(p1, p2);
        assertNotEquals(p1, span1);
        assertNotEquals(p1, null);
    }

    @Test
    public void testTagHashCode() {
        Tag p1 = new Tag("p");
        Tag p2 = new Tag("p");
        Tag span1 = new Tag("span");

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), span1.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag pTag = new Tag("p");
        assertEquals("p", pTag.toString());
    }
}