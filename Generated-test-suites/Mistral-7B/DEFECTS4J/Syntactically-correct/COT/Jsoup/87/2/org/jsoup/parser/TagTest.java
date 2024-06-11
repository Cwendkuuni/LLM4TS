import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {

 private static final String TAG_NAME_TEST = "test";
 private static final String TAG_NAME_TEST_LOWERCASE = "test";
 private static final String TAG_NAME_BLOCK = "html";
 private static final String TAG_NAME_INLINE = "span";
 private static final String TAG_NAME_EMPTY = "img";
 private static final String TAG_NAME_FORMAT_AS_BLOCK = "p";
 private static final String TAG_NAME_FORMAT_AS_INLINE = "a";
 private static final String TAG_NAME_PRESERVE_WHITESPACE = "pre";
 private static final String TAG_NAME_FORM_LISTED = "input";
 private static final String TAG_NAME_FORM_SUBMITTABLE = "form";

 @Test
 public void testTagIsBlock() {
 Tag tag = Tag.valueOf(TAG_NAME_BLOCK);
 Assert.assertTrue(tag.isBlock());
 }

 @Test
 public void testTagIsInline() {
 Tag tag = Tag.valueOf(TAG_NAME_INLINE);
 Assert.assertFalse(tag.isBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag tag = Tag.valueOf(TAG_NAME_EMPTY);
 Assert.assertTrue(tag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tag = Tag.valueOf(TAG_NAME_EMPTY);
 Assert.assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag tag = Tag.valueOf(TAG_NAME_BLOCK);
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_BLOCK));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = Tag.valueOf(TAG_NAME_PRESERVE_WHITESPACE);
 Assert.assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag tag = Tag.valueOf(TAG_NAME_FORM_LISTED);
 Assert.assertTrue(tag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tag = Tag.valueOf(TAG_NAME_FORM_SUBMITTABLE);
 Assert.assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfCaseInsensitive() {
 Tag tag = Tag.valueOf(TAG_NAME_TEST);
 Tag tagLowercase = Tag.valueOf(TAG_NAME_TEST_LOWERCASE);
 Assert.assertEquals(tag, tagLowercase);
 }

 @Test(expected = NullPointerException.class)
 public void testTagValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = Tag.valueOf("div");
 Assert.assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag tag = Tag.valueOf("span");
 Assert.assertTrue(tag.canContainInline());
 }

 @Test
 public void testTagIsData() {
 Tag tag = Tag.valueOf("script");
 Assert.assertTrue(tag.isData());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = Tag.valueOf(TAG_NAME_BLOCK);
 Tag tag2 = Tag.valueOf(TAG_NAME_BLOCK);
 Tag tag3 = new Tag(TAG_NAME_BLOCK);
 Assert.assertEquals(tag1, tag2);
 Assert.assertEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = Tag.valueOf(TAG_NAME_BLOCK);
 Tag tag2 = Tag.valueOf(TAG_NAME_BLOCK);
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testTagToString() {
 Tag tag = Tag.valueOf(TAG_NAME_BLOCK);
 Assert.assertEquals(TAG_NAME_BLOCK, tag.toString());
 }
}