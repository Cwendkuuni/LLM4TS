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
 }

 @Test
 public void testTagNormalization() {
 Tag tag = Tag.valueOf("P", ParseSettings.preserveCase);
 Assert.assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag tag = Tag.valueOf("unknownTag");
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 Assert.assertEquals(tag1, tag1);
 Assert.assertEquals(tag1, tag2);
 Assert.assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 Assert.assertEquals(tag1.hashCode(), tag1.hashCode());
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testTagToString() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null, ParseSettings.preserveCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf("", ParseSettings.preserveCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameContainsInvalidCharacters() {
 Tag tag = Tag.valueOf("<tag>", ParseSettings.preserveCase);
 }

 @Test
 public void testIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("pre");
 Assert.assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testFormListedTag() {
 Tag tag = Tag.valueOf("input");
 Assert.assertTrue(tag.isFormListed());
 }

 @Test
 public void testFormSubmittableTag() {
 Tag tag = Tag.valueOf("input");
 Assert.assertTrue(tag.isFormSubmittable());
 }
}