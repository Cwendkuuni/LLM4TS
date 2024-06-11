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
 // Add other expected tags here
 }};

 @org.junit.Test
 public void testTagValueOfKnownTag() {
 Tag tag = Tag.valueOf("html");
 Assert.assertEquals("html", tag.getName());
 Assert.assertTrue(tag.isKnownTag());
 }

 @org.junit.Test
 public void testTagValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknown");
 Assert.assertFalse(tag.isKnownTag());
 Assert.assertEquals("unknown", tag.getName());
 }

 @org.junit.Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertFalse(imgTag.isBlock());
 }

 @org.junit.Test
 public void testTagFormatAsBlock() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 Assert.assertTrue(preTag.formatAsBlock());
 Assert.assertTrue(pTag.formatAsBlock());
 }

 @org.junit.Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");

 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertTrue(pTag.canContainInline());
 }

 @org.junit.Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag imgTag = Tag.valueOf("img");

 Assert.assertTrue(scriptTag.isData());
 Assert.assertFalse(imgTag.isData());
 }

 @org.junit.Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 Assert.assertFalse(imgTag.isEmpty());
 Assert.assertTrue(brTag.isEmpty());
 }

 @org.junit.Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 Assert.assertFalse(imgTag.isSelfClosing());
 Assert.assertTrue(brTag.isSelfClosing());
 }

 @org.junit.Test
 public void testTagIsKnownTagForGivenName() {
 Tag tag = Tag.valueOf("html");
 Assert.assertTrue(Tag.isKnownTag("html"));
 }

 @org.junit.Test
 public void testTagIsNotKnownTagForGivenName() {
 Tag tag = Tag.valueOf("unknown");
 Assert.assertFalse(Tag.isKnownTag("html"));
 }

 @org.junit.Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @org.junit.Test
 public void testTagEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");

 Assert.assertEquals(pTag1, pTag2);
 }

 @org.junit.Test
 public void testTagHashCode() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals(pTag.hashCode(), pTag.hashCode());
 Assert.assertNotEquals(pTag.hashCode(), divTag.hashCode());
 }

 @org.junit.Test
 public void testTagToString() {
 Tag pTag = Tag.valueOf("p");

 Assert.assertEquals("p", pTag.toString());
 }

 @org.junit.Test
 public void testRegisteredTags() {
 Map<String, Tag> registeredTags = new HashMap<String, Tag>();
 for (String tagName : expectedTags.keySet()) {
 Tag tag = Tag.valueOf(tagName);
 registeredTags.put(tagName, tag);
 }

 Assert.assertEquals(expectedTags, registeredTags);
 }
}