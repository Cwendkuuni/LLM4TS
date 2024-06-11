import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertEquals("div", tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testUnknownTagCreation() {
 String tagName = "myCustomTag";
 Tag tag = Tag.valueOf(tagName);
 assertEquals("myCustomTag", tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testInlineTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertTrue(tag.canContainBlock()); // can contain other inline tags
 assertFalse(tag.canContainInline()); // can only contain pcdata
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testEmptyTag() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertTrue(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testSelfClosingTag() {
 String tagName = "img";
 Tag tag = new Tag(tagName).setSelfClosing();
 assertFalse(tag.isEmpty());
 assertTrue(tag.isSelfClosing());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhitespaceOnlyName() {
 Tag tag = new Tag(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = new Tag("&tag"); // invalid tag name
 }
}