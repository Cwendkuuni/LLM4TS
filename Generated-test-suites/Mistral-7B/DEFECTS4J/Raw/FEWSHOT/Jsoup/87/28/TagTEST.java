package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Assert.assertTrue(Tag.isKnownTag("html"));
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testValueOf() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Tag htmlTag1 = Tag.valueOf("html");
        Tag pTag1 = Tag.valueOf("p");

        Assert.assertEquals(htmlTag, htmlTag1);
        Assert.assertEquals(pTag, pTag1);

        Tag unknownTag = Tag.valueOf("unknown");
        Assert.assertNotEquals(htmlTag, unknownTag);
        Assert.assertFalse(unknownTag.isKnownTag());
        Assert.assertTrue(unknownTag.equals(unknownTag));
    }

    @Test
    public void testIsBlock() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        Tag divTag = new Tag("div");
        tags.put("html", htmlTag);
        tags.put("p", pTag);
        tags.put("div", divTag);

        Assert.assertTrue(pTag.isBlock());
        Assert.assertTrue(divTag.isBlock());
        Assert.assertTrue(htmlTag.isBlock());

        Tag inputTag = new Tag("input");
        inputTag.setSelfClosing();
        Assert.assertFalse(inputTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        Tag divTag = new Tag("div");
        tags.put("html", htmlTag);
        tags.put("p", pTag);
        tags.put("div", divTag);

        Assert.assertTrue(pTag.formatAsBlock());
        Assert.assertTrue(divTag.formatAsBlock());
        Assert.assertTrue(htmlTag.formatAsBlock());

        Tag imgTag = new Tag("img");
        Assert.assertFalse(imgTag.formatAsBlock());
        Assert.assertFalse(imgTag.isBlock());
    }

    @Test
    public void testCanContainInline() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        Tag divTag = new Tag("div");
        tags.put("html", htmlTag);
        tags.put("p", pTag);
        tags.put("div", divTag);

        Assert.assertTrue(pTag.canContainInline());
        Assert.assertTrue(divTag.canContainInline());
        Assert.assertTrue(htmlTag.canContainInline());

        Tag scriptTag = new Tag("script");
        Assert.assertFalse(scriptTag.canContainInline());
        Assert.assertFalse(scriptTag.isBlock());
    }

    @Test
    public void testIsEmpty() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag imgTag = new Tag("img");
        tags.put("html", htmlTag);
        tags.put("img", imgTag);

        Assert.assertFalse(htmlTag.isEmpty());
        Assert.assertFalse(imgTag.isEmpty());

        Tag metaTag = new Tag("meta");
        metaTag.empty = true;
        Assert.assertTrue(metaTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag imgTag = new Tag("img");
        tags.put("html", htmlTag);
        tags.put("img", imgTag);

        Assert.assertFalse(htmlTag.isSelfClosing());
        Assert.assertTrue(imgTag.isSelfClosing());

        Tag inputTag = new Tag("input");
        inputTag.setSelfClosing();
        Assert.assertTrue(inputTag.isSelfClosing());
    }

    @Test
    public void testPreserveWhitespace() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        Tag preTag = new Tag("pre");
        tags.put("html", htmlTag);
        tags.put("p", pTag);
        tags.put("pre", preTag);

        Assert.assertFalse(pTag.preserveWhitespace());
        Assert.assertTrue(preTag.preserveWhitespace());
        Assert.assertFalse(htmlTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag formTag = new Tag("form");
        tags.put("html", htmlTag);
        tags.put("form", formTag);

        Assert.assertFalse(htmlTag.isFormListed());
        Assert.assertTrue(formTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        tags.put("html", htmlTag);
        tags.put("form", formTag);
        tags.put("input", inputTag);

        Assert.assertFalse(htmlTag.isFormSubmittable());
        Assert.assertTrue(formTag.isFormSubmittable());
        Assert.assertTrue(inputTag.isFormSubmittable());
    }

    @Test
    public void testEqualsAndHashCode() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Tag htmlTag1 = new Tag("html");
        Tag pTag1 = new Tag("p");

        Assert.assertEquals(htmlTag, htmlTag1);
        Assert.assertEquals(pTag, pTag1);

        Tag htmlTag2 = new Tag("HTML");
        Tag pTag2 = new Tag("P");

        Assert.assertNotEquals(htmlTag, htmlTag2);
        Assert.assertNotEquals(pTag, pTag2);

        Assert.assertEquals(htmlTag.hashCode(), htmlTag1.hashCode());
        Assert.assertEquals(pTag.hashCode(), pTag1.hashCode());
        Assert.assertNotEquals(htmlTag.hashCode(), pTag.hashCode());
    }

    @Test
    public void testToString() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");
        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Assert.assertEquals("html", htmlTag.toString());
        Assert.assertEquals("p", pTag.toString());
    }
}