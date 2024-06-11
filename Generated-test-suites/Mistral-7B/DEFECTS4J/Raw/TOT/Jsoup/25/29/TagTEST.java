package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagCreation() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals("div", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagUnknownCreation() {
 String tagName = "unknownTag";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals("unknownTag", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testIsKnownTag() {
 String tagName1 = "div";
 String tagName2 = "unknownTag";

 Assert.assertTrue(Tag.isKnownTag(tagName1));
 Assert.assertFalse(Tag.isKnownTag(tagName2));
 }

 @Test
 public void testCanContainBlockTag() {
 String tagName1 = "div";
 String tagName2 = "p";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertTrue(tag1.canContainBlock());
 Assert.assertTrue(tag1.canContain(tag2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotContainBlockTag() {
 String tagName1 = "p";
 String tagName2 = "div";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 tag1.canContain(tag2);
 }

 @Test
 public void testCanContainInlineTag() {
 String tagName1 = "div";
 String tagName2 = "span";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertTrue(tag1.canContainInline());
 Assert.assertTrue(tag1.canContain(tag2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotContainInlineTag() {
 String tagName1 = "div";
 String tagName2 = "div";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 tag1.canContain(tag2);
 }

 @Test
 public void testIsEmptyTag() {
 String tagName1 = "img";
 String tagName2 = "input";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertFalse(tag1.isEmpty());
 Assert.assertFalse(tag1.isSelfClosing());
 Assert.assertFalse(tag2.isEmpty());
 Assert.assertFalse(tag2.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingTag() {
 String tagName1 = "img";
 String tagName2 = "input";
 String tagName3 = "br";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 Tag tag3 = Tag.valueOf(tagName3);

 Assert.assertFalse(tag1.isSelfClosing());
 Assert.assertFalse(tag2.isSelfClosing());
 Assert.assertTrue(tag3.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 String tagName1 = "pre";
 String tagName2 = "p";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertTrue(tag1.preserveWhitespace());
 Assert.assertFalse(tag2.preserveWhitespace());
 }

 @Test
 public void testFormatAsBlockTag() {
 String tagName1 = "p";
 String tagName2 = "pre";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertTrue(tag1.formatAsBlock());
 Assert.assertTrue(tag2.formatAsBlock());
 }

 @Test
 public void testFormatAsInlineTag() {
 String tagName1 = "p";
 String tagName2 = "a";

 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);

 Assert.assertTrue(tag1.formatAsBlock());
 Assert.assertFalse(tag2.formatAsBlock());
 }
}