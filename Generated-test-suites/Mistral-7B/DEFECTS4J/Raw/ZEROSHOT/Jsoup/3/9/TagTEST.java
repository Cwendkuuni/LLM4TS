package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("p");
        String tagName = tag.getName();
        assertEquals("p", tagName);
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        String tagName = tag.getName();
        assertEquals("p", tagName);

        Tag unknownTag = Tag.valueOf("UNKNOWN");
        String unknownTagName = unknownTag.getName();
        assertNotEquals("UNKNOWN", unknownTagName);
        assertEquals("unknown", unknownTagName);
    }

    @Test
    public void testTagCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");
        Tag imgTag = Tag.valueOf("img");
        Tag headTag = Tag.valueOf("head");
        Tag bodyTag = Tag.valueOf("body");

        // p can contain div and span
        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(spanTag));

        // p cannot contain p
        assertFalse(pTag.canContain(pTag));

        // p can contain img but only if img is implicitly defined as a descendant of p
        assertTrue(pTag.canContain(imgTag));

        // head cannot contain body
        assertFalse(headTag.canContain(bodyTag));

        // body can contain anything, so can contain head
        assertTrue(bodyTag.canContain(headTag));
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");
        Tag imgTag = Tag.valueOf("img");

        // p is a block tag
        assertTrue(pTag.isBlock());

        // div is a block tag
        assertTrue(divTag.isBlock());

        // span is an inline tag
        assertFalse(spanTag.isBlock());

        // img is an inline tag
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        // p can contain div
        assertTrue(pTag.canContainBlock());

        // div can contain div
        assertTrue(divTag.canContainBlock());

        // span cannot contain block
        assertFalse(spanTag.canContainBlock());
    }

    @Test
    public void testTagIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        // p is a block tag
        assertFalse(pTag.isInline());

        // div is a block tag
        assertFalse(divTag.isInline());

        // span is an inline tag
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag scriptTag = Tag.valueOf("script");

        // p is not a data only tag
        assertFalse(pTag.isData());

        // div is not a data only tag
        assertFalse(divTag.isData());

        // script is a data only tag
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // img is an empty tag
        assertTrue(imgTag.isEmpty());

        // p is not an empty tag
        assertFalse(pTag.isEmpty());

        // div is not an empty tag
        assertFalse(divTag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag textareaTag = Tag.valueOf("textarea");
        Tag pTag = Tag.valueOf("p");

        // pre tag preserves whitespace
        assertTrue(preTag.preserveWhitespace());

        // textarea tag preserves whitespace
        assertTrue(textareaTag.preserveWhitespace());

        // p tag does not preserve whitespace
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div").setAncestor("p");

        // p tag has no implicit parent
        assertNull(pTag.getImplicitParent());

        // div tag has an implicit parent of p
        assertEquals("p", divTag.getImplicitParent().getName());
    }

    @Test
    public void testTagIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag headTag = Tag.valueOf("head");

        // p is a valid parent for div
        assertTrue(pTag.isValidParent(divTag));

        // head is not a valid parent for p
        assertFalse(headTag.isValidParent(pTag));
    }

    @Test
    public void testTagEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // p tags are equal
        assertEquals(pTag1, pTag2);

        // p and div are not equal
        assertNotEquals(pTag1, divTag);
    }

    @Test
    public void testTagHashcode() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // p and p have the same hashcode
        int pHashcode = pTag.hashCode();
        int pHashcode2 = pTag.hashCode();
        assertEquals(pHashcode, pHashcode2);

        // p and div have different hashcodes
        int pHashcode3 = pTag.hashCode();
        int divHashcode = divTag.hashCode();
        assertNotEquals(pHashcode3, divHashcode);
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("p");

        // p tag toString returns tag name
        assertEquals("p", pTag.toString());
    }
}