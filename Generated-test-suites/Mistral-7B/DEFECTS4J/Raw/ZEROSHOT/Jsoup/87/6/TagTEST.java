package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNameNormalization() {
 Tag tag = Tag.valueOf("p", ParseSettings.preserveCase);
 assertEquals("p", tag.getName());

 Tag tag1 = Tag.valueOf("P", ParseSettings.preserveCase);
 assertEquals("p", tag1.getName());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());
 assertTrue(pTag.canContainBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isBlock());
 assertFalse(inputTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isInline());

 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isData());

 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isEmpty());

 Tag imgTagEmpty = Tag.valueOf("img", ParseSettings.emptyImages);
 assertTrue(imgTagEmpty.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());

 Tag imgTagSelfClosing = Tag.valueOf("img", ParseSettings.preserveSelfClosingTags);
 assertTrue(imgTagSelfClosing.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormListed());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormSubmittable());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isFormSubmittable());
 }

}