package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag divTag;

 @Before
 public void setUp() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 divTag = Tag.valueOf("div");
 }

 @Test
 public void testValueOfValidTag() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test
 public void testValueOfInvalidTag() {
 Tag tag = Tag.valueOf("invalidTag");
 Assert.assertFalse(Tag.isKnownTag("invalidTag"));
 Assert.assertFalse(tag.isBlock());
 }

 @Test
 public void testIsBlockTrue() {
 Assert.assertTrue(pTag.isBlock());
 }

 @Test
 public void testIsBlockFalse() {
 imgTag.isBlock = false;
 Assert.assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlockTrue() {
 Assert.assertTrue(pTag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlockFalse() {
 divTag.formatAsBlock = false;
 Assert.assertFalse(divTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTrue() {
 Assert.assertTrue(pTag.canContainBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 imgTag.canContainBlock = false;
 Assert.assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsEmptyTrue() {
 imgTag.empty = true;
 Assert.assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testIsEmptyFalse() {
 Assert.assertFalse(pTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTrue() {
 imgTag.selfClosing = true;
 Assert.assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingFalse() {
 Assert.assertFalse(pTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagTrue() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 }

 @Test
 public void testIsKnownTagFalse() {
 Assert.assertFalse(Tag.isKnownTag("invalidTag"));
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 pTag.preserveWhitespace = true;
 Assert.assertTrue(pTag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceFalse() {
 divTag.preserveWhitespace = false;
 Assert.assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 Tag tag4 = tag1;

 Assert.assertEquals(tag1, tag1);
 Assert.assertEquals(tag1, tag2);
 Assert.assertNotEquals(tag1, tag3);
 Assert.assertNotEquals(tag1, tag4);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 Assert.assertEquals(tag1.hashCode(), tag1.hashCode());
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("p", pTag.toString());
 }

 @Test
 public void testBlockTags() {
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 Assert.assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testInlineTags() {
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 Assert.assertFalse(tag.isBlock());
 }
 }

 @Test
 public void testEmptyTags() {
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 Assert.assertTrue(tag.isEmpty());
 }
 }

 @Test
 public void testFormatAsInlineTags() {
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 Assert.assertFalse(tag.formatAsBlock());
 }
 }

 @Test
 public void testPreserveWhitespaceTags() {
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 Assert.assertTrue(tag.preserveWhitespace());
 }
 }
}