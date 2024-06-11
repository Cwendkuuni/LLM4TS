import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag DIV = Tag.valueOf("div");

 @Test
 public void testTagName() {
 Tag tag = new Tag("tagName");
 Assert.assertEquals("tagName", tag.getName());
 }

 @Test
 public void testTagNameNormalized() {
 Tag tag = Tag.valueOf("TagName");
 Assert.assertEquals("tagname", tag.getName());
 }

 @Test
 public void testValueOfPredefinedTag() {
 Tag tag = HTML;
 Assert.assertEquals(HTML, tag);
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("UNKNOWN");
 Assert.assertNotEquals(Tag.valueOf("UNKNOWN"), tag);
 Assert.assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlock() {
 Assert.assertTrue(HTML.isBlock());
 Assert.assertTrue(HEAD.isBlock());
 Assert.assertTrue(BODY.isBlock());
 Assert.assertFalse(P.isBlock());
 Assert.assertFalse(DIV.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Assert.assertTrue(HTML.formatAsBlock());
 Assert.assertTrue(HEAD.formatAsBlock());
 Assert.assertTrue(BODY.formatAsBlock());
 Assert.assertFalse(P.formatAsBlock());
 Assert.assertFalse(DIV.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Assert.assertTrue(HTML.canContainBlock());
 Assert.assertTrue(HEAD.canContainBlock());
 Assert.assertTrue(BODY.canContainBlock());
 Assert.assertFalse(P.canContainBlock());
 Assert.assertFalse(DIV.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Assert.assertFalse(HTML.isInline());
 Assert.assertFalse(HEAD.isInline());
 Assert.assertFalse(BODY.isInline());
 Assert.assertTrue(P.isInline());
 Assert.assertTrue(DIV.isInline());
 }

 @Test
 public void testIsData() {
 Assert.assertFalse(HTML.isData());
 Assert.assertFalse(HEAD.isData());
 Assert.assertFalse(BODY.isData());
 Assert.assertFalse(P.isData());
 Assert.assertTrue(DIV.isData());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(HTML.isEmpty());
 Assert.assertFalse(HEAD.isEmpty());
 Assert.assertFalse(BODY.isEmpty());
 Assert.assertFalse(P.isEmpty());
 Assert.assertFalse(DIV.isEmpty());
 }

 @Test
 public void testIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("html"));
 Assert.assertTrue(Tag.isKnownTag("head"));
 Assert.assertFalse(Tag.isKnownTag("UNKNOWN"));
 }

 @Test
 public void testPreserveWhitespace() {
 Assert.assertFalse(HTML.preserveWhitespace());
 Assert.assertFalse(HEAD.preserveWhitespace());
 Assert.assertFalse(BODY.preserveWhitespace());
 Assert.assertFalse(P.preserveWhitespace());
 Assert.assertTrue(Tag.valueOf("pre").preserveWhitespace());
 Assert.assertTrue(Tag.valueOf("plaintext").preserveWhitespace());
 }

 @Test(expected = AssertionError.class)
 public void testIsFormListed() {
 Tag unknownTag = Tag.valueOf("UNKNOWN");
 Assert.assertFalse(unknownTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Assert.assertFalse(Tag.valueOf("html").isFormSubmittable());
 Assert.assertFalse(Tag.valueOf("head").isFormSubmittable());
 Assert.assertTrue(Tag.valueOf("input").isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }
}