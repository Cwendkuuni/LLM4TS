package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(Tag.isKnownTag(htmlTag.getName()));
        Tag unknownTag = new Tag("unknown");
        assertFalse(Tag.isKnownTag(unknownTag.getName()));
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());
        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());
        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.canContainBlock());
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());
        Tag metaTag = Tag.valueOf("meta");
        assertTrue(metaTag.isEmpty());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isInline());
        Tag aTag = Tag.valueOf("a");
        assertTrue(aTag.isInline());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTagWithInvalidArgument() {
        assertFalse(Tag.isKnownTag(null));
        assertFalse(Tag.isKnownTag(""));
        assertFalse(Tag.isKnownTag(" "));
    }

    @Test
    public void testIsBlockWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).isBlock());
        assertFalse(Tag.valueOf("").isBlock());
        assertFalse(Tag.valueOf(" ").isBlock());
    }

    @Test
    public void testFormatAsBlockWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).formatAsBlock());
        assertFalse(Tag.valueOf("").formatAsBlock());
        assertFalse(Tag.valueOf(" ").formatAsBlock());
    }

    @Test
    public void testCanContainBlockWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).canContainBlock());
        assertFalse(Tag.valueOf("").canContainBlock());
        assertFalse(Tag.valueOf(" ").canContainBlock());
    }

    @Test
    public void testIsEmptyWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).isEmpty());
        assertFalse(Tag.valueOf("").isEmpty());
        assertFalse(Tag.valueOf(" ").isEmpty());
    }

    @Test
    public void testIsDataWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).isData());
        assertFalse(Tag.valueOf("").isData());
        assertFalse(Tag.valueOf(" ").isData());
    }

    @Test
    public void testIsInlineWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).isInline());
        assertFalse(Tag.valueOf("").isInline());
        assertFalse(Tag.valueOf(" ").isInline());
    }

    @Test
    public void testIsSelfClosingWithInvalidArgument() {
        assertFalse(Tag.valueOf(null).isSelfClosing());
        assertFalse(Tag.valueOf("").isSelfClosing());
        assertFalse(Tag.valueOf(" ").isSelfClosing());
    }
}