package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());

 // Test case insensitivity
 Tag PTag = Tag.valueOf("P");
 Tag DivTag = Tag.valueOf("DIV");

 assertEquals("p", PTag.getName());
 assertEquals("div", DivTag.getName());
 }

 @Test
 public void testTagFormatting() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());

 Tag pInlineTag = Tag.valueOf("p").setFormatAsBlock(false);
 assertFalse(pInlineTag.formatAsBlock());

 Tag divInlineTag = Tag.valueOf("div").setFormatAsBlock(false);
 assertFalse(divInlineTag.formatAsBlock());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());

 Tag pInlineTag = Tag.valueOf("p").setIsBlock(false);
 assertFalse(pInlineTag.isBlock());

 Tag divInlineTag = Tag.valueOf("div").setIsBlock(false);
 assertFalse(divInlineTag.isBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(inputTag.isEmpty());

 Tag imgEmptyTag = Tag.valueOf("img").setEmpty(true);
 assertTrue(imgEmptyTag.isEmpty());

 Tag inputEmptyTag = Tag.valueOf("input").setEmpty(true);
 assertTrue(inputEmptyTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(inputTag.isSelfClosing());

 Tag imgSelfClosingTag = Tag.valueOf("img").setSelfClosing(true);
 assertTrue(imgSelfClosingTag.isSelfClosing());

 Tag inputSelfClosingTag = Tag.valueOf("input").setSelfClosing(true);
 assertTrue(inputSelfClosingTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());

 Tag preTagNoPreserveWhitespace = Tag.valueOf("pre").setPreserveWhitespace(false);
 assertFalse(preTagNoPreserveWhitespace.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = Tag.valueOf("input");

 assertTrue(inputTag.isFormListed());

 Tag inputTagNoFormListed = Tag.valueOf("input").setFormList(false);
 assertFalse(inputTagNoFormListed.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");

 assertTrue(inputTag.isFormSubmittable());

 Tag inputTagNoFormSubmittable = Tag.valueOf("input").setFormSubmit(false);
 assertFalse(inputTagNoFormSubmittable.isFormSubmittable());
 }
}