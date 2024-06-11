package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    Tag htmlTag;
    Tag divTag;
    Tag pTag;

    @Before
    public void setUp() {
        htmlTag = Tag.valueOf("html");
        divTag = Tag.valueOf("div");
        pTag = Tag.valueOf("p");
    }

    @Test
    public void testGetName() {
        assertEquals("html", htmlTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("p", pTag.getName());
    }

    @Test
    public void testValueOf() {
        Tag preDefinedTag = Tag.valueOf("title");
        Tag unknownTag = Tag.valueOf("unknown");

        // Test pre-defined tag
        assertTrue(preDefinedTag.isKnownTag());
        assertEquals("title", preDefinedTag.getName());
        assertTrue(preDefinedTag.isInline());
        assertFalse(preDefinedTag.isBlock());
        assertFalse(preDefinedTag.formatAsBlock());
        assertTrue(preDefinedTag.canContainBlock());
        assertFalse(preDefinedTag.canContainInline());

        // Test unknown tag
        assertFalse(unknownTag.isKnownTag());
        assertEquals("unknown", unknownTag.getName());
        assertFalse(unknownTag.isInline());
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.canContainInline());
    }

    @Test
    public void testIsBlock() {
        assertTrue(divTag.isBlock());
        assertTrue(pTag.isBlock());
        assertFalse(htmlTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(divTag.formatAsBlock());
        assertTrue(pTag.formatAsBlock());
        assertTrue(htmlTag.formatAsBlock());

        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(divTag.canContainBlock());
        assertTrue(htmlTag.canContainBlock());

        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(divTag.isInline());
        assertFalse(pTag.isInline());
        assertTrue(htmlTag.isInline());

        Tag spanTag = Tag.valueOf("span");
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(divTag.isData());
        assertFalse(pTag.isData());
        assertFalse(htmlTag.isData());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isData());

        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(divTag.isEmpty());
        assertFalse(pTag.isEmpty());
        assertFalse(htmlTag.isEmpty());

        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(divTag.isSelfClosing());
        assertFalse(pTag.isSelfClosing());
        assertFalse(htmlTag.isSelfClosing());

        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());

        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(divTag.isKnownTag());
        assertTrue(pTag.isKnownTag());
        assertTrue(htmlTag.isKnownTag());

        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("html"));

        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(divTag.preserveWhitespace());
        assertFalse(pTag.preserveWhitespace());
        assertFalse(htmlTag.preserveWhitespace());

        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag plaintextTag = Tag.valueOf("plaintext");
        assertTrue(plaintextTag.preserveWhitespace());
    }
}