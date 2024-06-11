package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertFalse(tag.isEmpty());
        Assert.assertFalse(tag.isSelfClosing());
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testUnknownTagCreation() {
        Tag tag = Tag.valueOf("unknown");
        Assert.assertEquals("unknown", tag.getName());
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.canContainBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertFalse(tag.isEmpty());
        Assert.assertFalse(tag.isSelfClosing());
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isBlock());
            Assert.assertTrue(tag.formatAsBlock());
            Assert.assertTrue(tag.canContainBlock());
            Assert.assertTrue(tag.canContainInline());
            Assert.assertFalse(tag.isEmpty());
            Assert.assertFalse(tag.isSelfClosing());
            Assert.assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertFalse(tag.canContainBlock());
            Assert.assertTrue(tag.canContainInline());
            Assert.assertFalse(tag.isEmpty());
            Assert.assertFalse(tag.isSelfClosing());
            Assert.assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertFalse(tag.canContainBlock());
            Assert.assertFalse(tag.canContainInline());
            Assert.assertTrue(tag.isEmpty());
            Assert.assertFalse(tag.isSelfClosing());
            Assert.assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertTrue(tag.canContainBlock());
            Assert.assertTrue(tag.canContainInline());
            Assert.assertFalse(tag.isEmpty());
            Assert.assertFalse(tag.isSelfClosing());
            Assert.assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertTrue(tag.canContainBlock());
            Assert.assertTrue(tag.canContainInline());
            Assert.assertFalse(tag.isEmpty());
            Assert.assertFalse(tag.isSelfClosing());
            Assert.assertTrue(tag.preserveWhitespace());
        }
    }
}