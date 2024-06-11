package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagCreationAndProperties() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 // Basic properties
 assertEquals("div", tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());

 // Check if it's a known tag
 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testUnknownTagCreation() {
 String tagName = "unknownTag";
 Tag tag = Tag.valueOf(tagName);

 // Basic properties
 assertEquals("unknownTag", tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());

 // Unknown tags can contain block and inline
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 }

 @Test
 public void testEmptyTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 tag.empty = true;

 // Basic properties
 assertEquals("img", tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testSelfClosingTag() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 tag.selfClosing = true;

 // Basic properties
 assertEquals("input", tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.isEmpty());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 tag.preserveWhitespace = true;

 // Basic properties
 assertEquals("pre", tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertTrue(tag.isEmpty());
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testCanContainBlockTag() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "div";
 Tag tag2 = Tag.valueOf(tagName2);

 // Check if p tag can contain div tag
 assertTrue(tag1.canContainBlock());
 assertTrue(tag1.canContain(tag2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotContainBlockTag() {
 String tagName1 = "input";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "div";
 Tag tag2 = Tag.valueOf(tagName2);

 // Check if input tag can't contain div tag
 assertFalse(tag1.canContainBlock());
 try {
 tag1.canContain(tag2);
 } catch (IllegalArgumentException e) {
 assertEquals("Tag 'input' can't contain 'div'", e.getMessage());
 }
 }

 @Test
 public void testCanContainInlineTag() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "span";
 Tag tag2 = Tag.valueOf(tagName2);

 // Check if p tag can contain span tag
 assertTrue(tag1.canContainInline());
 assertTrue(tag1.canContain(tag2));
 }

 @Test
 public void testCannotContainInlineTag() {
 String tagName1 = "pre";
 Tag tag1 = Tag.valueOf(tagName1);
 String tagName2 = "span";
 Tag tag2 = Tag.valueOf(tagName2);

 // Check if pre tag can't contain span tag
 assertFalse(tag1.canContainInline());
 assertFalse(tag1.canContain(tag2));
 }

 @Test
 public void testFormatAsBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 tag.formatAsBlock = false;

 // Check if p tag is formatted as block by default
 assertTrue(tag.formatAsBlock());

 // Change formatAsBlock to false
 assertFalse(tag.formatAsBlock());
 }
}