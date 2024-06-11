package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreationAndEquality() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");

        Assert.assertEquals(tag1.getName(), tag2.getName());
        Assert.assertTrue(tag1.equals(tag2));
        Assert.assertTrue(tag1.hashCode() == tag2.hashCode());

        Tag tag3 = new Tag("div");
        Assert.assertFalse(tag1.equals(tag3));
        Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertTrue(Tag.isKnownTag("div"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testblockTags() {
        for (String tag : Tag.blockTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(t.isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tag : Tag.inlineTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertFalse(t.isBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tag : Tag.emptyTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(t.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tag : Tag.formatAsInlineTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertFalse(t.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tag : Tag.preserveWhitespaceTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(t.preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        for (String tag : Tag.formListedTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(t.isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        for (String tag : Tag.formSubmitTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(t.isFormSubmittable());
        }
    }

    @Test
    public void testSelfClosingTag() {
        Tag img = Tag.valueOf("img");
        Tag imgSelfClosing = img.setSelfClosing();
        Assert.assertTrue(imgSelfClosing.isSelfClosing());
    }
}