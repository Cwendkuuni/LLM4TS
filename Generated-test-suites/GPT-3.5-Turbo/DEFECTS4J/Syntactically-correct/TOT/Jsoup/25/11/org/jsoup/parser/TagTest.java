package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("testTag");
        assertNotNull(tag);
        assertTrue(tag instanceof Tag);
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("blockTag");
        assertEquals(true, blockTag.isBlock());

        Tag inlineTag = new Tag("inlineTag");
        assertEquals(false, inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockFormatTag = new Tag("blockFormatTag");
        assertEquals(true, blockFormatTag.formatAsBlock());

        Tag inlineFormatTag = new Tag("inlineFormatTag");
        assertEquals(false, inlineFormatTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag canContainBlockTag = new Tag("canContainBlockTag");
        assertEquals(true, canContainBlockTag.canContainBlock());

        Tag cannotContainBlockTag = new Tag("cannotContainBlockTag");
        assertEquals(false, cannotContainBlockTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag inlineTag = new Tag("inlineTag");
        assertEquals(true, inlineTag.isInline());

        Tag blockTag = new Tag("blockTag");
        assertEquals(false, blockTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag dataTag = new Tag("dataTag");
        dataTag.canContainInline = false;
        dataTag.empty = false;
        assertEquals(true, dataTag.isData());

        Tag nonDataTag = new Tag("nonDataTag");
        nonDataTag.canContainInline = true;
        nonDataTag.empty = false;
        assertEquals(false, nonDataTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = new Tag("emptyTag");
        emptyTag.empty = true;
        assertEquals(true, emptyTag.isEmpty());

        Tag nonEmptyTag = new Tag("nonEmptyTag");
        nonEmptyTag.empty = false;
        assertEquals(false, nonEmptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag selfClosingTag = new Tag("selfClosingTag");
        selfClosingTag.empty = true;
        assertEquals(true, selfClosingTag.isSelfClosing());

        Tag nonSelfClosingTag = new Tag("nonSelfClosingTag");
        nonSelfClosingTag.empty = false;
        nonSelfClosingTag.selfClosing = false;
        assertEquals(false, nonSelfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag knownTag = new Tag("knownTag");
        Tag.register(knownTag);
        assertEquals(true, knownTag.isKnownTag());

        Tag unknownTag = new Tag("unknownTag");
        assertEquals(false, unknownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagByName() {
        Tag knownTag = new Tag("knownTag");
        Tag.register(knownTag);
        assertEquals(true, Tag.isKnownTag("knownTag"));

        assertEquals(false, Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preserveWhitespaceTag = new Tag("preserveWhitespaceTag");
        preserveWhitespaceTag.preserveWhitespace = true;
        assertEquals(true, preserveWhitespaceTag.preserveWhitespace());

        Tag noPreserveWhitespaceTag = new Tag("noPreserveWhitespaceTag");
        preserveWhitespaceTag.preserveWhitespace = false;
        assertEquals(false, preserveWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag notSelfClosingTag = new Tag("notSelfClosingTag");
        notSelfClosingTag.setSelfClosing();
        assertEquals(true, notSelfClosingTag.isSelfClosing());
    }

}