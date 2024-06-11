package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNormalName() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("mytag", ParseSettings.preserveCase);
 assertNotNull(tag);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testBlockTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testInlineTag() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testEmptyTag() {
 Tag tag = Tag.valueOf("img");
 tag.empty = true;
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testSelfClosingTag() {
 Tag tag = Tag.valueOf("img");
 tag.selfClosing = true;
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testFormListedTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testFormSubmittableTag() {
 Tag tag = Tag.valueOf("input");
 tag.formSubmit = true;
 assertTrue(tag.isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTag() {
 Tag tag = Tag.valueOf("", ParseSettings.preserveCase);
 fail();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null, ParseSettings.preserveCase);
 fail();
 }
}