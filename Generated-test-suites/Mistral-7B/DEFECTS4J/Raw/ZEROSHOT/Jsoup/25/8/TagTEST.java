package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals("testTag", tag.getName());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag brTag = Tag.valueOf("br");
 assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testCanContainInline() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainInline());

 Tag scriptTag = Tag.valueOf("script");
 assertFalse(scriptTag.canContainInline());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isEmpty());

 Tag metaTag = Tag.valueOf("meta");
 assertTrue(metaTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertTrue(imgTag.isSelfClosing());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testValueOfWithNullTagName() {
 assertNull(Tag.valueOf(null));
 }

 @Test
 public void testValueOfWithEmptyTagName() {
 assertNotNull(Tag.valueOf(""));
 }

 @Test
 public void testValueOfWithCaseInsensitiveTagName() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 String newTagName = tag.getName().toUpperCase();
 Tag newTag = Tag.valueOf(newTagName);
 assertEquals(tag, newTag);
 }

 @Test
 public void testTagEquality() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertEquals(pTag1, pTag2);
 assertNotEquals(pTag1, divTag);
 }

 @Test
 public void testTagHashCode() {
 Tag pTag = Tag.valueOf("p");
 int hashCode1 = pTag.hashCode();
 Tag pTag2 = Tag.valueOf("p");
 int hashCode2 = pTag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Tag pTag = Tag.valueOf("p");
 assertEquals("p", pTag.toString());
 }

 @Test
 public void testRegisteredTags() {
 Map<String, Tag> expectedTags = new HashMap<String, Tag>() {{
 put("html", Tag.htmlTag);
 put("head", Tag.headTag);
 // ... (add all registered tags here)
 }};
 Map<String, Tag> actualTags = new HashMap<String, Tag>();
 for (String tagName : Tag.tags.keySet()) {
 actualTags.put(tagName, Tag.valueOf(tagName));
 }
 assertEquals(expectedTags, actualTags);
 }
}