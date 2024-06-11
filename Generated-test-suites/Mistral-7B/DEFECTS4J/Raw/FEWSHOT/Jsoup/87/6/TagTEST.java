package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag html = Tag.valueOf("html");
        Assert.assertTrue(html.isKnownTag());

        Tag unknown = new Tag("unknown");
        Assert.assertFalse(unknown.isKnownTag());
    }

    @Test
    public void testIsKnownTagCaseInsensitive() {
        Tag html = Tag.valueOf("Html");
        Assert.assertTrue(html.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag p = Tag.valueOf("p");
        Assert.assertTrue(p.isBlock());

        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag p = Tag.valueOf("p");
        Assert.assertTrue(p.formatAsBlock());

        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.formatAsBlock());
    }

    @Test
    public void testCanContainInline() {
        Tag p = Tag.valueOf("p");
        Assert.assertTrue(p.canContainInline());

        Tag pcdata = Tag.valueOf("p[data]");
        Assert.assertFalse(pcdata.canContainInline());

        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.canContainInline());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        Assert.assertFalse(img.isEmpty());

        Tag hr = Tag.valueOf("hr");
        Assert.assertTrue(hr.isEmpty());

        Tag input = Tag.valueOf("input");
        Assert.assertFalse(input.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Assert.assertTrue(img.isSelfClosing());

        Tag p = Tag.valueOf("p");
        Assert.assertFalse(p.isSelfClosing());
    }

    @Test
    public void testSetSelfClosing() {
        Tag myTag = new Tag("mytag");
        Tag selfClosingTag = myTag.setSelfClosing();
        Assert.assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsData() {
        Tag script = Tag.valueOf("script");
        Assert.assertTrue(script.isData());

        Tag p = Tag.valueOf("p");
        Assert.assertFalse(p.isData());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        Assert.assertTrue(pre.preserveWhitespace());

        Tag p = Tag.valueOf("p");
        Assert.assertFalse(p.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag input = Tag.valueOf("input");
        Assert.assertTrue(input.isFormListed());

        Tag p = Tag.valueOf("p");
        Assert.assertFalse(p.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag input = Tag.valueOf("input");
        Assert.assertTrue(input.isFormSubmittable());

        Tag p = Tag.valueOf("p");
        Assert.assertFalse(p.isFormSubmittable());
    }
}