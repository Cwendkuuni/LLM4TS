package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<String, Tag>();

 @Test
 public void testTagConstructor() {
 Tag tag = new Tag("div");
 Assert.assertEquals("div", tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.canContainInline());
 }

 @Test
 public void testTagEmpty() {
 Tag tag = new Tag("img");
 Assert.assertFalse(tag.isEmpty());
 }

 @Test
 public void testTagValueOfExistingTag() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 Assert.assertEquals(tagName, tag.getName());
 }

 @Test
 public void testTagValueOfNewTag() {
 String tagName = "newTag";
 Tag tag = Tag.valueOf(tagName);
 Assert.assertEquals(tagName, tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.canContainBlock());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNullTagName() {
 Tag tag = null;
 try {
 tag = Tag.valueOf(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 Assert.assertNull(tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmptyTagName() {
 Tag tag = null;
 try {
 tag = Tag.valueOf("");
 } catch (IllegalArgumentException e) {
 // expected
 }
 Assert.assertNull(tag);
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName = "div";
 Assert.assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testTagIsNotKnownTag() {
 String tagName = "newTag";
 Assert.assertFalse(Tag.isKnownTag(tagName));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = new Tag("pre");
 Assert.assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testTagSetSelfClosing() {
 Tag tag = new Tag("p");
 Tag selfClosingTag = tag.setSelfClosing();
 Assert.assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 Tag tag3 = new Tag("p");
 Assert.assertEquals(tag1, tag1);
 Assert.assertEquals(tag1, tag2);
 Assert.assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 Tag tag3 = new Tag("p");
 Assert.assertEquals(tag1.hashCode(), tag1.hashCode());
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("div");
 Assert.assertEquals("div", tag.toString());
 }

}