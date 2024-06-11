package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<>();

 @Test
 public void testTagNameGetters() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 assertEquals("div", tag.toString());
 }

 @Test
 public void testValueOfWithKnownTag() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfWithUnknownTag() {
 Tag tag = Tag.valueOf("unknown");
 assertEquals("unknown", tag.getName());
 assertEquals("unknown", tag.toString());
 }

 @Test
 public void testValueOfWithParseSettingsNormCase() {
 Tag tag = Tag.valueOf("P", new ParseSettings() {});
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfWithParseSettingsPreserveCase() {
 Tag tag = Tag.valueOf("P", new ParseSettings() {
 @Override public String normalizeTag(String tagName) {
 return tagName;
 }
 });
 assertEquals("P", tag.getName());
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsFormatAsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag tag = new Tag("img");
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("html"));
 }

 @Test
 public void testIsKnownTagWithUnknownTag() {
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag tag = new Tag("button");
 assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testSetSelfClosing() {
 Tag tag = new Tag("img");
 tag.setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test(expected = NullPointerException.class)
 public void testValueOfWithNullTagName() {
 Tag.valueOf(null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTagWithNullSettings() {
 Tag.getValueOf("p", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfWithEmptyTagName() {
 Tag.valueOf("", new ParseSettings() {});
 }

 @Test(expected = IllegalStateException.class)
 public void testValueOfWithInvalidTagName() {
 Tag.valueOf("&tag;", new ParseSettings() {});
 }

 @Test
 public void testGetBlockTags() {
 Tag[] blockTags = Tag.getBlockTags();
 assertArrayEquals(blockTags.length, blockTags.length, blockTags);
 for (String tagName : blockTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testGetInlineTags() {
 Tag[] inlineTags = Tag.getInlineTags();
 assertArrayEquals(inlineTags.length, inlineTags.length, inlineTags);
 for (String tagName : inlineTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testGetEmptyTags() {
 Tag[] emptyTags = Tag.getEmptyTags();
 assertArrayEquals(emptyTags.length, emptyTags.length, emptyTags);
 for (String tagName : emptyTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testGetFormatAsInlineTags() {
 Tag[] formatAsInlineTags = Tag.getFormatAsInlineTags();
 assertArrayEquals(formatAsInlineTags.length, formatAsInlineTags.length, formatAsInlineTags);
 for (String tagName : formatAsInlineTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testGetPreserveWhitespaceTags() {
 Tag[] preserveWhitespaceTags = Tag.getPreserveWhitespaceTags();
 assertArrayEquals(preserveWhitespaceTags.length, preserveWhitespaceTags.length, preserveWhitespaceTags);
 for (String tagName : preserveWhitespaceTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testGetFormListedTags() {
 Tag[] formListedTags = Tag.getFormListedTags();
 assertArrayEquals(formListedTags.length, formListedTags.length, formListedTags);
 for (String tagName : formListedTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testGetFormSubmitTags() {
 Tag[] formSubmitTags = Tag.getFormSubmitTags();
 assertArrayEquals(formSubmitTags.length, formSubmitTags.length, formSubmitTags);
 for (String tagName : formSubmitTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }
}