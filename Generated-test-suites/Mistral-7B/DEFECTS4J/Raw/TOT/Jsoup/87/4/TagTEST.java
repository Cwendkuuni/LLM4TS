package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertNotNull(unknownTag);
 assertEquals("unknownTag", unknownTag.getName());

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(unknownTag.isBlock());

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertFalse(unknownTag.formatAsBlock());

 assertTrue(pTag.canContainInline());
 assertTrue(divTag.canContainInline());
 assertFalse(unknownTag.canContainInline());

 assertFalse(pTag.isEmpty());
 assertFalse(divTag.isEmpty());
 assertFalse(unknownTag.isEmpty());

 assertFalse(pTag.isSelfClosing());
 assertFalse(divTag.isSelfClosing());
 assertFalse(unknownTag.isSelfClosing());

 assertFalse(pTag.preserveWhitespace());
 assertFalse(divTag.preserveWhitespace());
 assertFalse(unknownTag.preserveWhitespace());

 assertFalse(pTag.isFormListed());
 assertFalse(divTag.isFormListed());
 assertFalse(unknownTag.isFormListed());

 assertFalse(pTag.isFormSubmittable());
 assertFalse(divTag.isFormSubmittable());
 assertFalse(unknownTag.isFormSubmittable());
 }

 @Test
 public void testTagNormalization() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");

 assertEquals("p", pTag.getName());
 assertEquals("p", PTag.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 Tag pTag = Tag.valueOf("p");

 assertFalse(unknownTag.equals(pTag));
 assertTrue(unknownTag.equals(unknownTag));
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveCaseTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");

 assertEquals(pTag, PTag);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagInstance() {
 Tag tag = null;
 assertFalse(tag.isBlock());
 fail("Expected NullPointerException");
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(brTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertFalse(pTag.isInline());
 assertFalse(divTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");

 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isSelfClosing());
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagWithEmptyName() {
 Tag tag = Tag.valueOf("");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(preTag.preserveWhitespace());
 assertTrue(textareaTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag buttonTag = Tag.valueOf("button");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(buttonTag.isFormListed());
 assertTrue(textareaTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(inputTag.isFormSubmittable());
 assertTrue(textareaTag.isFormSubmittable());
 }
}