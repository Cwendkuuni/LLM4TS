package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Tag expectedTag = new Tag(TAG_NAME);
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagIsBlock() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainInline());
 }

 @Test
 public void testTagEmpty() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testTagSelfClosing() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testTagSetSelfClosing() {
 Tag tag = new Tag(TAG_NAME);
 Tag selfClosingTag = tag.setSelfClosing();
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test(expected = NullPointerException.class)
 public void testTagValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testTagValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 Tag expectedTag = new Tag("");
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagValueOfLowerCaseTagName() {
 String tagName = "TestTag";
 Tag tag = Tag.valueOf(tagName);
 Tag expectedTag = new Tag(tagName.toLowerCase());
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagValueOfDuplicateTagName() {
 String tagName = "div";
 Tag tag1 = Tag.valueOf(tagName);
 Tag tag2 = Tag.valueOf(tagName);
 assertEquals(tag1, tag2);
 }

 @Test
 public void testTagIsKnownTagUnknownTag() {
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.toString());
 }

 @Test
 public void testTagEqualsDifferentObjectType() {
 Tag tag = new Tag("div");
 Assert.assertNotEquals(tag, new Object());
 }

 @Test
 public void testTagEqualsDifferentTagName() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testTagEqualsSameTagButDifferentProperties() {
 Tag tag1 = new Tag("div").setSelfClosing();
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testTagHashCodeDifferentTags() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}