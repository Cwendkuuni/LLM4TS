package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("p");

        assertEquals("div", tag1.getName());
        assertEquals("p", tag2.getName());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("span");

        assertTrue(blockTag.isBlock());
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("span");

        assertTrue(blockTag.formatAsBlock());
        assertTrue(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("span");

        assertTrue(blockTag.canContainBlock());
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("span");

        assertFalse(blockTag.isInline());
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("span");
        Tag dataOnlyTag = Tag.valueOf("img");

        assertFalse(blockTag.isData());
        assertFalse(inlineTag.isData());
        assertTrue(dataOnlyTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag nonEmptyTag = Tag.valueOf("p");
        Tag emptyTag = Tag.valueOf("img");

        assertFalse(nonEmptyTag.isEmpty());
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag nonSelfClosingTag = Tag.valueOf("div");
        Tag selfClosingTag = Tag.valueOf("img");

        assertFalse(nonSelfClosingTag.isSelfClosing());
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("custom"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        Tag tag2 = Tag.valueOf("span");

        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }

}