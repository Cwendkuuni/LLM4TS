package org.jsoup.parser;

import org.junit.Assert;
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

        Tag htmlTag1 = Tag.valueOf("html");
        Tag pTag1 = Tag.valueOf("p");

        Assert.assertSame(htmlTag, htmlTag1);
        Assert.assertSame(pTag, pTag1);

        Tag unknownTag = Tag.valueOf("unknown");

        Assert.assertNotSame(htmlTag, unknownTag);
        Assert.assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");

        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Assert.assertTrue(htmlTag.isBlock());
        Assert.assertTrue(pTag.isInline());

        Tag divTag = new Tag("div");
        divTag.setBlock(false);

        Assert.assertFalse(divTag.isBlock());
        Assert.assertTrue(divTag.isInline());
    }

    @Test
    public void testFormatAsBlock() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");

        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Assert.assertTrue(htmlTag.formatAsBlock());
        Assert.assertFalse(pTag.formatAsBlock());

        Tag preTag = new Tag("pre");
        preTag.setFormatAsBlock(true);

        Assert.assertTrue(preTag.formatAsBlock());
        Assert.assertFalse(preTag.isBlock());
    }

    @Test
    public void testCanContainInline() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag = new Tag("html");
        Tag pTag = new Tag("p");

        tags.put("html", htmlTag);
        tags.put("p", pTag);

        Assert.assertTrue(htmlTag.canContainInline());
        Assert.assertTrue(pTag.canContainInline());

        Tag divTag = new Tag("div");
        divTag.setCanContainInline(false);

        Assert.assertFalse(divTag.canContainInline());
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

        imgTag.setEmpty(true);

        Assert.assertTrue(imgTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Map<String, Tag> tags = new HashMap<>();
        Tag imgTag = new Tag("img");

        tags.put("img", imgTag);

        Assert.assertFalse(imgTag.isSelfClosing());

        imgTag.setSelfClosing(true);

        Assert.assertTrue(imgTag.isSelfClosing());
    }

    @Test
    public void testIsFormListed() {
        Map<String, Tag> tags = new HashMap<>();
        Tag inputTag = new Tag("input");

        tags.put("input", inputTag);

        Assert.assertFalse(inputTag.isFormListed());

        inputTag.setFormList(true);

        Assert.assertTrue(inputTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Map<String, Tag> tags = new HashMap<>();
        Tag inputTag = new Tag("input");

        tags.put("input", inputTag);

        Assert.assertFalse(inputTag.isFormSubmittable());

        inputTag.setFormSubmit(true);

        Assert.assertTrue(inputTag.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag1 = new Tag("html");
        Tag htmlTag2 = new Tag("html");

        Tag pTag1 = new Tag("p");
        Tag pTag2 = new Tag("p");

        tags.put("html", htmlTag1);
        tags.put("html", htmlTag2);
        tags.put("p", pTag1);
        tags.put("p", pTag2);

        Assert.assertSame(htmlTag1, htmlTag2);
        Assert.assertSame(pTag1, pTag2);

        htmlTag1.setEmpty(true);
        htmlTag2.setEmpty(false);

        Assert.assertNotSame(htmlTag1, htmlTag2);
    }

    @Test
    public void testHashCode() {
        Map<String, Tag> tags = new HashMap<>();
        Tag htmlTag1 = new Tag("html");
        Tag htmlTag2 = new Tag("html");

        Tag pTag1 = new Tag("p");
        Tag pTag2 = new Tag("p");

        tags.put("html", htmlTag1);
        tags.put("html", htmlTag2);
        tags.put("p", pTag1);
        tags.put("p", pTag2);

        Assert.assertEquals(htmlTag1.hashCode(), htmlTag2.hashCode());
        Assert.assertEquals(pTag1.hashCode(), pTag2.hashCode());

        htmlTag1.setEmpty(true);
        htmlTag2.setEmpty(false);

        Assert.assertNotEquals(htmlTag1.hashCode(), htmlTag2.hashCode());
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