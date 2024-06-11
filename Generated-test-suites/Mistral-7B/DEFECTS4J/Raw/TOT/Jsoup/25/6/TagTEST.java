package org.jsoup.parser.test;

import org.junit.Assert;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Map<String, Tag> expectedTags = new HashMap<String, Tag>() {{
 put("html", new Tag("html"));
 put("head", new Tag("head"));
 put("body", new Tag("body"));
 // Add more expected tags here...
 }};

 @org.junit.Test
 public void testTagValueOf() {
 Tag tag;

 // Test known tags
 tag = Tag.valueOf("html");
 Assert.assertEquals("html", tag.getName());
 Assert.assertEquals(Tag.class.newInstance(), tag); // Check for equality with newly created instance

 // Test unknown tag, which should return new generic tag
 tag = Tag.valueOf("newTag");
 Assert.assertFalse(tag.isKnownTag());
 Assert.assertEquals("newTag", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.canContainBlock());
 }

 @org.junit.Test
 public void testTagIsBlock() {
 Tag tag = Tag.valueOf("p");
 Assert.assertTrue(tag.isBlock());

 tag = Tag.valueOf("br");
 Assert.assertFalse(tag.isBlock());
 }

 @org.junit.Test
 public void testTagCanContainBlock() {
 Tag tag = Tag.valueOf("p");
 Assert.assertTrue(tag.canContainBlock());

 tag = Tag.valueOf("br");
 Assert.assertFalse(tag.canContainBlock());
 }

 @org.junit.Test
 public void testTagIsData() {
 Tag tag = Tag.valueOf("script");
 Assert.assertTrue(tag.isData());

 tag = Tag.valueOf("div");
 Assert.assertFalse(tag.isData());
 }

 @org.junit.Test
 public void testTagIsEmpty() {
 Tag tag = Tag.valueOf("img");
 Assert.assertFalse(tag.isEmpty());

 tag = Tag.valueOf("br");
 Assert.assertTrue(tag.isEmpty());
 }

 @org.junit.Test
 public void testTagIsSelfClosing() {
 Tag tag = Tag.valueOf("img");
 Assert.assertFalse(tag.isSelfClosing());

 tag = Tag.valueOf("br");
 Assert.assertTrue(tag.isSelfClosing());
 }

 @org.junit.Test
 public void testTagIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("html"));
 Assert.assertFalse(Tag.isKnownTag("newTag"));
 }

 @org.junit.Test
 public void testTagPreserveWhitespace() {
 Tag tag = Tag.valueOf("pre");
 Assert.assertTrue(tag.preserveWhitespace());

 tag = Tag.valueOf("div");
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @org.junit.Test
 public void testTagMapContainsExpectedTags() {
 Assert.assertEquals(expectedTags.size(), Tag.tags.size());
 for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
 Assert.assertEquals(entry.getValue(), Tag.tags.get(entry.getKey()));
 }
 }
}