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
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testIsBlockMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.isBlock());

 Tag inlineTag = new Tag("span");
 assertFalse(inlineTag.isBlock());
 }

 @Test
 public void testFormatAsBlockMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.formatAsBlock());

 Tag inlineTag = new Tag("span");
 assertFalse(inlineTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockMethod() {
 Tag parentBlock = new Tag("div");
 Tag childBlock = new Tag("p");
 parentBlock.canContain(childBlock);
 assertTrue(parentBlock.canContainBlock());

 Tag parentInline = new Tag("span");
 assertFalse(parentInline.canContainBlock());
 }

 @Test(expected = IllegalStateException.class)
 public void testCanContainBlockDeprecatedMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.canContainBlock()); // deprecated
 }

 @Test
 public void testIsDataMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isData());

 Tag dataOnlyTag = new Tag("meta");
 assertTrue(dataOnlyTag.isData());
 }

 @Test
 public void testIsEmptyMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isEmpty());

 Tag emptyTag = new Tag("img");
 assertTrue(emptyTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isSelfClosing());

 Tag selfClosingTag = new Tag("img");
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(Tag.isKnownTag(TAG_NAME));

 Tag knownTag = Tag.valueOf("p");
 assertTrue(Tag.isKnownTag(knownTag.getName()));
 }

 @Test
 public void testPreserveWhitespaceMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.preserveWhitespace());

 Tag preservingTag = new Tag("pre");
 assertTrue(preservingTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListedMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isFormListed());

 Tag formListedTag = new Tag("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testIsFormSubmittableMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isFormSubmittable());

 Tag formSubmittableTag = new Tag("input[type=submit]");
 assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testSelfClosingMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isSelfClosing());

 Tag selfClosingTag = tag.setSelfClosing();
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testEqualsMethod() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 assertTrue(tag1.equals(tag2));

 Tag tag3 = new Tag("span");
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testHashCodeMethod() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 assertEquals(tag1.hashCode(), tag2.hashCode());

 Tag tag3 = new Tag("span");
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToStringMethod() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.toString());
 }

 @Test
 public void testStaticInitializers() {
 // Check static block initialization
 Map<String, Tag> tagsMap = new HashMap<>();
 // Check blockTags
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.blockTags.length);
 // Check inlineTags
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.inlineTags.length);
 // Check emptyTags
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isData());
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.emptyTags.length);
 // Check formatAsInlineTags
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.formatAsInlineTags.length);
 // Check preserveWhitespaceTags
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.preserveWhitespaceTags.length);
 // Check formListedTags
 for (String tagName : Tag.formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormListed());
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.formListedTags.length);
 // Check formSubmitTags
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormSubmittable());
 tagsMap.put(tagName, tag);
 }
 assertTrue(tagsMap.size() == Tag.formSubmitTags.length);
 }
}