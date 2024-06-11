package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreationAndEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = Tag.valueOf("p");

        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
    }

    @Test
    public void testTagIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("p");

        Assert.assertTrue(tag1.isBlock());
        Assert.assertFalse(tag2.isBlock());
    }

    @Test
    public void testTagIsInline() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("input");

        Assert.assertFalse(tag1.isInline());
        Assert.assertTrue(tag2.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag tag1 = Tag.valueOf("script");
        Tag tag2 = Tag.valueOf("div");

        Assert.assertTrue(tag1.isData());
        Assert.assertFalse(tag2.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tag1 = Tag.valueOf("img");
        Tag tag2 = Tag.valueOf("input");

        Assert.assertFalse(tag1.isEmpty());
        Assert.assertFalse(tag2.isEmpty());

        Tag tag3 = Tag.valueOf("input");
        tag3.empty = true;
        Assert.assertTrue(tag3.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag1 = Tag.valueOf("img");
        Tag tag2 = Tag.valueOf("input");
        Tag tag3 = Tag.valueOf("input").setSelfClosing();

        Assert.assertFalse(tag1.isSelfClosing());
        Assert.assertFalse(tag2.isSelfClosing());
        Assert.assertTrue(tag3.isSelfClosing());
    }

    @Test
    public void testTagIsKnown() {
        Assert.assertTrue(Tag.isKnownTag("div"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        Tag tag2 = Tag.valueOf("div");

        Assert.assertTrue(tag1.preserveWhitespace());
        Assert.assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag tag1 = Tag.valueOf("input");
        Tag tag2 = Tag.valueOf("div");

        Assert.assertTrue(tag1.isFormListed());
        Assert.assertFalse(tag2.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag tag1 = Tag.valueOf("input[type=text]");
        Tag tag2 = Tag.valueOf("div");

        Assert.assertTrue(tag1.isFormSubmittable());
        Assert.assertFalse(tag2.isFormSubmittable());
    }
}