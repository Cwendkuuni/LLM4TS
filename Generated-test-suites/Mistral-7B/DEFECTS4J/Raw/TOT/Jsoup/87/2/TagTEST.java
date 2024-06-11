package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagValues() {
        Map<String, Tag> expectedTags = new HashMap<>();

        // pre-defined tags
        expectedTags.put("html", new Tag("html"));
        expectedTags.put("head", new Tag("head"));
        expectedTags.put("body", new Tag("body"));
        // ... add others from the blockTags array

        // test getting tag by name
        Tag tag = Tag.valueOf("html");
        Assert.assertEquals("html", tag.getName());
        Assert.assertEquals(expectedTags.get("html"), tag);

        tag = Tag.valueOf("div");
        Assert.assertNotNull(tag);
        Assert.assertEquals("div", tag.getName());

        // test getting tag by normalize name
        tag = Tag.valueOf("DIV");
        Assert.assertNotNull(tag);
        Assert.assertEquals("div", tag.getName());

        // test unknown tag
        tag = Tag.valueOf("UNKNOWN");
        Assert.assertNotNull(tag);
        Assert.assertFalse(tag.isKnownTag());

        // test case sensitivity
        tag = Tag.valueOf("DIV");
        Assert.assertEquals("div", tag.getName());
        Assert.assertFalse(tag.equals("div")); // different object
        Assert.assertFalse(tag.equals("DIV")); // case sensitivity
    }

    @Test
    public void testTagProperties() {
        Tag tag = Tag.valueOf("p");

        // test basic properties
        Assert.assertEquals("p", tag.getName());
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertFalse(tag.isEmpty());
        Assert.assertFalse(tag.isSelfClosing());
        Assert.assertFalse(tag.preserveWhitespace());
        Assert.assertFalse(tag.isFormListed());
        Assert.assertFalse(tag.isFormSubmittable());

        // test setSelfClosing
        tag = tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());

        // test property changes
        tag.isBlock = false;
        Assert.assertFalse(tag.isBlock());
        tag.formatAsBlock = false;
        Assert.assertFalse(tag.formatAsBlock());
        tag.canContainInline = false;
        Assert.assertFalse(tag.canContainInline());
        tag.empty = true;
        Assert.assertTrue(tag.isEmpty());
        tag.preserveWhitespace = true;
        Assert.assertTrue(tag.preserveWhitespace());
        tag.formList = true;
        Assert.assertTrue(tag.isFormListed());
        tag.formSubmit = true;
        Assert.assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testTagEquality() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");

        Assert.assertEquals(tag1, tag2);
        Assert.assertTrue(tag1.equals(tag2));

        Tag tag3 = new Tag("DIV");
        Assert.assertFalse(tag1.equals(tag3));
        Assert.assertNotEquals(tag1, tag3);
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");

        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("DIV");
        Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.toString());
    }
}