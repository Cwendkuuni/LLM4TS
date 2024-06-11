import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 // Test for the getName() method
 @Test
 public void testTaggetName() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 }

 // Test for the valueOf() method with known tag
 @Test
 public void testTagValueOfKnownTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isKnownTag());
 assertEquals("p", tag.getName());
 }

 // Test for the valueOf() method with unknown tag
 @Test
 public void testTagValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknown");
 assertFalse(tag.isKnownTag());
 assertEquals("unknown", tag.getName());
 }

 // Test for the isBlock() method
 @Test
 public void testTagIsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 Tag tagInline = new Tag("span");
 assertFalse(tagInline.isBlock());
 }

 // Test for the formatAsBlock() method
 @Test
 public void testTagFormatAsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.formatAsBlock());
 Tag tagInline = new Tag("span");
 assertTrue(tagInline.formatAsBlock());
 }

 // Test for the canContainInline() method
 @Test
 public void testTagCanContainInline() {
 Tag tag = new Tag("p");
 assertTrue(tag.canContainInline());
 Tag tagEmpty = new Tag("img");
 assertFalse(tagEmpty.canContainInline());
 }

 // Test for the isData() method
 @Test
 public void testTagIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 Tag tagEmpty = new Tag("p");
 assertFalse(tagEmpty.isData());
 }

 // Test for the isEmpty() method
 @Test
 public void testTagIsEmpty() {
 Tag tagEmpty = new Tag("img");
 assertTrue(tagEmpty.isEmpty());
 Tag tag = new Tag("p");
 assertFalse(tag.isEmpty());
 }

 // Test for the isSelfClosing() method
 @Test
 public void testTagIsSelfClosing() {
 Tag tagSelfClosing = new Tag("img");
 assertTrue(tagSelfClosing.isSelfClosing());
 Tag tag = new Tag("p");
 assertFalse(tag.isSelfClosing());
 }

 // Test for the isKnownTag() method
 @Test
 public void testTagIsKnownTag() {
 Tag tag = new Tag("p");
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 // Test for the preserveWhitespace() method
 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 Tag tagInline = new Tag("span");
 assertFalse(tagInline.preserveWhitespace());
 }

 // Test for the isFormListed() method
 @Test
 public void testTagIsFormListed() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormListed());
 Tag tagNotFormListed = new Tag("p");
 assertFalse(tagNotFormListed.isFormListed());
 }

 // Test for the isFormSubmittable() method
 @Test
 public void testTagIsFormSubmittable() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormSubmittable());
 Tag tagNotFormSubmittable = new Tag("p");
 assertFalse(tagNotFormSubmittable.isFormSubmittable());
 }

 // Test for the equals() method
 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 Tag tag3 = new Tag("div");
 assertFalse(tag1.equals(tag3));
 }

 // Test for the hashCode() method
 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 Tag tag3 = new Tag("div");
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 // Test for the toString() method
 @Test
 public void testTagToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }

 // Test for edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testTagNullName() {
 Tag tag = new Tag(null);
 }
}