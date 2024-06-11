package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

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
    }

    @Test
    public void testTagEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertEquals(pTag1, pTag2);
        Assert.assertNotEquals(pTag1, divTag);
    }

    @Test
    public void testTagHashCode() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertNotEquals(pTag.hashCode(), divTag.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("p");
        Assert.assertEquals("p", pTag.toString());
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertFalse(pTag.isData());
        Assert.assertTrue(scriptTag.isData());
    }

    @Test
    public void testTagIsKnownTag() {
        Tag pTag = Tag.valueOf("p");
        Tag unknownTag = Tag.valueOf("unknown");

        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testTagIsFormListed() {
        Tag inputTag = Tag.valueOf("input");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(inputTag.isFormListed());
        Assert.assertFalse(divTag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(inputTag.isFormSubmittable());
        Assert.assertFalse(divTag.isFormSubmittable());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag divTag = Tag.valueOf("div");
        Tag pTag = Tag.valueOf("p");

        Assert.assertTrue(divTag.canContainBlock());
        Assert.assertFalse(pTag.canContainBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(pTag.canContainInline());
        Assert.assertTrue(divTag.canContainInline());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag hrTag = Tag.valueOf("hr");

        Assert.assertFalse(imgTag.isEmpty());
        Assert.assertTrue(hrTag.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag pTag = Tag.valueOf("p");

        Assert.assertFalse(imgTag.isSelfClosing());
        Assert.assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag pTag = Tag.valueOf("p");

        Assert.assertTrue(preTag.preserveWhitespace());
        Assert.assertFalse(pTag.preserveWhitespace());
    }
}