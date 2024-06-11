package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals(tagName, tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.isFormListed());
 Assert.assertFalse(tag.isFormSubmittable());

 tagName = "FORM";
 tag = Tag.valueOf(tagName);

 Assert.assertEquals(tagName, tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertTrue(tag.isFormListed());
 Assert.assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfUnknown() {
 String tagName = "Xyz";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertNotNull(tag);
 Assert.assertEquals(tagName, tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.isFormListed());
 Assert.assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 String tagName2 = "div";
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertTrue(tag1.canContain(tag2));
 }

 @Test(expected = IllegalStateException.class)
 public void testTagCannotContainSelf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 tag = tag.setSelfClosing();

 String tagName2 = tagName;
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertFalse(tag.canContain(tag2));
 }

 @Test
 public void testTagIsDataTag() {
 String tagName = "script";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertTrue(tag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);

 tag = tag.setEmpty();

 Assert.assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testTagPreserveWhitespace() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);

 tag = tag.setPreserveWhitespace(true);

 Assert.assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testTagFormListed() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertTrue(tag.isFormListed());
 }

 @Test
 public void testTagFormSubmittable() {
 String tagName = "button";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertTrue(tag.isFormSubmittable());
 }
}