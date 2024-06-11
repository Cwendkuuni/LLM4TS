package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(Tag.isKnownTag(pTag.getName()));

        Tag unknownTag = new Tag("unknown");
        assertFalse(Tag.isKnownTag(unknownTag.getName()));
    }

    @Test
    public void testIsBlockTag() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.canContainBlock());

        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.canContainBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsInlineTag() {
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isInline());

        Tag spanTag = Tag.valueOf("span");
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testIsDataTag() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());

        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTagWhenUnknownIsRegistered() {
        Tag unknownTag = new Tag("unknown");
        unknownTag.setSelfClosing();
        Tag registeredUnknownTag = Tag.valueOf("unknown");

        assertTrue(registeredUnknownTag.isSelfClosing());
    }
}