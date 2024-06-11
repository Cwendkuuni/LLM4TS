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
    }

    @Test
    public void testTagValueOfWithNormalizedName() {
        Tag pTag = Tag.valueOf("p", ParseSettings.normalizeTag);
        Tag PTag = Tag.valueOf("P", ParseSettings.normalizeTag);

        Assert.assertEquals(pTag, PTag);
        Assert.assertTrue(pTag.isBlock());
    }

    @Test
    public void testTagValueOfWithUnknownName() {
        Tag unknownTag = Tag.valueOf("unknown");

        Assert.assertNotEquals("unknown", unknownTag.getName());
        Assert.assertFalse(unknownTag.isBlock());
    }

    @Test
    public void testTagIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testTagIsBlockTag() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");

        Assert.assertTrue(pTag.isBlock());
        Assert.assertTrue(divTag.isBlock());
        Assert.assertFalse(imgTag.isBlock());
    }

    @Test
    public void testTagIsInlineTag() {
        Tag spanTag = Tag.valueOf("span");
        Tag imgTag = Tag.valueOf("img");

        Assert.assertFalse(spanTag.isInline());
        Assert.assertTrue(imgTag.isInline());
    }

    @Test
    public void testTagIsDataTag() {
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertTrue(scriptTag.isData());
    }

    @Test
    public void testTagIsEmptyTag() {
        Tag imgTag = Tag.valueOf("img");

        Assert.assertFalse(imgTag.isEmpty());

        Tag inputTag = Tag.valueOf("input");
        Tag imgTagEmpty = Tag.valueOf("img", ParseSettings.emptyElement);

        Assert.assertFalse(inputTag.isEmpty());
        Assert.assertTrue(imgTagEmpty.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag imgTagSelfClosing = imgTag.setSelfClosing();

        Assert.assertFalse(imgTag.isSelfClosing());
        Assert.assertTrue(imgTagSelfClosing.isSelfClosing());
    }

    @Test
    public void testTagIsKnownEmptyTag() {
        Tag imgTag = Tag.valueOf("img");
        Tag metaTag = Tag.valueOf("meta");

        Assert.assertFalse(imgTag.isEmpty());
        Assert.assertFalse(metaTag.isEmpty());
        Assert.assertTrue(Tag.isKnownTag("img"));
        Assert.assertTrue(Tag.isKnownTag("meta"));
        Assert.assertTrue(Tag.isKnownTag("img").isEmpty());
        Assert.assertFalse(Tag.isKnownTag("meta").isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag pTag = Tag.valueOf("p");

        Assert.assertTrue(preTag.preserveWhitespace());
        Assert.assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag inputTag = Tag.valueOf("input");
        Tag textareaTag = Tag.valueOf("textarea");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(inputTag.isFormListed());
        Assert.assertTrue(textareaTag.isFormListed());
        Assert.assertFalse(divTag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        Tag textareaTag = Tag.valueOf("textarea");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(inputTag.isFormSubmittable());
        Assert.assertTrue(textareaTag.isFormSubmittable());
        Assert.assertFalse(divTag.isFormSubmittable());
    }
}