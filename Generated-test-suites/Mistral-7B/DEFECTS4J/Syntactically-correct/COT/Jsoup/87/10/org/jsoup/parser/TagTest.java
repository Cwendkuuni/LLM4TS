package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagNameGetters() {
 Tag tag = new Tag(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 assertEquals(TAG_NAME.toLowerCase(), tag.getName().toLowerCase());
 }

 @Test
 public void testTagValueOfWithPredefinedTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testTagValueOfWithUnknownTag() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertFalse(tag.isBlock());
 }

 @Test(expected = NullPointerException.class)
 public void testTagValueOfWithNullTagName() {
 Tag.valueOf(null);
 }

 @Test
 public void testTagIsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());

 tag = new Tag("input");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.formatAsBlock());

 tag = new Tag("input");
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.canContainBlock());

 tag = new Tag("input");
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testTagIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());

 tag = new Tag("div");
 assertFalse(tag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag tag = new Tag("img");
 assertFalse(tag.isEmpty());

 tag = new Tag("input");
 tag.empty = true;
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tag = new Tag("img");
 assertFalse(tag.isSelfClosing());

 tag = new Tag("img").setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Map<String, Tag> tags = new HashMap<>();
 Tag.register(new Tag("p"));

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("testTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());

 tag = new Tag("div");
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag tag = new Tag("button");
 assertTrue(tag.isFormListed());

 tag = new Tag("div");
 assertFalse(tag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormSubmittable());

 tag = new Tag("div");
 assertFalse(tag.isFormSubmittable());
 }

 @Test(expected = ClassCastException.class)
 public void testTagEqualsWithDifferentClass() {
 Tag tag = new Tag("p");
 Object obj = new Object();
 assertNotEquals(tag, obj);
 }

 @Test
 public void testTagEqualsWithSameTagName() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testTagEqualsWithDifferentTagName() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertNotEquals(tag1, tag2);
 }

 @Test
 public void testTagEqualsWithSameTagButDifferentValues() {
 Tag tag1 = new Tag("input").setFormList(true).setFormSubmit(true);
 Tag tag2 = new Tag("input");
 assertNotEquals(tag1, tag2);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("p").setFormList(true).setFormSubmit(true);
 Tag tag2 = new Tag("p").setFormList(true).setFormSubmit(true);
 assertEquals(tag1.hashCode(), tag2.hashCode());

 Tag tag3 = new Tag("div").setFormList(true).setFormSubmit(true);
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }

 // Edge cases and exception handling tests

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithEmptyTagName() {
 Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithNullCharacterTagName() {
 Tag.valueOf(null);
 }

 @Test
 public void testTagValueOfWithDuplicateTagName() {
 Map<String, Tag> tags = new HashMap<>();
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag.register(tag1);
 Tag.register(tag2);

 assertEquals(tag1, Tag.valueOf("p"));
 assertEquals(tag1, Tag.valueOf("p"));
 assertEquals(1, tags.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testTagValueOfWithUnknownTagAndSettingsThatPreserveCase() {
 Tag.valueOf("TestTag", ParseSettings.preserveCase);
 }

 @Test(expected = IllegalStateException.class)
 public void testTagValueOfWithUnknownTagAndSettingsThatNormalizeTag() {
 Tag.valueOf("TestTag", ParseSettings.normalizeTag);
 }

 @Test(expected = IllegalStateException.class)
 public void testTagValueOfWithUnknownTagAndSettingsThatPreserveWhitespace() {
 Tag.valueOf("TestTag", ParseSettings.preserveWhitespace);
 }

 @Test(expected = IllegalStateException.class)
 public void testTagValueOfWithUnknownTagAndSettingsThatPreserveTagName() {
 Tag.valueOf("TestTag", ParseSettings.preserveTagName);
 }

 @Test(expected = IllegalStateException.class)
 public void testTagValueOfWithUnknownTagAndSettingsThatTrimTagName() {
 Tag.valueOf("TestTag", ParseSettings.trimTagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithEmptyListOfTags() {
 Tag.valueOf("", new ParseSettings());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithNullListOfTags() {
 Tag.valueOf(null, new ParseSettings());
 }

 @Test(expected = NullPointerException.class)
 public void testTagRegisterWithNullTag() {
 Tag.register(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagRegisterWithTagAlreadyRegistered() {
 Map<String, Tag> tags = new HashMap<>();
 Tag tag = new Tag("p");
 Tag.register(tag);
 Tag.register(tag);
 }
}