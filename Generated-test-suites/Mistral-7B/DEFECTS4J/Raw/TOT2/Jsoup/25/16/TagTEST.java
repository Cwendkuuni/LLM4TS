import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = new Tag("div");
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
 public void testTagValueOf() {
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
 public void testTagIsBlock() {
 Tag tag = new Tag("p");
 Assert.assertTrue(tag.isBlock());

 tag = new Tag("input");
 Assert.assertFalse(tag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = new Tag("p");
 Assert.assertTrue(tag.formatAsBlock());

 tag = new Tag("input");
 Assert.assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = new Tag("p");
 Assert.assertTrue(tag.canContainBlock());

 tag = new Tag("input");
 Assert.assertFalse(tag.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag tag = new Tag("p");
 Assert.assertTrue(tag.canContainInline());

 tag = new Tag("input");
 Assert.assertFalse(tag.canContainInline());
 }

 @Test
 public void testTagEmpty() {
 Tag tag = new Tag("img");
 Assert.assertFalse(tag.isEmpty());

 tag = new Tag("input");
 Assert.assertFalse(tag.isEmpty());

 tag = new Tag("img").setSelfClosing();
 Assert.assertTrue(tag.isEmpty());

 tag = new Tag("input").setSelfClosing();
 Assert.assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tag = new Tag("img");
 Assert.assertFalse(tag.isSelfClosing());

 tag = new Tag("img").setSelfClosing();
 Assert.assertTrue(tag.isSelfClosing());

 tag = new Tag("input");
 Assert.assertFalse(tag.isSelfClosing());

 tag = new Tag("input").setSelfClosing();
 Assert.assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("x"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = new Tag("pre");
 Assert.assertTrue(tag.preserveWhitespace());

 tag = new Tag("p");
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNullName() {
 new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagEmptyName() {
 new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagInvalidTagName() {
 new Tag("&tag");
 }

 @Test
 public void testTagSynchronizedGet() {
 String tagName = "div";
 Tag tag1 = Tag.valueOf(tagName);
 Tag tag2 = Tag.valueOf(tagName);
 Assert.assertSame(tag1, tag2);
 }
}