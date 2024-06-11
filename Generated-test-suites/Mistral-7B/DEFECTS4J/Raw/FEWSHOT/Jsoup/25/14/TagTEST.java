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
    public void testIsKnownTagWithLowerCase() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(Tag.isKnownTag("html".toLowerCase()));

        Tag unknownTag = new Tag("Unknown");
        assertFalse(Tag.isKnownTag("unknown".toLowerCase()));
    }

    @Test
    public void testIsBlock() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(htmlTag.isBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(htmlTag.formatAsBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(htmlTag.canContainBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.canContainBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());

        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isSelfClosing());

        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isSelfClosing());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testIsInline() {
        Tag htmlTag = Tag.valueOf("html");
        assertFalse(htmlTag.isInline());

        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isInline());
    }

    @Test
    public void testIsKnownTagForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertTrue(Tag.isKnownTag(metaTag.getName()));

        Tag imgTag = Tag.valueOf("img");
        assertTrue(Tag.isKnownTag(imgTag.getName()));
    }

    @Test
    public void testIsBlockForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlockForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.formatAsBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlockForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.canContainBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testCanContainInlineForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.canContainInline());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainInline());
    }

    @Test
    public void testEmptyForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.isEmpty());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());
    }

    @Test
    public void testSelfClosingForEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.isSelfClosing());

        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());
    }

    @Test
    public void testRegisterTags() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(Tag.isKnownTag(htmlTag.getName()));

        Tag divTag = new Tag("div");
        divTag.isBlock = false;
        divTag.canContainBlock = true;
        divTag.formatAsBlock = false;
        divTag.register();

        Tag newDivTag = Tag.valueOf("div");
        assertTrue(newDivTag.equals(divTag));
    }
}