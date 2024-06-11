package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertEquals("p", Tag.valueOf("p").getName());
        assertTrue(Tag.isKnownTag("p"));
        assertEquals(Tag.valueOf("p"), Tag.valueOf("P"));
        assertFalse(Tag.isKnownTag(""));
        assertFalse(Tag.isKnownTag(null));
    }

    @Test
    public void testIsBlockTag() {
        assertTrue(Tag.valueOf("p").isBlock());
        assertTrue(Tag.valueOf("div").isBlock());
        assertFalse(Tag.valueOf("span").isBlock());
        assertFalse(Tag.valueOf("img").isBlock());
        assertFalse(Tag.valueOf("input").isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(Tag.valueOf("p").formatAsBlock());
        assertTrue(Tag.valueOf("div").formatAsBlock());
        assertFalse(Tag.valueOf("span").formatAsBlock());
        assertFalse(Tag.valueOf("img").formatAsBlock());
        assertFalse(Tag.valueOf("input").formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(Tag.valueOf("div").canContainBlock());
        assertTrue(Tag.valueOf("ul").canContainBlock());
        assertTrue(Tag.valueOf("ol").canContainBlock());
        assertTrue(Tag.valueOf("table").canContainBlock());
        assertFalse(Tag.valueOf("p").canContainBlock());
        assertFalse(Tag.valueOf("span").canContainBlock());
        assertFalse(Tag.valueOf("img").canContainBlock());
        assertFalse(Tag.valueOf("input").canContainBlock());
    }

    @Test
    public void testIsInlineTag() {
        assertFalse(Tag.valueOf("p").isInline());
        assertFalse(Tag.valueOf("div").isInline());
        assertTrue(Tag.valueOf("span").isInline());
        assertTrue(Tag.valueOf("img").isInline());
        assertTrue(Tag.valueOf("input").isInline());
    }

    @Test
    public void testIsDataTag() {
        assertFalse(Tag.valueOf("p").isData());
        assertFalse(Tag.valueOf("div").isData());
        assertFalse(Tag.valueOf("span").isData());
        assertFalse(Tag.valueOf("img").isData());
        assertTrue(Tag.valueOf("script").isData());
        assertTrue(Tag.valueOf("style").isData());
    }

    @Test
    public void testIsEmptyTag() {
        assertFalse(Tag.valueOf("p").isEmpty());
        assertFalse(Tag.valueOf("div").isEmpty());
        assertFalse(Tag.valueOf("span").isEmpty());
        assertFalse(Tag.valueOf("img").isEmpty());
        assertTrue(Tag.valueOf("img").isSelfClosing());
        assertTrue(Tag.valueOf("input").isEmpty());
        assertTrue(Tag.valueOf("input:hidden").isEmpty());
        assertTrue(Tag.valueOf("meta").isEmpty());
        assertTrue(Tag.valueOf("link").isEmpty());
        assertTrue(Tag.valueOf("base").isEmpty());
        assertTrue(Tag.valueOf("frame").isEmpty());
        assertTrue(Tag.valueOf("br").isEmpty());
        assertTrue(Tag.valueOf("wbr").isEmpty());
        assertTrue(Tag.valueOf("embed").isEmpty());
        assertTrue(Tag.valueOf("hr").isEmpty());
        assertTrue(Tag.valueOf("area").isEmpty());
        assertTrue(Tag.valueOf("param").isEmpty());
        assertTrue(Tag.valueOf("source").isEmpty());
        assertTrue(Tag.valueOf("track").isEmpty());
        assertTrue(Tag.valueOf("summary").isEmpty());
        assertTrue(Tag.valueOf("command").isEmpty());
        assertTrue(Tag.valueOf("device").isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(Tag.valueOf("p").isSelfClosing());
        assertFalse(Tag.valueOf("div").isSelfClosing());
        assertFalse(Tag.valueOf("span").isSelfClosing());
        assertFalse(Tag.valueOf("img").isSelfClosing());
        assertFalse(Tag.valueOf("input").isSelfClosing());
        assertTrue(Tag.valueOf("img").isSelfClosing());
        assertTrue(Tag.valueOf("input").isSelfClosing());
        assertFalse(Tag.valueOf("input:hidden").isSelfClosing());
        assertFalse(Tag.valueOf("input:file").isSelfClosing());
        assertFalse(Tag.valueOf("img").setSelfClosing().isSelfClosing());
    }

    @Test
    public void testIsKnownTagArray() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("span"));
        assertTrue(Tag.isKnownTag("img"));
        assertTrue(Tag.isKnownTag("input"));
        assertFalse(Tag.isKnownTag(""));
        assertFalse(Tag.isKnownTag(null));
    }

    @Test
    public void testPreserveWhitespace() {
        assertTrue(Tag.valueOf("pre").preserveWhitespace());
        assertFalse(Tag.valueOf("p").preserveWhitespace());
        assertFalse(Tag.valueOf("div").preserveWhitespace());
        assertFalse(Tag.valueOf("span").preserveWhitespace());
        assertFalse(Tag.valueOf("img").preserveWhitespace());
        assertFalse(Tag.valueOf("input").preserveWhitespace());
        assertTrue(Tag.valueOf("title").preserveWhitespace());
    }
}