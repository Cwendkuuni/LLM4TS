import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 // Test getting tag's name
 Assert.assertEquals(tagName, tag.getName());

 // Test getting tag's properties
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());

 // Test getting unknown tag
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);

 // Test getting unknown tag's properties
 Assert.assertEquals(unknownTagName, unknownTag.getName());
 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertTrue(unknownTag.canContainBlock());
 Assert.assertTrue(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 // Test known tag
 Assert.assertTrue(Tag.isKnownTag(tagName));

 // Test unknown tag
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);

 // Test unknown tag
 Assert.assertFalse(Tag.isKnownTag(unknownTagName));
 }

 @Test
 public void testTagIsBlock() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 // Test block tag
 Assert.assertTrue(tag1.isBlock());

 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);

 // Test inline tag
 Assert.assertFalse(tag2.isBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 // Test can contain block tag
 Assert.assertTrue(tag1.canContainBlock());

 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);

 // Test cannot contain block tag
 Assert.assertFalse(tag2.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 // Test can contain inline tag
 Assert.assertTrue(tag1.canContainInline());

 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);

 // Test cannot contain inline tag
 Assert.assertFalse(tag2.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 // Test non-empty tag
 Assert.assertFalse(tag1.isEmpty());

 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);

 // Test empty tag
 Assert.assertTrue(tag2.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 // Test non-self closing tag
 Assert.assertFalse(tag1.isSelfClosing());

 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);

 // Test self closing tag
 Assert.assertTrue(tag2.isSelfClosing());
 }

 @Test
 public void testTagPreserveWhitespace() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);

 // Test non-preserve whitespace tag
 Assert.assertFalse(tag1.preserveWhitespace());

 String tagName2 = "pre";
 Tag tag2 = Tag.valueOf(tagName2);

 // Test preserve whitespace tag
 Assert.assertTrue(tag2.preserveWhitespace());
 }

 @Test
 public void testTagSetSelfClosing() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 // Set self-closing tag
 Tag selfClosingTag = tag.setSelfClosing();

 // Test self-closing tag
 Assert.assertTrue(selfClosingTag.isSelfClosing());
 }
}