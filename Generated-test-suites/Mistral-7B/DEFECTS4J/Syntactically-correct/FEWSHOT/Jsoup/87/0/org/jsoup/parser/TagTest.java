package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertEquals(Tag.isKnownTag("p"), true);
        assertEquals(Tag.isKnownTag("div"), true);
        assertEquals(Tag.isKnownTag("unknown"), false);
    }

    @Test
    public void testValueOf() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknown");

        assertEquals(tagP.getName(), "p");
        assertEquals(tagDiv.getName(), "div");
        assertNotEquals(tagP, tagDiv);
        assertEquals(tagUnknown, Tag.valueOf("unknown"));
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag inputTag = Tag.valueOf("input");
        Tag brTag = Tag.valueOf("br");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(inputTag.isBlock());
        assertFalse(brTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag brTag = Tag.valueOf("br");

        assertTrue(pTag.formatAsBlock());
        assertTrue(divTag.formatAsBlock());
        assertFalse(brTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag divTag = Tag.valueOf("div");
        Tag pTag = Tag.valueOf("p");
        Tag brTag = Tag.valueOf("br");

        assertTrue(divTag.canContainBlock());
        assertFalse(pTag.canContainBlock());
        assertFalse(brTag.canContainBlock());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");

        assertFalse(scriptTag.isData());
        assertFalse(divTag.isData());
        assertTrue(imgTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag pTag = Tag.valueOf("p");
        Tag brTag = Tag.valueOf("br");

        assertFalse(imgTag.isEmpty());
        assertFalse(pTag.isEmpty());
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag pTag = Tag.valueOf("p");
        Tag brTag = Tag.valueOf("br");

        assertFalse(imgTag.isSelfClosing());
        assertFalse(pTag.isSelfClosing());
        assertTrue(brTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTagLists() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));

        assertTrue(Tag.isKnownTag("input"));
        assertTrue(Tag.isKnownTag("select"));
        assertFalse(Tag.isKnownTag("unknownInput"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag pTag = Tag.valueOf("p");

        assertTrue(preTag.preserveWhitespace());
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag buttonTag = Tag.valueOf("button");
        Tag inputTag = Tag.valueOf("input");
        Tag textareaTag = Tag.valueOf("textarea");

        assertTrue(buttonTag.isFormListed());
        assertTrue(inputTag.isFormListed());
        assertTrue(textareaTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        Tag buttonTag = Tag.valueOf("button");
        Tag textareaTag = Tag.valueOf("textarea");

        assertFalse(inputTag.isFormSubmittable());
        assertTrue(buttonTag.isFormSubmittable());
        assertTrue(textareaTag.isFormSubmittable());
    }
}