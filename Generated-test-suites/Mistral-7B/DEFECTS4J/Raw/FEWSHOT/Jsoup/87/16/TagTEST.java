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
        Tag tag3 = Tag.valueOf("DIV");

        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
    }

    @Test
    public void testBlockTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.isKnownTag(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (entry.getValue().isInline()) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testEmptyTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (entry.getValue().isEmpty()) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testFormatAsInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (entry.getValue().formatAsInline()) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (entry.getValue().preserveWhitespace()) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testFormListedTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (entry.getValue().isFormListed()) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testFormSubmitTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (entry.getValue().isFormSubmittable()) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        Assert.assertEquals(expectedTags, actualTags);
    }
}