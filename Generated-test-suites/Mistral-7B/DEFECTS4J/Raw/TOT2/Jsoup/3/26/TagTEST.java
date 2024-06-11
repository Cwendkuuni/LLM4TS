import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagNameGettingAndSetting() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 tag.setName("span");
 assertEquals("span", tag.getName());
 }

 @Test
 public void testValueOf() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 assertEquals(tag1, tag2); // pre-defined tags are equal

 Tag tag3 = Tag.valueOf("UnknownTag");
 assertNotSame(tag1, tag3); // unknown tags are not equal
 }

 @Test
 public void testCanContain() {
 Tag parentTag = new Tag("div");
 Tag childTag1 = new Tag("p");
 Tag childTag2 = new Tag("div");
 Tag childTag3 = new Tag("div");

 assertTrue(parentTag.canContain(childTag1)); // block parent can contain inline child
 assertTrue(parentTag.canContain(childTag2)); // same tag can contain itself
 assertFalse(parentTag.canContain(childTag3)); // same tag cannot contain itself directly
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainWithNullTag() {
 Tag parentTag = new Tag("div");
 Tag nullChildTag = null;
 assertFalse(parentTag.canContain(nullChildTag));
 }

 @Test
 public void testIsBlock() {
 Tag tag1 = new Tag("p");
 assertTrue(tag1.isBlock());

 Tag tag2 = new Tag("span");
 assertFalse(tag2.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag tag1 = new Tag("div");
 assertTrue(tag1.canContainBlock());

 Tag tag2 = new Tag("span");
 assertTrue(tag2.canContainBlock());
 }

 @Test
 public void testIsData() {
 Tag tag1 = new Tag("div");
 assertFalse(tag1.isData());

 Tag tag2 = new Tag("script");
 assertTrue(tag2.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag1 = new Tag("div");
 assertFalse(tag1.isEmpty());

 Tag tag2 = new Tag("img");
 assertTrue(tag2.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag1 = new Tag("pre");
 assertTrue(tag1.preserveWhitespace());

 Tag tag2 = new Tag("span");
 assertFalse(tag2.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag1 = new Tag("div").setAncestor("body");
 Tag tag2 = new Tag("p").setAncestor("div");
 assertEquals(tag1, tag2.getImplicitParent());
 }

 @Test
 public void testIsValidParent() {
 Tag parentTag = new Tag("div");
 Tag childTag1 = new Tag("div");
 Tag childTag2 = new Tag("body");

 assertTrue(parentTag.isValidParent(childTag1)); // same tag can be parent of itself
 assertFalse(parentTag.isValidParent(childTag2)); // different tag cannot be parent
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 assertTrue(tag1.equals(tag2));

 Tag tag3 = new Tag("p");
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.toString());
 }
}