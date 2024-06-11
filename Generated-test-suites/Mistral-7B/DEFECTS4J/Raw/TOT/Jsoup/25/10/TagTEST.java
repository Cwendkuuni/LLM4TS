import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagCreation() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testUnknownTagCreation() {
 String tagName = "unknown";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals("unknown", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 Assert.assertNotEquals(Tag.valueOf("p"), tag);
 }

 @Test
 public void testBlockTag() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testInlineTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testEmptyTag() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertTrue(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testSelfClosingTag() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName).setSelfClosing();

 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertTrue(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertTrue(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertTrue(tag.preserveWhitespace());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 String tagName = null;
 Tag tag = Tag.valueOf(tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 String tagName = "<script>";
 Tag tag = Tag.valueOf(tagName);
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");

 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertTrue(Tag.isKnownTag("script"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }
}