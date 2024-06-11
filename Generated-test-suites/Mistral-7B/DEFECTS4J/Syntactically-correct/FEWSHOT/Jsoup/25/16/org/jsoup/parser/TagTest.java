package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag(null));
        assertFalse(Tag.isKnownTag(""));
        assertFalse(Tag.isKnownTag(" "));
        assertFalse(Tag.isKnownTag("tagNameX"));

        Tag htmlTag = Tag.valueOf("html");
        assertTrue(htmlTag.isKnownTag());
        assertTrue(htmlTag.isBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isKnownTag());
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isKnownTag());
        assertFalse(imgTag.isBlock());

        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.isKnownTag());
        assertTrue(divTag.isBlock());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isKnownTag());
        assertFalse(inputTag.isBlock());
    }

    @Test
    public void testEmptyTags() {
        Tag metaTag = Tag.valueOf("meta");
        assertTrue(metaTag.isKnownTag());
        assertFalse(metaTag.isBlock());
        assertFalse(metaTag.isEmpty());

        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isKnownTag());
        assertFalse(imgTag.isBlock());
        assertTrue(imgTag.isEmpty());

        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isKnownTag());
        assertFalse(brTag.isBlock());
        assertTrue(brTag.isEmpty());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isKnownTag());
        assertFalse(inputTag.isBlock());
        assertFalse(inputTag.isEmpty());

        Tag colTag = Tag.valueOf("col");
        assertTrue(colTag.isKnownTag());
        assertFalse(colTag.isBlock());
        assertTrue(colTag.isEmpty());
    }

    @Test
    public void testFormatAsBlockTags() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.isKnownTag());
        assertTrue(preTag.isBlock());
        assertTrue(preTag.formatAsBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isKnownTag());
        assertTrue(pTag.isBlock());
        assertFalse(pTag.formatAsBlock());

        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.isKnownTag());
        assertTrue(divTag.isBlock());
        assertFalse(divTag.formatAsBlock());

        Tag ulTag = Tag.valueOf("ul");
        assertTrue(ulTag.isKnownTag());
        assertTrue(ulTag.isBlock());
        assertTrue(ulTag.formatAsBlock());

        Tag liTag = Tag.valueOf("li");
        assertTrue(liTag.isKnownTag());
        assertFalse(liTag.isBlock());
        assertFalse(liTag.formatAsBlock());
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.isKnownTag());
        assertTrue(preTag.isBlock());
        assertTrue(preTag.preserveWhitespace());

        Tag plaintextTag = Tag.valueOf("plaintext");
        assertTrue(plaintextTag.isKnownTag());
        assertFalse(plaintextTag.isBlock());
        assertTrue(plaintextTag.preserveWhitespace());

        Tag titleTag = Tag.valueOf("title");
        assertTrue(titleTag.isKnownTag());
        assertFalse(titleTag.isBlock());
        assertFalse(titleTag.preserveWhitespace());
    }

    @Test
    public void testGetTagByName() {
        assertNull(Tag.valueOf(null));
        assertNull(Tag.valueOf(""));

        Tag htmlTag = Tag.valueOf("html");
        Tag headTag = Tag.valueOf("head");
        Tag bodyTag = Tag.valueOf("body");

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertNotNull(unknownTag);
        assertTrue(unknownTag.tagName.equals("unknownTag"));
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertFalse(unknownTag.canContainBlock());
        assertFalse(unknownTag.canContainInline());
        assertFalse(unknownTag.empty);
        assertFalse(unknownTag.selfClosing());
        assertFalse(unknownTag.preserveWhitespace());
    }
}