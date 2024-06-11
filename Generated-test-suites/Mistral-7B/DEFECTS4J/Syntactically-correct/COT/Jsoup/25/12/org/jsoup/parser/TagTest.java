package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testValueOf_knownTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isKnownTag());
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOf_unknownTag() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertFalse(tag.isKnownTag());
 assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testIsBlock_default() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlock_setToFalse() {
 Tag tag = new Tag(TAG_NAME).setInline();
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlock_default() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlock_setToFalse() {
 Tag tag = new Tag(TAG_NAME).setFormatAsInline();
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock_default() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainBlock_setToFalse() {
 Tag tag = new Tag(TAG_NAME).setCannotContainBlock();
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testCanContainInline_default() {
 Tag tag = new Tag(TAG_NAME);
 assertTrue(tag.canContainInline());
 }

 @Test
 public void testCanContainInline_setToFalse() {
 Tag tag = new Tag(TAG_NAME).setDataOnly();
 assertFalse(tag.canContainInline());
 }

 @Test
 public void testIsEmpty_default() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsEmpty_setToTrue() {
 Tag tag = new Tag(TAG_NAME).setEmpty();
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing_default() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosing_setToTrue() {
 Tag tag = new Tag(TAG_NAME).setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test(expected = NullPointerException.class)
 public void testValueOf_nullTagNameThrowsException() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOf_emptyTagNameThrowsException() {
 Tag tag = Tag.valueOf("");
 }

 @Test
 public void testIsKnownTag_knownTag() {
 assertTrue(Tag.isKnownTag("p"));
 }

 @Test
 public void testIsKnownTag_unknownTag() {
 assertFalse(Tag.isKnownTag(TAG_NAME));
 }

 @Test
 public void testPreserveWhitespace_default() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespace_setToTrue() {
 Tag tag = new Tag(TAG_NAME).setPreserveWhitespace();
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testEquals_sameTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testEquals_differentTagNames() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEquals_differentState() {
 Tag tag1 = new Tag("p").setBlock().setPreserveWhitespace();
 Tag tag2 = new Tag("p");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testHashCode_sameTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testHashCode_differentTags() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testHashCode_sameStateDifferentTags() {
 Tag tag1 = new Tag("p").setBlock().setPreserveWhitespace();
 Tag tag2 = new Tag("div").setBlock().setPreserveWhitespace();
 assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }
}