import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testgetValueOfBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testgetValueOfInlineTag() {
 String tagName = "span";
 Tag tag = Tag.getValueOf(tagName);
 assertFalse(tag.isBlock());
 }

 @Test
 public void testgetValueOfEmptyTag() {
 String tagName = "img";
 Tag tag = Tag.getValueOf(tagName);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testgetValueOfFormatAsBlockTag() {
 String tagName = "pre";
 Tag tag = Tag.getValueOf(tagName);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testgetValueOfUnknownTag() {
 String tagName = "mytag";
 Tag tag = Tag.getValueOf(tagName);
 assertFalse(tag.isKnownTag());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetValueOfNullTagName() {
 String tagName = null;
 Tag tag = Tag.getValueOf(tagName);
 }

 @Test
 public void testcanContainBlock_blockTag() {
 String tagName1 = "p";
 String tagName2 = "div";
 Tag tag1 = Tag.getValueOf(tagName1);
 Tag tag2 = Tag.getValueOf(tagName2);
 assertTrue(tag2.canContainBlock());
 assertTrue(tag1.canContainBlock());
 assertTrue(tag1.canContain(tag2));
 }

 @Test
 public void testcanContainBlock_inlineTag() {
 String tagName1 = "span";
 String tagName2 = "p";
 Tag tag1 = Tag.getValueOf(tagName1);
 Tag tag2 = Tag.getValueOf(tagName2);
 assertFalse(tag1.canContainBlock());
 assertFalse(tag2.canContainBlock());
 assertFalse(tag2.canContain(tag1));
 }

 @Test
 public void testpreserveWhitespace_preserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.getValueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testpreserveWhitespace_nonPreserveWhitespaceTag() {
 String tagName = "span";
 Tag tag = Tag.getValueOf(tagName);
 assertFalse(tag.preserveWhitespace());
 }
}