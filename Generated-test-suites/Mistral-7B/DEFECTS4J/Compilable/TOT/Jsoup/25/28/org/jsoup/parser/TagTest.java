package org.jsoup.parser;

import org.junit.Test;
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
 String tagName = "abc123";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isKnownTag());
 assertEquals(tagName.toLowerCase(), tag.getName());
 }

 @Test
 public void testIsBlockTrue() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlockFalse() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockTrue() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlockFalse() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTrue() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testIsEmptyTrue() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsEmptyFalse() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTrue() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingFalse() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagTrue() {
 String tagName = "p";
 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testIsKnownTagFalse() {
 String tagName = "xyz";
 assertFalse(Tag.isKnownTag(tagName));
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceFalse() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullName() {
 Tag tag = Tag.valueOf(null);
 fail("expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyName() {
 Tag tag = Tag.valueOf("");
 fail("expected IllegalArgumentException");
 }
}