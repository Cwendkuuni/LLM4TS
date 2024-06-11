package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagConstructor() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME.toLowerCase(), tag.getName());
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals(TAG_NAME.toLowerCase(), tag.getName());
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

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyOrWhiteSpaceTagName() {
 Tag tag = new Tag("");
 Tag tag2 = new Tag(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterTagTwice() {
 Tag tag = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 }

 @Test
 public void testIsBlockFalse() {
 Tag tag = new Tag(TAG_NAME).setInline();
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockFalse() {
 Tag tag = new Tag(TAG_NAME).setFormatAsInline();
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 Tag tag = new Tag(TAG_NAME).setCannotContainBlock();
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testCanContainInlineFalse() {
 Tag tag = new Tag(TAG_NAME).setCannotContainInline();
 assertFalse(tag.canContainInline());
 }

 @Test
 public void testEmptyTrue() {
 Tag tag = new Tag(TAG_NAME).setEmpty();
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testSelfClosingTrue() {
 Tag tag = new Tag(TAG_NAME).setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 Tag tag = new Tag(TAG_NAME).setPreserveWhitespace();
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testEqualsSameObject() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(tag, tag);
 }

 @Test
 public void testEqualsDifferentObjects() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag("testTag2");
 assertNotEquals(tag1, tag2);
 }

 @Test
 public void testHashCode() {
 Tag tag = new Tag(TAG_NAME);
 int hashCode1 = tag.hashCode();
 Tag tag2 = new Tag(TAG_NAME);
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.toString());
 }

 @Test
 public void testIsKnownTag() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(Tag.isKnownTag(TAG_NAME));

 // register tag
 Tag registeredTag = Tag.register(tag);
 assertTrue(Tag.isKnownTag(TAG_NAME));
 assertEquals(registeredTag, Tag.valueOf(TAG_NAME));
 }

 @Test
 public void testIsKnownTagOtherTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag(TAG_NAME);
 assertFalse(Tag.isKnownTag(TAG_NAME));
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testToStringUpperCase() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME.toUpperCase(), tag.toString().toUpperCase());
 }

 @Test
 public void testToStringLowerCase() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME.toLowerCase(), tag.toString().toLowerCase());
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