package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNormalization() {
 Tag tag = Tag.valueOf("P", ParseSettings.preserveCase);
 Assert.assertEquals("p", tag.getName());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag tag = Tag.valueOf(" unknownTag ", ParseSettings.preserveCase);
 Assert.assertFalse(tag.isKnownTag());
 Assert.assertTrue(tag.equals(Tag.valueOf(" unknownTag ")));
 }

 @Test
 public void testBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Assert.assertTrue(pTag.isBlock());
 }

 @Test
 public void testInlineTag() {
 Tag imgTag = Tag.valueOf("img");
 Assert.assertFalse(imgTag.isBlock());
 }

 @Test
 public void testEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 imgTag.setEmpty();
 Assert.assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 imgTag.setSelfClosing();
 Assert.assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");
 preTag.setPreserveWhitespace(true);
 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testFormListedTag() {
 Tag inputTag = Tag.valueOf("input");
 inputTag.setFormList(true);
 Assert.assertTrue(inputTag.isFormListed());
 }

 @Test
 public void testFormSubmittableTag() {
 Tag inputTag = Tag.valueOf("input");
 inputTag.setFormSubmittable(true);
 Assert.assertTrue(inputTag.isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null, ParseSettings.preserveCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf("", ParseSettings.preserveCase);
 }
}