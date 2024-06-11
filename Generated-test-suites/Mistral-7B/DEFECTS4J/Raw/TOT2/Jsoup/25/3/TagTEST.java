import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 String expectedTagName = "div";

 assertEquals(expectedTagName, tag.getName());
 assertTrue(tag.isInline());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagValueOfUnknown() {
 String tagName = "unknown";
 Tag tag = Tag.valueOf(tagName);
 String expectedTagName = "unknown";

 assertEquals(expectedTagName, tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testTagIsBlock() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 assertTrue(tag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 String tagName = "span";
 Tag tag = Tag.valueOf(tagName);
 tag.formatAsBlock(false);

 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagIsData() {
 String tagName = "script";
 Tag tag = Tag.valueOf(tagName);

 assertTrue(tag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 tag.empty = true;

 assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 tag.selfClosing = true;

 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName = "p";

 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testTagPreserveWhitespace() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 tag.preserveWhitespace = true;

 assertTrue(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 String tagName = null;

 try {
 Tag tag = Tag.valueOf(tagName);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 String tagName = "";

 try {
 Tag tag = Tag.valueOf(tagName);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhitespaceTagName() {
 String tagName = " \t html";

 try {
 Tag tag = Tag.valueOf(tagName);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameTooLong() {
 String tagName = "tagNameWithLengthGreaterThan64";

 try {
 Tag tag = Tag.valueOf(tagName);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}