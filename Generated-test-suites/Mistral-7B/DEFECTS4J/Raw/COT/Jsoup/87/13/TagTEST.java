import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagName() {
 Tag tag = new Tag(TAG_NAME);
 String name = tag.getName();
 assertEquals(TAG_NAME, name);
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 String name = tag.getName();
 assertEquals(TAG_NAME, name);
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag(TAG_NAME);
 tag.isBlock = false;
 assertFalse(tag.isBlock());

 Tag blockTag = new Tag("p");
 assertTrue(blockTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = new Tag(TAG_NAME);
 tag.formatAsBlock = false;
 assertFalse(tag.formatAsBlock());

 Tag blockTag = new Tag("p");
 assertTrue(blockTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 // deprecated
 Tag tag = new Tag(TAG_NAME);
 tag.isBlock = false;
 assertFalse(tag.canContainBlock());

 Tag blockTag = new Tag("p");
 assertTrue(blockTag.isBlock);
 assertTrue(blockTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag(TAG_NAME);
 tag.empty = true;
 assertTrue(tag.isEmpty());

 Tag nonEmptyTag = new Tag("p");
 assertFalse(nonEmptyTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag tag = new Tag(TAG_NAME);
 tag.selfClosing = true;
 assertTrue(tag.isSelfClosing());

 Tag nonSelfClosingTag = new Tag("p");
 assertFalse(nonSelfClosingTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag tag = new Tag(TAG_NAME);
 assertFalse(Tag.isKnownTag(TAG_NAME));

 Tag knownTag = new Tag("p");
 assertTrue(Tag.isKnownTag(knownTag.getName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag(TAG_NAME);
 tag.preserveWhitespace = true;
 assertTrue(tag.preserveWhitespace());

 Tag nonPreserveWhitespaceTag = new Tag("p");
 assertFalse(nonPreserveWhitespaceTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag tag = new Tag(TAG_NAME);
 tag.formList = true;
 assertTrue(tag.isFormListed());

 Tag nonFormListedTag = new Tag("p");
 assertFalse(nonFormListedTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag tag = new Tag(TAG_NAME);
 tag.formSubmit = true;
 assertTrue(tag.isFormSubmittable());

 Tag nonFormSubmittableTag = new Tag("p");
 assertFalse(nonFormSubmittableTag.isFormSubmittable());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));

 Tag tag3 = new Tag("div");
 assertFalse(tag1.equals(tag3));

 Tag tag4 = tag1;
 assertTrue(tag1.equals(tag4));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());

 Tag tag3 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag3.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}