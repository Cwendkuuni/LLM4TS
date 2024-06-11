package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTag_ValidName() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Tag expectedTag = new Tag(TAG_NAME);
 assertEquals(expectedTag, tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_NullName() {
 Tag tag = Tag.valueOf(null);
 fail("Should throw IllegalArgumentException for null name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_EmptyName() {
 Tag tag = Tag.valueOf("");
 fail("Should throw IllegalArgumentException for empty name");
 }

 @Test
 public void testTag_GetName() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testTag_IsBlock_DefaultIsTrue() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testTag_FormatAsBlock_DefaultIsTrue() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testTag_CanContainBlock_DefaultIsTrue() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTag_IsInline_DefaultIsFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isInline());
 }

 @Test
 public void testTag_IsData_DefaultIsFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isData());
 }

 @Test
 public void testTag_IsEmpty_DefaultIsFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testTag_IsSelfClosing_DefaultIsFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testTag_IsKnownTag_DefaultIsFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(Tag.isKnownTag(TAG_NAME));
 }

 @Test
 public void testTag_PreserveWhitespace_DefaultIsFalse() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTag_SetSelfClosing() {
 Tag tag = new Tag(TAG_NAME);
 Tag selfClosingTag = tag.setSelfClosing();
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetName() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setName("newName");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetIsBlock() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setIsBlock(false);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetFormatAsBlock() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setFormatAsBlock(false);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetCanContainBlock() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setCanContainBlock(false);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetCanContainInline() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setCanContainInline(false);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetEmpty() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setEmpty(true);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTag_SetPreserveWhitespace() {
 Tag tag = new Tag(TAG_NAME);
 try {
 tag.setPreserveWhitespace(true);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testTag_Equals() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 Tag tag3 = new Tag("otherTag");
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testTag_Hashcode() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testTag_ToString() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.toString());
 }

 @Test
 public void testTag_ValueOf_KnownTag() {
 String knownTagName = "p";
 Tag tag = Tag.valueOf(knownTagName);
 Tag expectedTag = new Tag(knownTagName);
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTag_ValueOf_UnknownTag() {
 String unknownTagName = "testTag";
 Tag tag = Tag.valueOf(unknownTagName);
 // Unknown tags are not registered and will only .equals().
 Tag expectedTag = new Tag(unknownTagName);
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTag_IsBlock_KnownBlockTag() {
 String blockTagName = "p";
 Tag tag = Tag.valueOf(blockTagName);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testTag_IsBlock_UnknownTag() {
 String unknownTagName = "testTag";
 Tag tag = Tag.valueOf(unknownTagName);
 assertFalse(tag.isBlock());
 }

 @Test
 public void testTag_IsInline_KnownInlineTag() {
 String inlineTagName = "img";
 Tag tag = Tag.valueOf(inlineTagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.isInline());
 }

 @Test
 public void testTag_IsInline_UnknownTag() {
 String unknownTagName = "testTag";
 Tag tag = Tag.valueOf(unknownTagName);
 assertFalse(tag.isInline());
 }

 @Test
 public void testTag_IsData_KnownDataTag() {
 String dataTagName = "meta";
 Tag tag = Tag.valueOf(dataTagName);
 assertTrue(tag.isData());
 }

 @Test
 public void testTag_IsData_UnknownTag() {
 String unknownTagName = "testTag";
 Tag tag = Tag.valueOf(unknownTagName);
 assertFalse(tag.isData());
 }

 @Test
 public void testTag_IsEmpty_EmptyTag() {
 String emptyTagName = "img";
 Tag tag = Tag.valueOf(emptyTagName);
 tag.setEmpty(true);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testTag_IsEmpty_NonEmptyTag() {
 String nonEmptyTagName = "p";
 Tag tag = Tag.valueOf(nonEmptyTagName);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testTag_IsSelfClosing_SelfClosingTag() {
 String selfClosingTagName = "img";
 Tag tag = Tag.valueOf(selfClosingTagName);
 tag.setSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTag_IsSelfClosing_NonSelfClosingTag() {
 String nonSelfClosingTagName = "p";
 Tag tag = Tag.valueOf(nonSelfClosingTagName);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testTag_IsKnownTag_KnownTag() {
 String knownTagName = "p";
 assertTrue(Tag.isKnownTag(knownTagName));
 }

 @Test
 public void testTag_IsKnownTag_UnknownTag() {
 String unknownTagName = "testTag";
 assertFalse(Tag.isKnownTag(unknownTagName));
 }

 @Test
 public void testTag_PreserveWhitespace_PreserveWhitespaceTag() {
 String preserveWhitespaceTagName = "pre";
 Tag tag = Tag.valueOf(preserveWhitespaceTagName);
 tag.setPreserveWhitespace(true);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testTag_PreserveWhitespace_NonPreserveWhitespaceTag() {
 String nonPreserveWhitespaceTagName = "p";
 Tag tag = Tag.valueOf(nonPreserveWhitespaceTagName);
 assertFalse(tag.preserveWhitespace());
 }
}