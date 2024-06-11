package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> expectedTags = new HashMap<>();

 static {
 // Pre-populate expected map with known tags
 expectedTags.put("html", new Tag("html"));
 expectedTags.put("head", new Tag("head"));
 // ... add other known tags here
 }

 @Test
 public void testTagValues() {
 // Test methods that return the tag value
 Tag tag = Tag.valueOf("div");
 assertEquals("div", tag.getName());
 assertEquals("div", tag.toString());

 tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 assertEquals("p", tag.toString());

 tag = Tag.valueOf("script");
 assertEquals("script", tag.getName());
 assertEquals("script", tag.toString());
 }

 @Test
 public void testTagMethods() {
 // Test methods that return tag properties
 Tag divTag = new Tag("div");
 Tag pTag = new Tag("p");

 assertEquals(true, divTag.isBlock());
 assertEquals(true, divTag.formatAsBlock());
 assertEquals(true, divTag.canContainInline());
 assertFalse(divTag.isEmpty());
 assertFalse(divTag.isSelfClosing());
 assertFalse(divTag.preserveWhitespace());
 assertFalse(divTag.isFormListed());
 assertFalse(divTag.isFormSubmittable());

 assertEquals(false, pTag.isBlock());
 assertEquals(false, pTag.formatAsBlock());
 assertEquals(true, pTag.canContainInline());
 assertFalse(pTag.isEmpty());
 assertFalse(pTag.isSelfClosing());
 assertFalse(pTag.preserveWhitespace());
 assertFalse(pTag.isFormListed());
 assertFalse(pTag.isFormSubmittable());

 // Test equals and hashCode methods
 Tag divTag2 = new Tag("div");
 assertEquals(divTag, divTag2);
 assertEquals(divTag.hashCode(), divTag2.hashCode());

 Tag pTag2 = new Tag("p");
 assertNotEquals(divTag, pTag2);
 assertNotEquals(divTag.hashCode(), pTag2.hashCode());

 Tag divTag3 = new Tag("DIV");
 assertNotEquals(divTag, divTag3);
 assertNotEquals(divTag.hashCode(), divTag3.hashCode());
 }

 @Test
 public void testUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isBlock());
 assertFalse(unknownTag.formatAsBlock());
 assertFalse(unknownTag.canContainInline());
 assertFalse(unknownTag.isEmpty());
 assertFalse(unknownTag.isSelfClosing());
 assertFalse(unknownTag.preserveWhitespace());
 assertFalse(unknownTag.isFormListed());
 assertFalse(unknownTag.isFormSubmittable());
 }

 @Test
 public void testTagRegistration() {
 // Test that all expected tags have been registered
 for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
 Tag tag = Tag.valueOf(entry.getKey());
 assertNotNull(tag);
 }
 }
}