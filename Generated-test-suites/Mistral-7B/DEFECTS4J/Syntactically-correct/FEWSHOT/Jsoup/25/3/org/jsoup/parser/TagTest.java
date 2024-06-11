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
        Assert.assertNotEquals(tag2, tag3);
    }

    @Test
    public void testIsBlock() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isBlock());
        }

        Tag genericTag = Tag.valueOf("generic");
        Assert.assertFalse(genericTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        Assert.assertTrue(pTag.formatAsBlock());

        Tag imgTag = Tag.valueOf("img");
        Assert.assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag divTag = Tag.valueOf("div");
        Assert.assertTrue(divTag.canContainBlock());

        Tag imgTag = Tag.valueOf("img");
        Assert.assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testCanContainInline() {
        Tag pTag = Tag.valueOf("p");
        Assert.assertTrue(pTag.canContainInline());

        Tag scriptTag = Tag.valueOf("script");
        Assert.assertFalse(scriptTag.canContainInline());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        Assert.assertTrue(scriptTag.isData());

        Tag pTag = Tag.valueOf("p");
        Assert.assertFalse(pTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Assert.assertTrue(imgTag.isEmpty());

        Tag pTag = Tag.valueOf("p");
        Assert.assertFalse(pTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Assert.assertTrue(imgTag.isSelfClosing());

        Tag pTag = Tag.valueOf("p");
        Assert.assertFalse(pTag.isSelfClosing());

        Tag selfClosingTag = Tag.valueOf("input").setSelfClosing();
        Assert.assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        for (String tagName : Tag.blockTags) {
            Assert.assertTrue(Tag.isKnownTag(tagName));
        }

        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Assert.assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        Assert.assertFalse(pTag.preserveWhitespace());

        Tag titleTag = Tag.valueOf("title");
        Assert.assertTrue(titleTag.preserveWhitespace());
    }

    @Test
    public void testRegisteredTags() {
        Map<String, Tag> registeredTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            registeredTags.put(entry.getKey(), entry.getValue());
        }

        Assert.assertEquals(Tag.tags.size(), registeredTags.size());
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            Assert.assertEquals(entry.getValue(), registeredTags.get(entry.getKey()));
        }
    }
}