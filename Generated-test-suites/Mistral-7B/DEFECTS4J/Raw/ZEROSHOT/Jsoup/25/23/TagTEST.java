package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit 4 test file for the Tag class.
 */
public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagRegistration() {
 Tag tag = new Tag(TAG_NAME);
 Tag registeredTag = Tag.valueOf(TAG_NAME);
 assertEquals(tag, registeredTag);
 }

 @Test
 public void testTagKnown() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());
 Tag spanTag = Tag.valueOf("span");
 assertFalse(spanTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 assertTrue(divTag.canContainBlock());
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainInline());
 Tag scriptTag = Tag.valueOf("script");
 assertFalse(scriptTag.canContainInline());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());
 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isEmpty());
 Tag metaTag = Tag.valueOf("meta");
 assertTrue(metaTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());
 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isKnownTag());
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isKnownTag());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("tag1");
 Tag tag2 = new Tag("tag1");
 Tag tag3 = new Tag("tag2");
 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2)); // same tagName should be equal
 assertFalse(tag1.equals(tag3)); // different tagName should not be equal
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("tag1");
 Tag tag2 = new Tag("tag1");
 Tag tag3 = new Tag("tag2");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertEquals(hashCode1, hashCode2); // same tagName should have same hashCode
 assertNotSame(hashCode1, hashCode3); // different tagName should have different hashCode
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("tag");
 assertEquals("tag", tag.toString());
 }
}