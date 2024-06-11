package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagValueOf() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("TestTag");

 assertEquals("p", tag1.getName());
 assertEquals("TestTag", tag2.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.formatAsBlock());
 assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.canContainBlock());
 assertFalse(brTag.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(inputTag.isEmpty());

 Tag metaTag = Tag.valueOf("meta");
 Tag hrTag = Tag.valueOf("hr");

 assertFalse(metaTag.isEmpty());
 assertFalse(hrTag.isEmpty());

 Tag brTagEmpty = new Tag(TAG_NAME);
 brTagEmpty.empty = true;

 assertTrue(brTagEmpty.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(pTag.isSelfClosing());

 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = new Tag("UnknownTag");

 assertTrue(pTag.isKnownTag());
 assertFalse(unknownTag.isKnownTag());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagRegisterMap() {
 Map<String, Tag> tags = new HashMap<>();
 Tag pTag = new Tag("p");
 Tag testTag = new Tag(TAG_NAME);

 tags.put("p", pTag);
 tags.put(TAG_NAME, testTag);

 assertEquals(pTag, Tag.valueOf("p"));
 assertEquals(testTag, Tag.valueOf(TAG_NAME));
 }
}