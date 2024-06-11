package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagName() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testTagNormalizedName() {
 Tag tag = new Tag(TAG_NAME.toUpperCase());
 assertEquals(TAG_NAME, tag.getName()); // case insensitive
 }

 @Test
 public void testUnknownTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagNameValueOf() {
 Tag tag = Tag.valueOf(null);
 fail("Should throw NullPointerException");
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
 assertTrue(tag.canContainBlock()); // deprecated, same as isBlock()
 }

 @Test
 public void testTagIsInline() {
 Tag tag = new Tag("i");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testTagIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag tag = new Tag("img");
 tag.empty = true;
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tag = new Tag("img");
 tag.selfClosing = true;
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 }

 @Test
 public void testTagIsKnownTagFalse() {
 assertFalse(Tag.isKnownTag("TestTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = new Tag("pre");
 tag.preserveWhitespace = true;
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag tag = new Tag("input");
 tag.formList = true;
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tag = new Tag("input");
 tag.formSubmit = true;
 assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("testTag");
 assertEquals("testTag", tag.toString());
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
 tag.empty = true;
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
 tag.preserveWhitespace = true;
 assertTrue(tag.preserveWhitespace());
 }
 }

 @Test
 public void testFormListedTags() {
 for (String tagName : Tag.formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 tag.formList = true;
 assertTrue(tag.isFormListed());
 }
 }

 @Test
 public void testFormSubmitTags() {
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 tag.formSubmit = true;
 assertTrue(tag.isFormSubmittable());
 }
 }

}