package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.Tag.blockTags;
import static org.jsoup.parser.Tag.emptyTags;
import static org.jsoup.parser.Tag.formatAsBlockTags;
import static org.jsoup.parser.Tag.formatAsInlineTags;
import static org.jsoup.parser.Tag.inlineTags;

public class TagTest {

 private Tag tag;

 @Before
 public void setUp() {
 tag = Tag.valueOf("div");
 }

 @Test
 public void testGetName() {
 String expectedName = "div";
 assertEquals(expectedName, tag.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
 String tagName = "mytag";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 assertTrue(tag.isData());
 }

 @Test
 public void testIsBlock() {
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsFormatAsBlock() {
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 assertFalse(tag.isInline());
 }

 @Test
 public void testIsData() {
 assertFalse(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 }

 @Test
 public void testIsKnownTagUnknown() {
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testSetSelfClosing() {
 tag = tag.setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test(expected = NullPointerException.class)
 public void testValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testValueOfUpperCaseTagName() {
 Tag tag = Tag.valueOf("DIV");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testValueOfEmptyTagWithCanContainBlockAndCanContainInline() {
 Tag tag = Tag.valueOf("");
 tag.canContainBlock = true;
 tag.canContainInline = true;
 assertFalse(tag.isData());
 }

 @Test
 public void testValueOfEmptyTagWithCanContainBlockFalseAndCanContainInlineFalse() {
 Tag tag = Tag.valueOf("");
 tag.canContainBlock = false;
 tag.canContainInline = false;
 assertTrue(tag.isData());
 }

 @Test
 public void testValueOfBlockTagName() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testValueOfInlineTagName() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }

 @Test
 public void testValueOfEmptyTagNameWithCanContainBlockFalse() {
 String tagName = "";
 Tag tag = new Tag(tagName);
 tag.canContainBlock = false;
 tag.canContainInline = false;
 assertTrue(tag.isData());
 }

 @Test
 public void testValueOfEmptyTagNameWithCanContainBlockTrue() {
 String tagName = "";
 Tag tag = new Tag(tagName);
 tag.canContainBlock = true;
 tag.canContainInline = true;
 assertFalse(tag.isData());
 }

 @Test
 public void testToString() {
 String expected = "div";
 assertEquals(expected, tag.toString());
 }

 @Test
 public void testEquals() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testEqualsDifferentTags() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("p");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsNull() {
 Tag tag = Tag.valueOf("div");
 assertFalse(tag.equals(null));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 int expected = tag1.hashCode();
 int actual = tag2.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeDifferentTags() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("p");
 int expected1 = tag1.hashCode();
 int expected2 = tag2.hashCode();
 assertNotEquals(expected1, expected2);
 }

}