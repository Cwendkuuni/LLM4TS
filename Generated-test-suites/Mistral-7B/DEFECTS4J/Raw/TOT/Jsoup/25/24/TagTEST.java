package org.jsoup.parser.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 Assertions.assertEquals(tagName, tag.getName());
 Assertions.assertTrue(tag.isBlock());
 Assertions.assertTrue(tag.formatAsBlock());
 Assertions.assertTrue(tag.canContainBlock());
 Assertions.assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 Assertions.assertFalse(tag.isSelfClosing());
 Assertions.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagValueOfUnknown() {
 String tagName = "foo";
 Tag tag = Tag.valueOf(tagName);

 Assertions.assertFalse(tag.isBlock());
 Assertions.assertFalse(tag.formatAsBlock());
 Assertions.assertTrue(tag.canContainBlock());
 Assertions.assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 Assertions.assertFalse(tag.isSelfClosing());
 Assertions.assertFalse(tag.preserveWhitespace());
 Assertions.assertEquals(tagName, tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 String tagName1 = "div";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);

 Assertions.assertTrue(tag1.isBlock());
 Assertions.assertFalse(tag2.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 String tagName1 = "div";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "input";
 Tag tag2 = Tag.valueOf(tagName2);

 Assertions.assertTrue(tag1.formatAsBlock());
 Assertions.assertFalse(tag2.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName1 = "div";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "p";
 Tag tag2 = Tag.valueOf(tagName2);

 Assertions.assertTrue(tag1.canContainBlock());
 Assertions.assertTrue(tag1.canContainInline());
 Assertions.assertTrue(tag2.canContainBlock());
 Assertions.assertTrue(tag2.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 String tagName1 = "div";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);
 String tagName3 = "input";
 Tag tag3 = Tag.valueOf(tagName3);

 Assertions.assertFalse(tag1.isEmpty());
 Assertions.assertFalse(tag2.isEmpty());
 Assertions.assertFalse(tag3.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);
 tag2.setSelfClosing();

 Assertions.assertFalse(tag1.isSelfClosing());
 Assertions.assertTrue(tag2.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Map<String, Tag> tags = new HashMap<>();
 Tag tag = new Tag("html");
 tags.put("html", tag);

 Assertions.assertTrue(Tag.isKnownTag("html"));
 Assertions.assertTrue(Tag.isKnownTag(tag.getName()));
 Assertions.assertFalse(Tag.isKnownTag("invalid"));
 }
}