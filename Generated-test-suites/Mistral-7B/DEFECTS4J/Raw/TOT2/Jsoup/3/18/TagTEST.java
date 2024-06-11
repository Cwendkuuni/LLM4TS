import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testgetValueOf() {
 Tag tag = Tag.valueOf("p");
 String name = tag.getName();
 assertEquals("p", name);
 }

 @Test
 public void testCanContain() {
 Tag parentTag = new Tag("div");
 Tag childTag1 = new Tag("p");
 Tag childTag2 = new Tag("div");

 parentTag.canContain(childTag1); // Test for valid child
 parentTag.canContain(childTag2); // Test for invalid child (same type as parent)

 // Test for valid child types
 parentTag.canContain(new Tag("span"));
 parentTag.canContain(new Tag("a"));
 parentTag.canContain(new Tag("img"));
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());

 tag = new Tag("span");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("p");
 assertFalse(tag.isData());

 tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertTrue(tag.isEmpty());

 tag = new Tag("p");
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());

 tag = new Tag("p");
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("p");
 Tag parent = tag.getImplicitParent();
 assertEquals("div", parent.getName());
 }

 @Test
 public void testIsValidParent() {
 Tag childTag = new Tag("p");
 Tag parentTag1 = new Tag("div");
 Tag parentTag2 = new Tag("p");

 assertTrue(childTag.isValidParent(parentTag1)); // Valid parent
 assertFalse(childTag.isValidParent(parentTag2)); // Invalid parent (same type as child)
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNull() {
 Tag tag = new Tag("p");
 assertNotEquals(null, tag);
 }

 @Test
 public void testEqualsSameObject() {
 Tag tag = new Tag("p");
 assertEquals(tag, tag);
 }

 @Test
 public void testEqualsDifferentObjects() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertNotEquals(tag1, tag2);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }
}