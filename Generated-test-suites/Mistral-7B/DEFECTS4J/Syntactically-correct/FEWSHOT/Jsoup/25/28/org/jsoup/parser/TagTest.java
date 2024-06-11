package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testValueOf() {
        Tag tagP = Tag.valueOf("p");
        Tag tagUnknown = Tag.valueOf("unknown");

        assertEquals("p", tagP.getName());
        assertEquals("unknown", tagUnknown.getName());
        assertTrue(tagP.isKnownTag());
        assertFalse(tagUnknown.isKnownTag());
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
        Tag tagPre = Tag.valueOf("pre");
        Tag tagP = Tag.valueOf("p");

        assertTrue(tagPre.formatAsBlock());
        assertTrue(tagP.formatAsBlock());
        assertFalse(tagPre.isInline());
        assertTrue(tagP.isInline());
    }

    @Test
    public void testCanContainBlock() {
        Tag tagDiv = Tag.valueOf("div");
        Tag tagP = Tag.valueOf("p");

        assertTrue(tagDiv.canContainBlock());
        assertFalse(tagP.canContainBlock());
    }

    @Test
    public void testIsData() {
        Tag tagScript = Tag.valueOf("script");
        Tag tagP = Tag.valueOf("p");

        assertTrue(tagScript.isData());
        assertFalse(tagP.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tagImg = Tag.valueOf("img");
        Tag tagBr = Tag.valueOf("br");

        assertFalse(tagImg.isEmpty());
        assertTrue(tagBr.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tagBr = Tag.valueOf("br");
        Tag tagP = Tag.valueOf("p");

        assertTrue(tagBr.isSelfClosing());
        assertFalse(tagP.isSelfClosing());
    }

    @Test
    public void testIsKnownTagWithNull() {
        assertFalse(Tag.isKnownTag(null));
    }

    @Test
    public void testIsKnownTagWithEmptyString() {
        assertFalse(Tag.isKnownTag(""));
    }

    @Test
    public void testValueOfWithNull() {
        assertNull(Tag.valueOf(null));
    }

    @Test
    public void testValueOfWithEmptyString() {
        assertNull(Tag.valueOf(""));
    }
}