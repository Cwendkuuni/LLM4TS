package org.jsoup.parser.test;

import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {

 @org.junit.Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());
 Assert.assertEquals("img", imgTag.getName());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 Assert.assertFalse(unknownTag.isKnownTag());
 }

 @org.junit.Test
 public void testBlockTags() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag scriptTag = Tag.valueOf("script");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(scriptTag.isBlock());
 }

 @org.junit.Test
 public void testInlineTags() {
 Tag aTag = Tag.valueOf("a");
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertFalse(aTag.isBlock());
 Assert.assertFalse(imgTag.isBlock());
 Assert.assertFalse(inputTag.isBlock());

 // Test empty inline tag
 Tag brTag = Tag.valueOf("br");
 Assert.assertFalse(brTag.isBlock());
 Assert.assertTrue(brTag.isEmpty());
 }

 @org.junit.Test
 public void testDataOnlyTags() {
 Tag metaTag = Tag.valueOf("meta");
 Tag scriptTag = Tag.valueOf("script");

 Assert.assertTrue(metaTag.isData());
 Assert.assertFalse(scriptTag.isData());
 }

 @org.junit.Test
 public void testSelfClosingTags() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 Assert.assertTrue(imgTag.isSelfClosing());
 Assert.assertTrue(brTag.isSelfClosing());
 }

 @org.junit.Test
 public void testKnownTags() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertTrue(Tag.isKnownTag("div"));
 Assert.assertTrue(Tag.isKnownTag("img"));

 // Test unknown tag
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @org.junit.Test
 public void testPreserveWhitespaceTags() {
 Tag preTag = Tag.valueOf("pre");

 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @org.junit.Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("h1");

 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertFalse(h1Tag.formatAsBlock());
 }
}