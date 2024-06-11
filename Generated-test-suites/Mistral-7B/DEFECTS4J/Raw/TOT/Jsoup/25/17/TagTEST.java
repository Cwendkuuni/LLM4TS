package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.Tag.*;

public class TagTest {

    @Test
    public void testKnownBlockTags() {
        Tag html = valueOf("html");
        Tag p = valueOf("p");

        Assert.assertTrue(html.isKnownTag());
        Assert.assertTrue(p.isKnownTag());
        Assert.assertTrue(html.isBlock());
        Assert.assertTrue(p.isBlock());
    }

    @Test
    public void testUnknownTag() {
        String tagName = "unknown";
        Tag tag = valueOf(tagName);

        Assert.assertFalse(tag.isKnownTag());
        Assert.assertNotNull(tag);
        Assert.assertEquals(tagName.toLowerCase(), tag.getTagName());
    }

    @Test
    public void testEmptyTag() {
        Tag img = valueOf("img");
        img.empty = true;

        Assert.assertTrue(img.isEmpty());
        Assert.assertFalse(img.canContainBlock());
        Assert.assertFalse(img.canContainInline());
        Assert.assertTrue(img.isSelfClosing());
    }

    @Test
    public void testFormatAsInlineTag() {
        Tag p = valueOf("p");
        p.formatAsBlock = false;

        Assert.assertFalse(p.isBlock());
        Assert.assertFalse(p.formatAsBlock());
        Assert.assertTrue(p.isInline());
    }

    @Test
    public void testGenericTag() {
        Tag generic = valueOf("generic");

        Assert.assertFalse(generic.isBlock());
        Assert.assertTrue(generic.canContainBlock());
        Assert.assertFalse(generic.isEmpty());
        Assert.assertFalse(generic.preserveWhitespace());
    }

    @Test
    public void testPreserveWhitespaceTag() {
        Tag pre = valueOf("pre");
        pre.preserveWhitespace = true;

        Assert.assertTrue(pre.isBlock());
        Assert.assertTrue(pre.preserveWhitespace());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagNameThrowsException() {
        Tag tag = new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagNameThrowsException() {
        Tag tag = new Tag("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhiteSpaceTagNameThrowsException() {
        Tag tag = new Tag(" Tag ");
    }
}