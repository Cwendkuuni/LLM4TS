import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTag_ValidConstructor() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testTag_getValueOf_ValidTagName() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Assert.assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testTag_getValueOf_UnknownTagName() {
 String unknownTagName = "unknownTag";
 Tag tag = Tag.valueOf(unknownTagName);
 Assert.assertFalse(tag.isKnownTag());
 Assert.assertEquals(unknownTagName, tag.getName());
 }

 @Test
 public void testTag_isBlock_Default() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertTrue(tag.isBlock());
 }

 @Test
 public void testTag_isBlock_False() {
 Tag tag = new Tag("p");
 Assert.assertFalse(tag.isBlock());
 }

 @Test
 public void testTag_formatAsBlock_Default() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testTag_formatAsBlock_False() {
 Tag tag = new Tag("p");
 tag.formatAsBlock(false);
 Assert.assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testTag_canContainBlock_Default() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTag_canContainBlock_False() {
 Tag tag = new Tag("input");
 Assert.assertFalse(tag.canContainBlock());
 }

 @Test
 public void testTag_canContainInline_Default() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertTrue(tag.canContainInline());
 }

 @Test
 public void testTag_canContainInline_False() {
 Tag tag = new Tag("script");
 Assert.assertFalse(tag.canContainInline());
 }

 @Test
 public void testTag_isEmpty_Default() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertFalse(tag.isEmpty());
 }

 @Test
 public void testTag_isEmpty_True() {
 Tag tag = new Tag("img");
 tag.empty = true;
 Assert.assertTrue(tag.isEmpty());
 }

 @Test
 public void testTag_isSelfClosing_Default() {
 Tag tag = new Tag(TAG_NAME);
 Assert.assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testTag_isSelfClosing_True() {
 Tag tag = new Tag(TAG_NAME);
 tag.setSelfClosing();
 Assert.assertTrue(tag.isSelfClosing());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_setValueOf_InvalidTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_setValueOf_EmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = NullPointerException.class)
 public void testTag_setValueOf_NullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_register_NullTag() {
 Tag tag = null;
 Tag.register(tag);
 }
}