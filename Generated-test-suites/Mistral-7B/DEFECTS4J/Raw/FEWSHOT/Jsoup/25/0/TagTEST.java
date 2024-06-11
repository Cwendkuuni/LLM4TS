package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreationAndEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = new Tag("div");

        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
        Assert.assertFalse(tag1.equals(null));
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(pTag.isBlock());
        Assert.assertTrue(divTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        Assert.assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertTrue(pTag.formatAsBlock());
        Assert.assertTrue(divTag.formatAsBlock());

        Tag brTag = Tag.valueOf("br");
        Assert.assertFalse(brTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag hrTag = Tag.valueOf("hr");

        Assert.assertTrue(pTag.canContainBlock());
        Assert.assertTrue(divTag.canContainBlock());
        Assert.assertFalse(hrTag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag hrTag = Tag.valueOf("hr");
        Tag metaTag = Tag.valueOf("meta");

        Assert.assertFalse(imgTag.isEmpty());
        Assert.assertFalse(hrTag.isEmpty());
        Assert.assertTrue(metaTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");
        Tag metaTag = Tag.valueOf("meta");

        Assert.assertTrue(imgTag.isSelfClosing());
        Assert.assertTrue(brTag.isSelfClosing());
        Assert.assertFalse(metaTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertTrue(Tag.isKnownTag("div"));
        Assert.assertFalse(Tag.isKnownTag("UnknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag pTag = Tag.valueOf("p");

        Assert.assertTrue(preTag.preserveWhitespace());
        Assert.assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testGetTagByName() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertEquals(pTag, Tag.valueOf("p"));
        Assert.assertEquals(divTag, Tag.valueOf("div"));

        Tag unknownTag = Tag.valueOf("unknown");
        Assert.assertNotEquals(pTag, unknownTag);
        Assert.assertNotEquals(divTag, unknownTag);
    }
}