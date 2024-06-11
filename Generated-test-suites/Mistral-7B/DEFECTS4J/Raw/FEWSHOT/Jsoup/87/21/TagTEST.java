package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testBlockTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("html"));
        Tag html = Tag.valueOf("html", ParseSettings.preserveCase);
        Assert.assertTrue(html.isBlock());
        Assert.assertTrue(html.formatAsBlock());
        Assert.assertEquals("html", html.getName());

        Tag div = Tag.valueOf("div");
        Assert.assertTrue(div.isBlock());
        Assert.assertTrue(div.formatAsBlock());
        Assert.assertEquals("div", div.getName());
    }

    @Test
    public void testInlineTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("img"));
        Tag img = Tag.valueOf("img");
        Assert.assertFalse(img.isBlock());
        Assert.assertFalse(img.formatAsBlock());
        Assert.assertEquals("img", img.getName());

        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.isBlock());
        Assert.assertFalse(input.formatAsBlock());
        Assert.assertEquals("input", input.getName());
    }

    @Test
    public void testEmptyTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("img"));
        Tag img = Tag.valueOf("img");
        Assert.assertFalse(img.isBlock());
        Assert.assertFalse(img.formatAsBlock());
        Assert.assertEquals("img", img.getName());

        Tag imgEmpty = Tag.valueOf("img", ParseSettings.empty);
        Assert.assertFalse(imgEmpty.isBlock());
        Assert.assertFalse(imgEmpty.formatAsBlock());
        Assert.assertEquals("img", imgEmpty.getName());
        Assert.assertTrue(imgEmpty.isEmpty());
    }

    @Test
    public void testFormatAsInlineTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("p"));
        Tag p = Tag.valueOf("p");
        Assert.assertTrue(p.isBlock());
        Assert.assertTrue(p.formatAsBlock());
        Assert.assertEquals("p", p.getName());

        Tag pre = Tag.valueOf("pre");
        Assert.assertTrue(pre.isBlock());
        Assert.assertTrue(pre.formatAsBlock());
        Assert.assertEquals("pre", pre.getName());

        Tag preAsInline = pre.setFormatAsBlock(false);
        Assert.assertTrue(preAsInline.isBlock());
        Assert.assertFalse(preAsInline.formatAsBlock());
        Assert.assertEquals("pre", preAsInline.getName());
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("pre"));
        Tag pre = Tag.valueOf("pre");
        Assert.assertTrue(pre.isBlock());
        Assert.assertTrue(pre.formatAsBlock());
        Assert.assertEquals("pre", pre.getName());

        Tag prePreserveWhitespace = pre.setPreserveWhitespace(true);
        Assert.assertTrue(prePreserveWhitespace.isBlock());
        Assert.assertTrue(prePreserveWhitespace.formatAsBlock());
        Assert.assertEquals("pre", prePreserveWhitespace.getName());
        Assert.assertTrue(prePreserveWhitespace.preserveWhitespace());
    }

    @Test
    public void testFormListedTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("input"));
        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.isBlock());
        Assert.assertFalse(input.formatAsBlock());
        Assert.assertEquals("input", input.getName());

        Tag inputFormListed = input.setFormList(true);
        Assert.assertFalse(inputFormListed.isBlock());
        Assert.assertFalse(inputFormListed.formatAsBlock());
        Assert.assertEquals("input", inputFormListed.getName());
        Assert.assertTrue(inputFormListed.isFormListed());
    }

    @Test
    public void testFormSubmittableTags() {
        Map<String, Tag> tags = new HashMap<>();
        Tag.register(Tag.valueOf("input"));
        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.isBlock());
        Assert.assertFalse(input.formatAsBlock());
        Assert.assertEquals("input", input.getName());

        Tag inputFormSubmittable = input.setFormSubmittable(true);
        Assert.assertFalse(inputFormSubmittable.isBlock());
        Assert.assertFalse(inputFormSubmittable.formatAsBlock());
        Assert.assertEquals("input", inputFormSubmittable.getName());
        Assert.assertTrue(inputFormSubmittable.isFormSubmittable());
    }
}