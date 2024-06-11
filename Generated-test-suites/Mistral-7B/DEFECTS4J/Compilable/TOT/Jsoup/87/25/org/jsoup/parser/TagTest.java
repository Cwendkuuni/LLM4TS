package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag pTagNormalized = Tag.valueOf("p", ParseSettings.preserveCase);
 Tag unknownTag = Tag.valueOf("table23");

 assertEquals("p", pTag.getName());
 assertEquals("p", pTagNormalized.getName());
 assertNotEquals(pTag, unknownTag);
 assertTrue(pTag.equals(pTagNormalized));
 assertFalse(pTag.equals(unknownTag));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.formatAsBlock());
 assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testTagIsInline() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertFalse(pTag.isInline());
 assertTrue(brTag.isInline());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(scriptTag.isData());
 assertFalse(inputTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag hrTag = Tag.valueOf("hr");

 assertFalse(imgTag.isSelfClosing());
 assertTrue(hrTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("table23");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("table23"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag buttonTag = Tag.valueOf("button");

 assertTrue(buttonTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");

 assertTrue(inputTag.isFormSubmittable());
 }
}