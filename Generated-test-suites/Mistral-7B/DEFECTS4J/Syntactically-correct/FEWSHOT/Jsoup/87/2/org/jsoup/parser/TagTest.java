package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreationAndEquality() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("Div");

        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
        Assert.assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isFormSubmittable());
        }
    }

    @Test
    public void testUnknownTag() {
        String unknownTagName = "unknownTag";
        Tag tag = Tag.valueOf(unknownTagName);
        Assert.assertFalse(Tag.isKnownTag(unknownTagName));
        Assert.assertFalse(tag.isKnownTag());
        Assert.assertNotNull(tag);
    }
}