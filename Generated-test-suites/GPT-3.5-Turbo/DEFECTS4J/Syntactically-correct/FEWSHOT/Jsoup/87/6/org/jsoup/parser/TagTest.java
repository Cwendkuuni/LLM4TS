package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Assert.assertEquals("p", tag1.getName());
        Assert.assertEquals("div", tag2.getName());

        ParseSettings settings = new ParseSettings(true);
        Tag tag3 = Tag.valueOf("P", settings);
        Tag tag4 = Tag.valueOf("DIV", settings);
        Assert.assertEquals("p", tag3.getName());
        Assert.assertEquals("div", tag4.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        Assert.assertTrue(tag1.isBlock());
        Assert.assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        Assert.assertTrue(tag1.formatAsBlock());
        Assert.assertTrue(tag2.formatAsBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        Assert.assertFalse(tag1.isInline());
        Assert.assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("img");
        Assert.assertFalse(tag1.isData());
        Assert.assertFalse(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        Assert.assertFalse(tag1.isEmpty());
        Assert.assertFalse(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        Assert.assertFalse(tag1.isSelfClosing());
        Assert.assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("img");
        Assert.assertTrue(tag1.isKnownTag());
        Assert.assertTrue(tag2.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertTrue(Tag.isKnownTag("img"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("textarea");
        Assert.assertFalse(tag1.preserveWhitespace());
        Assert.assertTrue(tag2.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("input");
        Assert.assertFalse(tag1.isFormListed());
        Assert.assertTrue(tag2.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("input");
        Assert.assertFalse(tag1.isFormSubmittable());
        Assert.assertTrue(tag2.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("img");
        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.toString());
    }
}