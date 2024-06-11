package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.Tag.*;

public class TagTest {

    @Test
    public void testKnownTagNames() {
        Assert.assertTrue(isKnownTag("html"));
        Assert.assertTrue(isKnownTag("p"));
        Assert.assertTrue(isKnownTag("div"));
        Assert.assertFalse(isKnownTag("unknown"));
    }

    @Test
    public void testKnownTags() {
        Tag html = valueOf("html");
        Tag p = valueOf("p");
        Tag div = valueOf("div");

        Assert.assertEquals("html", html.getName());
        Assert.assertEquals("html", html.toString());
        Assert.assertTrue(html.isBlock());
        Assert.assertTrue(html.formatAsBlock());
        Assert.assertTrue(html.canContainBlock());
        Assert.assertFalse(html.isEmpty());
        Assert.assertFalse(html.isInline());
        Assert.assertFalse(html.isData());
        Assert.assertFalse(html.isSelfClosing());
        Assert.assertFalse(html.preserveWhitespace());
        Assert.assertFalse(html.isFormListed());
        Assert.assertFalse(html.isFormSubmittable());

        Assert.assertEquals("p", p.getName());
        Assert.assertEquals("p", p.toString());
        Assert.assertFalse(p.isBlock());
        Assert.assertFalse(p.formatAsBlock());
        Assert.assertFalse(p.canContainBlock());
        Assert.assertFalse(p.isEmpty());
        Assert.assertTrue(p.isInline());
        Assert.assertFalse(p.isData());
        Assert.assertFalse(p.isSelfClosing());
        Assert.assertFalse(p.preserveWhitespace());
        Assert.assertFalse(p.isFormListed());
        Assert.assertFalse(p.isFormSubmittable());

        Assert.assertEquals("div", div.getName());
        Assert.assertEquals("div", div.toString());
        Assert.assertTrue(div.isBlock());
        Assert.assertTrue(div.formatAsBlock());
        Assert.assertTrue(div.canContainBlock());
        Assert.assertFalse(div.isEmpty());
        Assert.assertTrue(div.isInline());
        Assert.assertFalse(div.isData());
        Assert.assertFalse(div.isSelfClosing());
        Assert.assertFalse(div.preserveWhitespace());
        Assert.assertFalse(div.isFormListed());
        Assert.assertFalse(div.isFormSubmittable());
    }

    @Test
    public void testUnknownTag() {
        Tag unknown = valueOf("unknown");

        Assert.assertEquals("unknown", unknown.getName());
        Assert.assertEquals("unknown", unknown.toString());
        Assert.assertFalse(unknown.isBlock());
        Assert.assertFalse(unknown.formatAsBlock());
        Assert.assertFalse(unknown.canContainBlock());
        Assert.assertFalse(unknown.isEmpty());
        Assert.assertFalse(unknown.isInline());
        Assert.assertFalse(unknown.isData());
        Assert.assertFalse(unknown.isSelfClosing());
        Assert.assertFalse(unknown.preserveWhitespace());
        Assert.assertFalse(unknown.isFormListed());
        Assert.assertFalse(unknown.isFormSubmittable());
        Assert.assertNotEquals(unknown, html);
    }

    @Test
    public void testIsBlock() {
        Tag html = valueOf("html");
        Tag p = valueOf("p");
        Tag div = valueOf("div");

        Assert.assertTrue(html.isBlock());
        Assert.assertFalse(p.isBlock());
        Assert.assertTrue(div.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag html = valueOf("html");
        Tag p = valueOf("p");
        Tag div = valueOf("div");

        Assert.assertTrue(html.formatAsBlock());
        Assert.assertFalse(p.formatAsBlock());
        Assert.assertTrue(div.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag html = valueOf("html");
        Tag p = valueOf("p");
        Tag div = valueOf("div");

        Assert.assertTrue(html.canContainBlock());
        Assert.assertFalse(p.canContainBlock());
        Assert.assertTrue(div.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag img = valueOf("img");
        Tag input = valueOf("input");
        Tag br = valueOf("br");

        Assert.assertFalse(img.isEmpty());
        Assert.assertFalse(input.isEmpty());
        Assert.assertTrue(br.isEmpty());
    }

    @Test
    public void testIsInline() {
        Tag p = valueOf("p");
        Tag input = valueOf("input");

        Assert.assertFalse(p.isInline());
        Assert.assertTrue(input.isInline());
    }

    @Test
    public void testIsData() {
        Tag script = valueOf("script");
        Tag textarea = valueOf("textarea");

        Assert.assertTrue(script.isData());
        Assert.assertTrue(textarea.isData());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = valueOf("img");
        Tag selfClosingTag = new Tag("selfClosingTag");
        selfClosingTag.selfClosing = true;

        Assert.assertFalse(img.isSelfClosing());
        Assert.assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = valueOf("pre");
        Tag div = valueOf("div");

        Assert.assertTrue(pre.preserveWhitespace());
        Assert.assertFalse(div.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag form = valueOf("form");
        Tag input = valueOf("input");

        Assert.assertTrue(form.isFormListed());
        Assert.assertFalse(input.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag input = valueOf("input");

        Assert.assertTrue(input.isFormSubmittable());
    }
}