package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> expectedTags = new HashMap<String, Tag>() {{
 put("html", new Tag("html"));
 put("head", new Tag("head"));
 put("body", new Tag("body"));
 // Add other pre-defined tags here as needed
 }};

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("div");
 assertEquals("div", tag.getName());
 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknownTag");
 assertNotEquals("div", unknownTag.getName());
 // Test existing tag
 Tag htmlTag = Tag.valueOf("html");
 assertEquals(htmlTag, Tag.valueOf("HTML"));
 }

 @Test
 public void testTagIsBlock() {
 Tag divTag = new Tag("div");
 assertTrue(divTag.isBlock());
 Tag pTag = new Tag("p");
 assertTrue(pTag.isBlock());
 // Test inline tag
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.formatAsBlock());
 Tag brTag = new Tag("br");
 assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag bodyTag = new Tag("body");
 assertTrue(bodyTag.canContainBlock());
 Tag pTag = new Tag("p");
 assertTrue(pTag.canContainBlock());
 // Test empty tag that can't contain block
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag divTag = new Tag("div");
 assertTrue(divTag.canContainInline());
 Tag pTag = new Tag("p");
 assertTrue(pTag.canContainInline());
 // Test empty tag that can't contain inline
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isEmpty());
 // Test empty tag
 Tag brTag = new Tag("br");
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isSelfClosing());
 Tag brTag = new Tag("br");
 assertTrue(brTag.isSelfClosing());
 Tag imgSelfClosingTag = imgTag.setSelfClosing();
 assertTrue(imgSelfClosingTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag htmlTag = new Tag("html");
 assertTrue(Tag.isKnownTag("html"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test(expected = NullPointerException.class)
 public void testTagValueOfWithNull() {
 Tag tag = Tag.valueOf(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithEmptyTrimmed() {
 Tag tag = Tag.valueOf("");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithEmptyNonTrimmed() {
 Tag tag = Tag.valueOf(" ");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithUpperCase() {
 Tag tag = Tag.valueOf("DIV");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testGetRegisteredTags() {
 assertEquals(expectedTags.size(), Tag.tags.size());
 for (Map.Entry<String, Tag> entry : expectedTags.entrySet()) {
 String tagName = entry.getKey();
 Tag expectedTag = entry.getValue();
 Tag actualTag = Tag.tags.get(tagName);
 assertEquals(expectedTag, actualTag);
 }
 }
}