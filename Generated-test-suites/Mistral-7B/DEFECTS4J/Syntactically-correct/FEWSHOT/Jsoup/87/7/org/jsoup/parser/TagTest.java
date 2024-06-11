package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreationAndBasicProperties() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals("p", pTag.getName());
        assertEquals("p", pTag.toString());
        assertEquals(true, pTag.isBlock());
        assertEquals(true, pTag.formatAsBlock());
        assertEquals(true, pTag.canContainInline());
        assertEquals(false, pTag.isEmpty());
        assertEquals(false, pTag.isSelfClosing());
        assertEquals(false, pTag.isKnownTag());

        assertEquals("div", divTag.getName());
        assertEquals("div", divTag.toString());
        assertEquals(true, divTag.isBlock());
        assertEquals(true, divTag.formatAsBlock());
        assertEquals(true, divTag.canContainInline());
        assertEquals(false, divTag.isEmpty());
        assertEquals(false, divTag.isSelfClosing());
        assertEquals(true, Tag.isKnownTag("div"));
    }

    @Test
    public void testTagValueOfWithNormalize() {
        Tag pTag = Tag.valueOf("p");
        Tag PTag = Tag.valueOf("P");

        assertEquals(pTag, pTag);
        assertEquals(pTag, PTag);
        assertEquals("p", pTag.getName());
        assertEquals(true, pTag.isBlock());
    }

    @Test
    public void testTagValueOfWithUnknown() {
        Tag unknownTag = Tag.valueOf("unknown");

        assertFalse(unknownTag.isBlock());
        assertEquals("unknown", unknownTag.getName());
        assertEquals(false, unknownTag.isSelfClosing());
        assertEquals(false, unknownTag.isEmpty());
        assertEquals(false, unknownTag.isKnownTag());
    }

    @Test
    public void testTagSelfClosing() {
        Tag imgTag = Tag.valueOf("img").setSelfClosing();

        assertEquals(false, imgTag.isEmpty());
        assertEquals(true, imgTag.isSelfClosing());
    }

    @Test
    public void testTagEqualsAndHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals(pTag1, pTag1);
        assertEquals(pTag1, pTag2);
        assertEquals(pTag1.hashCode(), pTag2.hashCode());
        assertNotEquals(pTag1, divTag);
        assertNotEquals(pTag1.hashCode(), divTag.hashCode());
    }

    @Test
    public void testTagIsBlock() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p").isBlock());
        assertFalse(Tag.isKnownTag("input").isBlock());
    }

    @Test
    public void testTagCanContainInline() {
        assertTrue(Tag.isKnownTag("p").canContainInline());
        assertTrue(Tag.isKnownTag("div").canContainInline());
        assertFalse(Tag.isKnownTag("script").canContainInline());
    }

    @Test
    public void testTagIsEmpty() {
        assertTrue(Tag.isKnownTag("img").isEmpty());
        assertFalse(Tag.isKnownTag("p").isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        assertTrue(Tag.isKnownTag("img").isSelfClosing());
        assertFalse(Tag.isKnownTag("p").isSelfClosing());
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag inputTypeSubmitTag = Tag.valueOf("input").setFormSubmit(true);
        assertTrue(inputTypeSubmitTag.isFormSubmittable());
    }
}