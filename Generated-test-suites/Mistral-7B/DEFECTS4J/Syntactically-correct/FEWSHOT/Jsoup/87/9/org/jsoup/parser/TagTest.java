package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreationAndEquals() {
        Tag html = Tag.valueOf("html");
        Tag html2 = Tag.valueOf("html");

        Tag p = Tag.valueOf("p");
        Tag p2 = new Tag("p");

        Tag unknown = new Tag("unknown");

        Tag equal = Tag.valueOf("p");
        Tag equal2 = new Tag("p");

        Tag notEqual = Tag.valueOf("div");

        Assert.assertEquals(html, html);
        Assert.assertEquals(html, html2);
        Assert.assertEquals(html, Tag.valueOf("HTML"));

        Assert.assertEquals(p, p);
        Assert.assertEquals(p, p2);

        Assert.assertNotEquals(html, p);
        Assert.assertNotEquals(p, html);
        Assert.assertNotEquals(html, unknown);
        Assert.assertNotEquals(p, unknown);

        Assert.assertEquals(p, equal);
        Assert.assertEquals(p, equal2);
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag input = Tag.valueOf("input");

        Assert.assertTrue(html.isBlock());
        Assert.assertTrue(p.isBlock());
        Assert.assertFalse(input.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag input = Tag.valueOf("input");

        Assert.assertFalse(html.isInline());
        Assert.assertFalse(p.isInline());
        Assert.assertTrue(input.isInline());
    }

    @Test
    public void testIsData() {
        Tag p = Tag.valueOf("p");
        Tag script = Tag.valueOf("script");

        Assert.assertFalse(p.isData());
        Assert.assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");
        Tag input = Tag.valueOf("input");

        Assert.assertFalse(img.isEmpty());
        Assert.assertFalse(br.isEmpty());
        Assert.assertFalse(input.isEmpty());

        Tag imgEmpty = new Tag("img").setSelfClosing();
        Assert.assertTrue(imgEmpty.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Tag imgSelfClosing = new Tag("img").setSelfClosing();
        Tag p = Tag.valueOf("p");

        Assert.assertFalse(img.isSelfClosing());
        Assert.assertTrue(imgSelfClosing.isSelfClosing());
        Assert.assertFalse(p.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("html"));
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testCanContainBlock() {
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");

        Assert.assertTrue(div.canContainBlock());
        Assert.assertTrue(div.canContain(p));

        Assert.assertFalse(p.canContainBlock());
    }

    @Test
    public void testCanContainInline() {
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        Assert.assertTrue(div.canContainInline());
        Assert.assertTrue(div.canContain(p));
        Assert.assertTrue(div.canContain(img));

        Assert.assertTrue(p.canContainInline());
        Assert.assertFalse(p.canContainBlock());
        Assert.assertTrue(p.canContain(img));

        Assert.assertFalse(img.canContainBlock());
        Assert.assertFalse(img.canContainInline());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        Tag p = Tag.valueOf("p");

        Assert.assertTrue(pre.preserveWhitespace());
        Assert.assertFalse(p.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag button = Tag.valueOf("button");
        Tag input = Tag.valueOf("input");

        Assert.assertTrue(button.isFormListed());
        Assert.assertTrue(input.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag button = Tag.valueOf("button");
        Tag input = Tag.valueOf("input");

        Assert.assertTrue(button.isFormSubmittable());
        Assert.assertTrue(input.isFormSubmittable());

        Tag select = Tag.valueOf("select");
        Assert.assertTrue(select.isFormSubmittable());
    }
}