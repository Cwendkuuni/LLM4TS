package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testBlockTags() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");

        Assert.assertTrue(html.isBlock());
        Assert.assertTrue(html.formatAsBlock());
        Assert.assertTrue(p.isBlock());
        Assert.assertTrue(p.formatAsBlock());

        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isBlock());
            Assert.assertTrue(tag.formatAsBlock());
        }
    }

    @Test
    public void testInlineTags() {
        Tag a = Tag.valueOf("a");
        Tag input = Tag.valueOf("input");

        Assert.assertFalse(a.isBlock());
        Assert.assertFalse(a.formatAsBlock());
        Assert.assertFalse(input.isBlock());
        Assert.assertFalse(input.formatAsBlock());

        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");

        Assert.assertFalse(img.isBlock());
        Assert.assertFalse(img.formatAsBlock());
        Assert.assertFalse(img.canContainInline);
        Assert.assertTrue(img.empty);

        Assert.assertFalse(input.isBlock());
        Assert.assertFalse(input.formatAsBlock());
        Assert.assertTrue(input.canContainInline);
        Assert.assertFalse(input.empty);

        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertTrue(tag.canContainInline);
            Assert.assertTrue(tag.empty);
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        Tag pre = Tag.valueOf("pre");
        Tag p = Tag.valueOf("p");

        Assert.assertTrue(pre.formatAsBlock());
        Assert.assertFalse(p.formatAsBlock());

        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Tag pre = Tag.valueOf("pre");

        Assert.assertTrue(pre.preserveWhitespace());

        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        Tag button = Tag.valueOf("button");
        Tag textarea = Tag.valueOf("textarea");

        Assert.assertTrue(button.isFormListed());
        Assert.assertTrue(textarea.isFormListed());

        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        Tag input = Tag.valueOf("input");
        Tag textarea = Tag.valueOf("textarea");

        Assert.assertTrue(input.isFormSubmittable());
        Assert.assertTrue(textarea.isFormSubmittable());

        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isFormSubmittable());
        }
    }
}