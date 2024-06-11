import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());
 Assert.assertFalse(pTag.isInline());
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());
 Assert.assertFalse(pTag.preserveWhitespace());

 Assert.assertFalse(divTag.isInline());
 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());
 Assert.assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknown");

 Assert.assertFalse(unknownTag.isInline());
 Assert.assertTrue(unknownTag.isBlock());
 Assert.assertTrue(unknownTag.formatAsBlock());
 Assert.assertTrue(unknownTag.canContainBlock());
 Assert.assertTrue(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 imgTag.empty = true;

 Assert.assertFalse(imgTag.isInline());
 Assert.assertTrue(imgTag.isBlock());
 Assert.assertTrue(imgTag.formatAsBlock());
 Assert.assertFalse(imgTag.canContainBlock());
 Assert.assertFalse(imgTag.canContainInline());
 Assert.assertTrue(imgTag.isEmpty());
 Assert.assertFalse(imgTag.isSelfClosing());
 Assert.assertFalse(imgTag.preserveWhitespace());
 }

 @Test
 public void testSelfClosingTag() {
 Tag inputTag = Tag.valueOf("input");
 inputTag.setSelfClosing();

 Assert.assertFalse(inputTag.isInline());
 Assert.assertTrue(inputTag.isBlock());
 Assert.assertFalse(inputTag.formatAsBlock());
 Assert.assertFalse(inputTag.canContainBlock());
 Assert.assertFalse(inputTag.canContainInline());
 Assert.assertFalse(inputTag.isEmpty());
 Assert.assertTrue(inputTag.isSelfClosing());
 Assert.assertFalse(inputTag.preserveWhitespace());
 }

 @Test
 public void testKnownTag() {
 Tag htmlTag = Tag.valueOf("html");

 Assert.assertEquals("html", htmlTag.getName());
 Assert.assertTrue(htmlTag.isBlock());
 Assert.assertTrue(htmlTag.formatAsBlock());
 Assert.assertTrue(htmlTag.canContainBlock());
 Assert.assertTrue(htmlTag.canContainInline());
 Assert.assertFalse(htmlTag.isEmpty());
 Assert.assertFalse(htmlTag.isSelfClosing());
 Assert.assertFalse(htmlTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyMap() {
 Tag tag = new Tag("");
 Tag tag2 = new Tag("html");
 synchronized (Tag.tags) {
 Tag.tags.clear();
 }
 Tag unknownTag = Tag.valueOf("unknown");

 Assert.assertEquals(Tag.class, unknownTag.getClass());
 }
}