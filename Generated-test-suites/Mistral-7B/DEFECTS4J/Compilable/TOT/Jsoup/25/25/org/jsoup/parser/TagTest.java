package org.jsoup.parser;
import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOfKnownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isKnownTag());
 assertEquals(tagName, tag.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
 String tagName = "mytag";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isKnownTag());
 assertEquals(tagName.toLowerCase(), tag.getName());
 }

 @Test
 public void testIsBlockForKnownBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlockForUnknownTag() {
 String tagName = "mytag";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockForKnownBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlockForKnownInlineTag() {
 String tagName = "span";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockForKnownBlockTag() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainBlockForKnownInlineTag() {
 String tagName = "span";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testIsEmptyForEmptyTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsEmptyForNonEmptyTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosingForSelfClosingTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingForNonSelfClosingTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagForKnownTag() {
 String tagName = "p";
 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testIsKnownTagForUnknownTag() {
 String tagName = "mytag";
 assertFalse(Tag.isKnownTag(tagName));
 }

 @Test
 public void testPreserveWhitespaceForPreserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceForNonPreserveWhitespaceTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.preserveWhitespace());
 }
}