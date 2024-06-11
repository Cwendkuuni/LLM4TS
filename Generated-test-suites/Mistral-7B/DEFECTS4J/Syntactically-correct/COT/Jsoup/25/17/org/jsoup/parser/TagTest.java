package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.Tag.blockTags;
import static org.jsoup.parser.Tag.inlineTags;
import static org.jsoup.parser.Tag.emptyTags;
import static org.jsoup.parser.Tag.formatAsInlineTags;
import static org.jsoup.parser.Tag.preserveWhitespaceTags;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag scriptTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 scriptTag = Tag.valueOf("script");
 }

 @Test
 public void testTagName() {
 Tag tag = new Tag("testTag");
 String tagName = tag.getName();
 assertEquals("testTag", tagName);
 }

 @Test
 public void testValueOfBlockTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testValueOfInlineTag() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testValueOfEmptyTag() {
 Tag tag = Tag.valueOf("img");
 tag.setEmpty();
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testValueOfFormatAsBlockTag() {
 Tag tag = Tag.valueOf("p");
 tag.setFormatAsBlock(false);
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testValueOfCanContainBlockTag() {
 Tag tag = Tag.valueOf("p");
 tag.setCanContainBlock(false);
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testValueOfCanContainInlineTag() {
 Tag tag = Tag.valueOf("p");
 tag.setCanContainInline(false);
 assertFalse(tag.canContainInline());
 }

 @Test
 public void testValueOfPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("pre");
 tag.setPreserveWhitespace(true);
 assertTrue(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testIsKnownTagCaseInsensitive() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("P"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testGetBlockTags() {
 Tag[] expected = blockTags.clone();
 Tag[] actual = Tag.getBlockTags();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetInlineTags() {
 Tag[] expected = inlineTags.clone();
 Tag[] actual = Tag.getInlineTags();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetEmptyTags() {
 Tag[] expected = emptyTags.clone();
 Tag[] actual = Tag.getEmptyTags();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetFormatAsInlineTags() {
 Tag[] expected = formatAsInlineTags.clone();
 Tag[] actual = Tag.getFormatAsInlineTags();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetPreserveWhitespaceTags() {
 Tag[] expected = preserveWhitespaceTags.clone();
 Tag[] actual = Tag.getPreserveWhitespaceTags();
 assertArrayEquals(expected, actual);
 }

}