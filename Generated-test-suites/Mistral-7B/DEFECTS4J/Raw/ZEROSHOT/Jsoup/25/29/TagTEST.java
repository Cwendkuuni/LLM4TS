package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Tag expectedTag = new Tag(TAG_NAME);

 assertEquals(expectedTag.getName(), tag.getName());
 assertEquals(expectedTag.isBlock(), tag.isBlock());
 assertEquals(expectedTag.formatAsBlock(), tag.formatAsBlock());
 assertEquals(expectedTag.canContainBlock(), tag.canContainBlock());
 assertEquals(expectedTag.canContainInline(), tag.canContainInline());
 assertEquals(expectedTag.isEmpty(), tag.isEmpty());
 assertEquals(expectedTag.selfClosing(), tag.selfClosing());
 assertEquals(expectedTag.preserveWhitespace(), tag.preserveWhitespace());
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.formatAsBlock());
 assertFalse(spanTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");

 assertTrue(divTag.canContainBlock());
 assertTrue(divTag.canContainInline());
 assertTrue(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(inputTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isSelfClosing());
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = new Tag("unknownTag");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagMap() {
 Map<String, Tag> tagMap = new HashMap<>();

 Tag pTag = Tag.valueOf("p");
 tagMap.put("p", pTag);

 Tag divTag = Tag.valueOf("div");
 tagMap.put("div", divTag);

 Tag testTag = new Tag(TAG_NAME);
 tagMap.put(TAG_NAME, testTag);

 Tag retrievedPTag = tagMap.get("p");
 Tag retrievedDivTag = tagMap.get("div");
 Tag retrievedTestTag = tagMap.get(TAG_NAME);

 assertEquals(pTag, retrievedPTag);
 assertEquals(divTag, retrievedDivTag);
 assertEquals(testTag, retrievedTestTag);
 }

 @Test(expected = NullPointerException.class)
 public void testTagNullName() {
 new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagEmptyName() {
 new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagInvalidTagName() {
 new Tag("&lt;script&gt;");
 }
}