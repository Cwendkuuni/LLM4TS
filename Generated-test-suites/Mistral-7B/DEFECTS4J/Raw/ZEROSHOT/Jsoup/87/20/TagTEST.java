package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for the Tag class.
 */
public class TagTest {

    /**
     * Test method for {@link Tag#valueOf(String)} with a known tag.
     */
    @Test
    public void testKnownTag() {
        Tag tag = Tag.valueOf("p");
        String expectedTagName = "p";
        assertEquals(expectedTagName, tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with an unknown tag.
     */
    @Test
    public void testUnknownTag() {
        Tag tag = Tag.valueOf("unknownTag");
        String expectedTagName = "unknownTag";
        assertEquals(expectedTagName, tag.getName());
        assertFalse(tag.isKnownTag());
    }

    /**
     * Test method for {@link Tag#isBlock()} method.
     */
    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()} method.
     */
    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#isInline()} method.
     */
    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isInline());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isInline());
    }

    /**
     * Test method for {@link Tag#isData()} method.
     */
    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());

        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isData());
    }

    /**
     * Test method for {@link Tag#isEmpty()} method.
     */
    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isSelfClosing());
        assertTrue(inputTag.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()} method.
     */
    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isSelfClosing());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isSelfClosing());
    }

    /**
     * Test method for {@link Tag#isKnownTag(String)} method.
     */
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    /**
     * Test method for {@link Tag#preserveWhitespace()} method.
     */
    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#isFormListed()} method.
     */
    @Test
    public void testIsFormListed() {
        Tag buttonTag = Tag.valueOf("button");
        assertTrue(buttonTag.isFormListed());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormListed());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()} method.
     */
    @Test
    public void testIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormSubmittable());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormSubmittable());
    }
}