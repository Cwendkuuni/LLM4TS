package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "newTag";

 @Test
 public void testTagCreation() {
 Tag tag = new Tag(TAG_NAME);
 Tag retrievedTag = Tag.valueOf(TAG_NAME);
 assertEquals(tag.getName(), retrievedTag.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isInline());
 assertTrue(divTag.isBlock());

 assertFalse(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());

 assertFalse(divTag.isEmpty());
 assertTrue(divTag.canContainBlock());

 assertFalse(pTag.isSelfClosing());
 assertFalse(divTag.isKnownTag());

 assertTrue(divTag.formatAsBlock());
 assertFalse(pTag.formatAsBlock());

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));

 assertTrue(pTag.preserveWhitespace());
 assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf(TAG_NAME);

 assertTrue(tag.isData());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 assertEquals(tag1, tag1);
 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag1.hashCode();
 int hashCode3 = tag3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }

 @Test
 public void testTagRegistered() {
 Tag tag = new Tag(TAG_NAME);
 Tag retrievedTag = Tag.valueOf(TAG_NAME);
 assertEquals(tag.getName(), retrievedTag.getName());
 }

 @Test
 public void testBlockTags() {
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testInlineTags() {
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isInline());
 }
 }

 @Test
 public void testEmptyTags() {
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }
 }

 @Test
 public void testFormatAsInlineTags() {
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }
 }

 @Test
 public void testPreserveWhitespaceTags() {
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }
 }
}