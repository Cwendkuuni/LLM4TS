package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNormalName() {
 Tag tag = Tag.valueOf("P", ParseSettings.ignoreCase);
 Assert.assertEquals("p", tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("UnknownTag");
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");

 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertTrue(pTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Tag brTag = Tag.valueOf("br");
 Tag pTag = Tag.valueOf("p");

 Assert.assertFalse(brTag.isInline());
 Assert.assertTrue(pTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag divTag = Tag.valueOf("div");

 Assert.assertTrue(scriptTag.isData());
 Assert.assertFalse(divTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertFalse(imgTag.isEmpty());
 Assert.assertFalse(inputTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertTrue(imgTag.isSelfClosing());
 Assert.assertFalse(inputTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("UnknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 Assert.assertTrue(preTag.preserveWhitespace());
 Assert.assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 Tag textareaTag = Tag.valueOf("textarea");

 Assert.assertTrue(inputTag.isFormListed());
 Assert.assertTrue(textareaTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input[type=submit]");
 Tag textareaTag = Tag.valueOf("textarea");

 Assert.assertTrue(inputTag.isFormSubmittable());
 Assert.assertFalse(textareaTag.isFormSubmittable());
 }
}