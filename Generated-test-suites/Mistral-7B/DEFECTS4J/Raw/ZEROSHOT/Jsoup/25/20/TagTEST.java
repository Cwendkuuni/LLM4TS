package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test file for Tag class.
 */
public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("unknown");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertNotSame(pTag, divTag);
        assertTrue(pTag.isInline());
        assertTrue(divTag.isBlock());

        assertEquals(pTag, Tag.valueOf("P"));
        assertEquals(pTag, Tag.valueOf("p   "));
        assertEquals(pTag, Tag.valueOf("p\t\n "));

        assertEquals(unknownTag, Tag.valueOf("unknown"));
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.isInline());
        assertFalse(unknownTag.isEmpty());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.formatAsBlock());
        assertFalse(unknownTag.preserveWhitespace());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isKnownTag());

        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsBlockTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(spanTag.isBlock());

        Tag pTagEmpty = Tag.valueOf("p").setEmpty();
        assertFalse(pTagEmpty.isBlock());

        Tag pTagSelfClosing = Tag.valueOf("p").setSelfClosing();
        assertFalse(pTagSelfClosing.isBlock());
    }

    @Test
    public void testIsFormatAsBlockTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        assertTrue(pTag.formatAsBlock());
        assertTrue(divTag.formatAsBlock());
        assertFalse(spanTag.formatAsBlock());

        Tag pTagInline = Tag.valueOf("p").setFormatAsInline();
        assertFalse(pTagInline.formatAsBlock());

        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());
        assertTrue(preTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlockTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");
        Tag formTag = Tag.valueOf("form");

        assertTrue(divTag.canContainBlock());
        assertTrue(formTag.canContainBlock());
        assertFalse(pTag.canContainBlock());
        assertFalse(spanTag.canContainBlock());

        Tag pTagNesting = Tag.valueOf("p").setCanContainBlock(true);
        assertTrue(pTagNesting.canContainBlock());

        Tag divTagNesting = Tag.valueOf("div").setCanContainBlock(false);
        assertFalse(divTagNesting.canContainBlock());
    }

    @Test
    public void testCanContainInlineTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        assertTrue(spanTag.canContainInline());
        assertFalse(pTag.canContainInline());
        assertFalse(divTag.canContainInline());

        Tag spanTagData = Tag.valueOf("span").setCanContainInline(false);
        assertFalse(spanTagData.canContainInline());
    }

    @Test
    public void testIsEmptyTag() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");
        Tag brTag = Tag.valueOf("br");

        assertFalse(imgTag.isEmpty());
        assertFalse(inputTag.isEmpty());
        assertTrue(brTag.isEmpty());

        Tag imgTagEmpty = Tag.valueOf("img").setEmpty();
        assertTrue(imgTagEmpty.isEmpty());
    }

    @Test
    public void testIsSelfClosingTag() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");
        Tag selfClosingTag = Tag.valueOf("img").setSelfClosing(true);

        assertFalse(imgTag.isSelfClosing());
        assertFalse(inputTag.isSelfClosing());
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEqualsTag() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("unknown");

        assertTrue(pTag1.equals(pTag2));
        assertFalse(pTag1.equals(divTag));
        assertFalse(pTag1.equals(unknownTag));

        Tag pTag3 = new Tag("P");
        assertFalse(pTag1.equals(pTag3));
        assertFalse(pTag3.equals(pTag1));
    }

    @Test
    public void testHashCodeTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        int pTagHashCode = pTag.hashCode();
        int divTagHashCode = divTag.hashCode();
        int spanTagHashCode = spanTag.hashCode();

        Tag pTag2 = Tag.valueOf("p");
        assertEquals(pTagHashCode, pTag2.hashCode());

        Tag divTag2 = Tag.valueOf("div");
        assertEquals(divTagHashCode, divTag2.hashCode());

        Tag spanTag2 = Tag.valueOf("span");
        assertEquals(spanTagHashCode, spanTag2.hashCode());

        Tag pTag3 = new Tag("P");
        assertNotEquals(pTagHashCode, pTag3.hashCode());
        assertNotEquals(pTag3.hashCode(), pTagHashCode);
    }

    @Test
    public void testToStringTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        assertEquals("p", pTag.toString());
        assertEquals("div", divTag.toString());
        assertEquals("span", spanTag.toString());
    }
}