package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 Assert.assertFalse(tag.isFormListed());
 Assert.assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfCaseInsensitive() {
 Tag tag = Tag.valueOf("P");
 Assert.assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 Assert.assertFalse(tag.isFormListed());
 Assert.assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfUnknown() {
 Tag tag = Tag.valueOf("unknown");
 Assert.assertNotNull(tag);
 Assert.assertEquals("unknown", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 Assert.assertFalse(tag.isFormListed());
 Assert.assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagIsKnownTagCaseInsensitive() {
 Assert.assertTrue(Tag.isKnownTag("P"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagIsBlock() {
 Tag tag = Tag.valueOf("p");
 Assert.assertTrue(tag.isBlock());

 Tag tag2 = Tag.valueOf("input");
 Assert.assertFalse(tag2.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = Tag.valueOf("p");
 Assert.assertTrue(tag.formatAsBlock());

 Tag tag2 = Tag.valueOf("input");
 Assert.assertFalse(tag2.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = Tag.valueOf("div");
 Assert.assertTrue(tag.canContainBlock());

 Tag tag2 = Tag.valueOf("p");
 Assert.assertFalse(tag2.canContainBlock());
 }

 @Test
 public void testTagIsInline() {
 Tag tag = Tag.valueOf("span");
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.isInline());
 }

 @Test
 public void testTagIsData() {
 Tag tag = Tag.valueOf("script");
 Assert.assertTrue(tag.isData());

 Tag tag2 = Tag.valueOf("p");
 Assert.assertFalse(tag2.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag tag = Tag.valueOf("img");
 Assert.assertFalse(tag.isEmpty());

 Tag tag2 = Tag.valueOf("img").setSelfClosing();
 Assert.assertTrue(tag2.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tag = Tag.valueOf("img");
 Assert.assertFalse(tag.isSelfClosing());

 Tag tag2 = Tag.valueOf("img").setSelfClosing();
 Assert.assertTrue(tag2.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTagPreserveCase() {
 Assert.assertFalse(Tag.isKnownTag("P"));
 Assert.assertFalse(Tag.isKnownTag("p"));
 }

 @Test
 public void testTagIsFormListed() {
 Tag tag = Tag.valueOf("input");
 Assert.assertTrue(tag.isFormListed());

 Tag tag2 = Tag.valueOf("p");
 Assert.assertFalse(tag2.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tag = Tag.valueOf("input");
 Assert.assertTrue(tag.isFormSubmittable());

 Tag tag2 = Tag.valueOf("p");
 Assert.assertFalse(tag2.isFormSubmittable());
 }
}