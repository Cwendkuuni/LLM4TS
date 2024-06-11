package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testValueOfKnownTag() {
  Tag tag = Tag.valueOf("p");
  assertTrue(Tag.isKnownTag("p"));
  assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
  Tag tag = Tag.valueOf(TAG_NAME);
  assertFalse(Tag.isKnownTag(TAG_NAME));
  assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testIsBlock_default() {
  Tag tag = new Tag(TAG_NAME);
  assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlock_false() {
  Tag tag = new Tag(TAG_NAME);
  tag.isBlock = false;
  assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlock_default() {
  Tag tag = new Tag(TAG_NAME);
  assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlock_false() {
  Tag tag = new Tag(TAG_NAME);
  tag.formatAsBlock = false;
  assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock_default() {
  Tag tag = new Tag(TAG_NAME);
  assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainBlock_false() {
  Tag tag = new Tag(TAG_NAME);
  tag.canContainBlock = false;
  assertFalse(tag.canContainBlock());
 }

 @Test
 public void testCanContainInline_default() {
  Tag tag = new Tag(TAG_NAME);
  assertTrue(tag.canContainInline());
 }

 @Test
 public void testCanContainInline_false() {
  Tag tag = new Tag(TAG_NAME);
  tag.canContainInline = false;
  assertFalse(tag.canContainInline());
 }

 @Test
 public void testIsEmpty_default() {
  Tag tag = new Tag(TAG_NAME);
  assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsEmpty_true() {
  Tag tag = new Tag(TAG_NAME);
  tag.empty = true;
  assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing_default() {
  Tag tag = new Tag(TAG_NAME);
  assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosing_true() {
  Tag tag = new Tag(TAG_NAME);
  tag.selfClosing = true;
  assertTrue(tag.isSelfClosing());
 }

 @Test(expected = NullPointerException.class)
 public void testValueOfNullTagName() {
  Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
  Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyMap() {
  synchronized (Tag.tags) {
      Tag.tags.clear();
      Tag tag = Tag.valueOf(TAG_NAME);
  }
 }
}