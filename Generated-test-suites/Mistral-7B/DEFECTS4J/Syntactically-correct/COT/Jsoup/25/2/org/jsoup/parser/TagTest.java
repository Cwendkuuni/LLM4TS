package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testConstructor() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testIsBlockDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainInlineDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainInline());
 }

 @Test
 public void testEmptyDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testSelfClosingDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceDefault() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testIsBlockFalse() {
 Tag tag = new Tag(TAG_NAME).setIsBlock(false);
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockFalse() {
 Tag tag = new Tag(TAG_NAME).setFormatAsBlock(false);
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 Tag tag = new Tag(TAG_NAME).setCanContainBlock(false);
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testCanContainInlineFalse() {
 Tag tag = new Tag(TAG_NAME).setCanContainInline(false);
 assertFalse(tag.canContainInline());
 }

 @Test
 public void testEmptyTrue() {
 Tag tag = new Tag(TAG_NAME).setEmpty(true);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testSelfClosingTrue() {
 Tag tag = new Tag(TAG_NAME).setSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 Tag tag = new Tag(TAG_NAME).setPreserveWhitespace(true);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testNotEquals() {
 Tag tag1 = new Tag(TAG_NAME);
 Object object = new Object();
 assertFalse(tag1.equals(object));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testToString() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.toString());
 }

 @Test
 public void testIsKnownTag() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(Tag.isKnownTag(tag.getName()));
 }

 @Test
 public void testIsKnownTagFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullMap() {
 synchronized (null) {
 Tag tag = new Tag(TAG_NAME);
 }
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
 assertFalse(tag.isBlock());
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
 assertFalse(tag.isBlock());
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