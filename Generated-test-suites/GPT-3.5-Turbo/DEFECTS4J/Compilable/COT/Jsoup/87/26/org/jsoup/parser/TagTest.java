package org.jsoup.parser;

import org.jsoup.parser.ParseSettings;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("a");
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("img");
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("br");
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("input");
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input");
        Assert.assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input");
        Assert.assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("div");
        Tag selfClosingTag = tag.setSelfClosing();
        Assert.assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Assert.assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("div");
        int hashCode = tag.hashCode();
        Assert.assertEquals(100215, hashCode);
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("span");
        Assert.assertEquals("span", tag.toString());
    }
}
