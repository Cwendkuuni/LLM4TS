package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testCreateTag() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testCreateEmptyTag() {
        Tag tag = Tag.valueOf("img");
        assertEquals("img", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testCreateBlockTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testCreateInlineTag() {
        Tag tag = Tag.valueOf("input");
        assertEquals("input", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testCreateSelfClosingTag() {
        Tag tag = Tag.valueOf("img");
        tag.setSelfClosing();
        assertEquals("img", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertTrue(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testCreatePreserveWhitespaceTag() {
        Tag tag = Tag.valueOf("pre");
        assertEquals("pre", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testIsBlockTag() {
        assertTrue(Tag.valueOf("p").isBlock());
        assertFalse(Tag.valueOf("input").isBlock());
    }

    @Test
    public void testIsInlineTag() {
        assertFalse(Tag.valueOf("p").isInline());
        assertTrue(Tag.valueOf("input").isInline());
    }

    @Test
    public void testIsDataTag() {
        assertFalse(Tag.valueOf("p").isData());
        assertTrue(Tag.valueOf("script").isData());
    }

    @Test
    public void testIsEmptyTag() {
        assertTrue(Tag.valueOf("img").isEmpty());
        assertFalse(Tag.valueOf("p").isEmpty());
    }

    @Test
    public void testIsSelfClosingTag() {
        Tag tag = Tag.valueOf("img");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTagMap() {
        Map<String, Tag> map = new HashMap<>();
        Tag tag1 = new Tag("tag1");
        tag1.isBlock = true;
        Tag tag2 = new Tag("tag2");
        tag2.isBlock = false;
        map.put("tag1", tag1);
        map.put("tag2", tag2);
        for (Map.Entry<String, Tag> entry : map.entrySet()) {
            String tagName = entry.getKey();
            Tag tag = entry.getValue();
            assertEquals(tagName, tag.getName());
            assertEquals(tag.isBlock(), map.get(tagName).isBlock());
        }
    }

}