package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());

 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());
 Assert.assertFalse(pTag.preserveWhitespace());
 Assert.assertFalse(pTag.isFormListed());
 Assert.assertFalse(pTag.isFormSubmittable());

 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());
 Assert.assertFalse(divTag.preserveWhitespace());
 Assert.assertFalse(divTag.isFormListed());
 Assert.assertFalse(divTag.isFormSubmittable());
 }

 @Test
 public void testUnknownTagValues() {
 Tag unknownTag = Tag.valueOf("unknownTag");

 Assert.assertEquals("unknownTag", unknownTag.getName());

 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.formatAsBlock());
 Assert.assertTrue(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 Assert.assertFalse(unknownTag.isFormListed());
 Assert.assertFalse(unknownTag.isFormSubmittable());
 }

 @Test
 public void testCaseInsensitiveTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("p", PTag.getName());

 Assert.assertEquals(pTag, PTag);
 }

 @Test
 public void testNormalizedTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag Ptag = Tag.valueOf("P");
 Tag pTagWithSettings = Tag.valueOf("p", ParseSettings.lowerCase);

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("p", Ptag.getName());
 Assert.assertEquals("p", pTagWithSettings.getName());

 Assert.assertEquals(pTag, Ptag);
 Assert.assertEquals(pTag, pTagWithSettings);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf("");

 Assert.assertEquals("", tag.getName());

 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertTrue(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 Assert.assertFalse(tag.isFormListed());
 Assert.assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testPredefinedTagValues() {
 Tag htmlTag = Tag.valueOf("html");
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals(htmlTag, Tag.html());
 Assert.assertEquals(pTag, Tag.p());
 Assert.assertEquals(divTag, Tag.div());
 }

 @Test
 public void testIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("html"));
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testIsInlineTag() {
 Tag pTag = Tag.valueOf("p");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertFalse(pTag.isInline());
 Assert.assertTrue(inputTag.isInline());
 }

 @Test
 public void testIsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(divTag.isBlock());
 }

 @Test
 public void testIsDataTag() {
 Tag pTag = Tag.valueOf("p");
 Tag textAreaTag = Tag.valueOf("textarea");

 Assert.assertFalse(pTag.isData());
 Assert.assertTrue(textAreaTag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertFalse(imgTag.isEmpty());
 Assert.assertFalse(inputTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");
 Tag brTag = Tag.valueOf("br");

 Assert.assertFalse(imgTag.isSelfClosing());
 Assert.assertFalse(inputTag.isSelfClosing());
 Assert.assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagWithNormalizedName() {
 Assert.assertTrue(Tag.isKnownTag("html"));
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("Unknown"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");

 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testFormListedTag() {
 Tag inputTag = Tag.valueOf("input");

 Assert.assertTrue(inputTag.isFormListed());
 }

 @Test
 public void testFormSubmittableTag() {
 Tag inputTag = Tag.valueOf("input");

 Assert.assertTrue(inputTag.isFormSubmittable());
 }
}