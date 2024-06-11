package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertEquals("p", pTag.getName());
        Assert.assertEquals("div", divTag.getName());
        Assert.assertTrue(pTag.isBlock());
        Assert.assertTrue(divTag.isBlock());
        Assert.assertFalse(pTag.isEmpty());
        Assert.assertFalse(divTag.isEmpty());
    }

    @Test
    public void testUnknownTagCreation() {
        Tag unknownTag = Tag.valueOf("unknownTag");

        Assert.assertEquals("unknownTag", unknownTag.getName());
        Assert.assertFalse(unknownTag.isBlock());
        Assert.assertFalse(unknownTag.isEmpty());
    }

    @Test
    public void testBlockTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Assert.assertEquals(expectedTags, Tag.getTags());
    }

    @Test
    public void testInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Assert.assertEquals(expectedTags, Tag.getTags());
    }

    @Test
    public void testEmptyTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Assert.assertEquals(expectedTags, Tag.getTags());

        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.canContainInline());
            Assert.assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
            Assert.assertFalse(tag.formatAsBlock());
        }

        Assert.assertEquals(expectedTags, Tag.getTags());
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
            Assert.assertTrue(tag.preserveWhitespace());
        }

        Assert.assertEquals(expectedTags, Tag.getTags());
    }

    @Test
    public void testFormListedTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
            Assert.assertTrue(tag.isFormListed());
        }

        Assert.assertEquals(expectedTags, Tag.getTags());
    }

    @Test
    public void testFormSubmitTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
            Assert.assertTrue(tag.isFormSubmittable());
        }

        Assert.assertEquals(expectedTags, Tag.getTags());
    }
}