package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());

 Assert.assertEquals("div", divTag.getName());
 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());
 }

 @Test
 public void testUnknownTagValues() {
 Tag unknownTag = Tag.valueOf("unknown");

 Assert.assertEquals("unknown", unknownTag.getName());
 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.formatAsBlock());
 Assert.assertTrue(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 }

 @Test
 public void testFormTagValues() {
 Tag inputTag = Tag.valueOf("input");

 Assert.assertEquals("input", inputTag.getName());
 Assert.assertFalse(inputTag.isBlock());
 Assert.assertFalse(inputTag.formatAsBlock());
 Assert.assertFalse(inputTag.canContainInline());
 Assert.assertFalse(inputTag.isEmpty());
 Assert.assertFalse(inputTag.isSelfClosing());
 Assert.assertTrue(inputTag.isFormListed());
 Assert.assertTrue(inputTag.isFormSubmittable());
 }

 @Test
 public void testEmptyTagValues() {
 Tag imgTag = Tag.valueOf("img");

 Assert.assertEquals("img", imgTag.getName());
 Assert.assertFalse(imgTag.isBlock());
 Assert.assertFalse(imgTag.formatAsBlock());
 Assert.assertFalse(imgTag.canContainInline());
 Assert.assertTrue(imgTag.isEmpty());
 Assert.assertFalse(imgTag.isSelfClosing());
 }

 @Test
 public void testSelfClosingTagValues() {
 Tag brTag = Tag.valueOf("br");

 Assert.assertEquals("br", brTag.getName());
 Assert.assertFalse(brTag.isBlock());
 Assert.assertFalse(brTag.formatAsBlock());
 Assert.assertTrue(brTag.canContainInline());
 Assert.assertFalse(brTag.isEmpty());
 Assert.assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testKnownTags() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertTrue(Tag.isKnownTag("div"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespaceTagValues() {
 Tag preTag = Tag.valueOf("pre");

 Assert.assertEquals("pre", preTag.getName());
 Assert.assertTrue(preTag.isBlock());
 Assert.assertTrue(preTag.formatAsBlock());
 Assert.assertTrue(preTag.canContainInline());
 Assert.assertFalse(preTag.isEmpty());
 Assert.assertFalse(preTag.isSelfClosing());
 Assert.assertTrue(preTag.preserveWhitespace());
 }
}