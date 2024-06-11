package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagName() {
 Tag pTag = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("h1");

 assertEquals("p", pTag.getName());
 assertEquals("h1", h1Tag.getName());
 }

 @Test
 public void testIsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.isBlock());
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.formatAsBlock());
 assertFalse(spanTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(spanTag.canContainBlock());
 }

 @Test
 public void testIsEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isSelfClosing());
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 assertTrue(preTag.preserveWhitespace());
 assertFalse(pTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = new Tag("!invalidTag");
 }
}