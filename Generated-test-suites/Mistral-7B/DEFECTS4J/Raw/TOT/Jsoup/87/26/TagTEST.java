package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag pTagLower = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("p", pTag.getName());
 assertEquals("p", pTagLower.getName());
 assertEquals("div", divTag.getName());
 assertNotEquals("p", divTag.getName());
 assertEquals(Tag.class, unknownTag.getClass());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertFalse(spanTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(divTag.isBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());
 assertFalse(spanTag.canContainBlock());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag styleTag = Tag.valueOf("style");
 Tag textareaTag = Tag.valueOf("textarea");

 assertFalse(scriptTag.isData());
 assertFalse(styleTag.isData());
 assertTrue(textareaTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(brTag.isEmpty());
 assertTrue(inputTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag selfClosingTag = Tag.valueOf("input");
 Tag pTag = Tag.valueOf("p");

 assertFalse(imgTag.isSelfClosing());
 assertTrue(selfClosingTag.isSelfClosing());
 assertFalse(pTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknown");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNull() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 }
}