package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag emptyTag = Tag.valueOf("meta");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("meta", emptyTag.getName());
    }

    @Test
    public void testBlockTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (tag.isBlock()) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }

    @Test
    public void testInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (!tag.isBlock()) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }

    @Test
    public void testEmptyTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (tag.isEmpty()) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (tag.formatAsBlock() == false) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (tag.preserveWhitespace()) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }

    @Test
    public void testFormListedTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (tag.isFormListed()) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }

    @Test
    public void testFormSubmitTags() {
        Map<String, Tag> expectedTags = new HashMap<>();
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Tag tag : Tag.values()) {
            if (tag.isFormSubmittable()) {
                actualTags.put(tag.getName(), tag);
            }
        }

        assertEquals(expectedTags.size(), actualTags.size());
        for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
            Tag expectedTag = entry.getValue();
            Tag actualTag = actualTags.get(entry.getKey());
            assertEquals(expectedTag, actualTag);
        }
    }
}