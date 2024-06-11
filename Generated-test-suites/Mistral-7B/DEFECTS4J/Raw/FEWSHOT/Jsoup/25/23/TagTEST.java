package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertEquals(true, tag.isBlock());
        assertEquals(true, tag.formatAsBlock());
        assertEquals(true, tag.canContainBlock());
        assertEquals(true, tag.canContainInline());
        assertEquals(false, tag.isEmpty());
        assertEquals(false, tag.isSelfClosing());
        assertEquals(false, tag.preserveWhitespace());
    }

    @Test
    public void testUnknownTagCreation() {
        Tag tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());
        assertEquals(false, tag.isBlock());
        assertEquals(true, tag.canContainBlock());
        assertEquals(true, tag.canContainInline());
        assertEquals(false, tag.isEmpty());
        assertEquals(false, tag.isSelfClosing());
        assertEquals(false, tag.preserveWhitespace());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testValueOfEmptyString() {
        Tag tag = Tag.valueOf("");
        assertNull(tag);
    }

    @Test
    public void testValueOfNull() {
        assertNull(Tag.valueOf(null));
    }

    @Test
    public void testIsBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagBr = Tag.valueOf("br");
        assertTrue(tagP.isBlock());
        assertFalse(tagBr.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagBr = Tag.valueOf("br");
        assertTrue(tagP.formatAsBlock());
        assertFalse(tagBr.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagBr = Tag.valueOf("br");
        Tag tagDiv = Tag.valueOf("div");
        assertTrue(tagDiv.canContainBlock());
        assertTrue(tagP.canContainBlock());
        assertFalse(tagBr.canContainBlock());
    }

    @Test
    public void testCanContainInline() {
        Tag tagP = Tag.valueOf("p");
        Tag tagBr = Tag.valueOf("br");
        Tag tagDiv = Tag.valueOf("div");
        assertTrue(tagP.canContainInline());
        assertTrue(tagBr.canContainInline());
        assertTrue(tagDiv.canContainInline());
    }

    @Test
    public void testIsEmpty() {
        Tag tagImg = Tag.valueOf("img");
        Tag tagBr = Tag.valueOf("br");
        Tag tagInput = Tag.valueOf("input");
        assertEquals(false, tagImg.isEmpty());
        assertEquals(true, tagBr.isEmpty());
        assertEquals(false, tagInput.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tagImg = Tag.valueOf("img");
        Tag tagBr = Tag.valueOf("br");
        Tag tagP = Tag.valueOf("p");
        assertEquals(true, tagImg.isSelfClosing());
        assertEquals(true, tagBr.isSelfClosing());
        assertEquals(false, tagP.isSelfClosing());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tagPre = Tag.valueOf("pre");
        Tag tagDiv = Tag.valueOf("div");
        assertFalse(tagDiv.preserveWhitespace());
        assertTrue(tagPre.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tagUnknown = Tag.valueOf("unknown");
        Tag tagSelfClosing = tagUnknown.setSelfClosing();
        assertTrue(tagSelfClosing.isSelfClosing());
    }
}