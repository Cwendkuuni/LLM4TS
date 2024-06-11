import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfUnknown() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isBlock());
 assertFalse(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 }

 @Test
 public void testCanContain_SameTag() {
 Tag parent = new Tag("div");
 Tag child = new Tag("div");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContain_BlockAndBlock() {
 Tag parent = new Tag("div");
 Tag child = new Tag("p");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCanContain_InlineAndBlock() {
 Tag parent = new Tag("p");
 Tag child = new Tag("span");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCanContain_BlockAndInline() {
 Tag parent = new Tag("p");
 Tag child = new Tag("span");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCanContain_InvalidParentChild() {
 Tag parent = new Tag("p");
 Tag child = new Tag("p");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContain_EmptyTag() {
 Tag tag = new Tag("img");
 tag.setEmpty();
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 }

 @Test
 public void testCanContain_HeadTag() {
 Tag tag = new Tag("head");
 List<String> validChildren = Arrays.asList("base", "script", "noscript", "link", "meta", "title", "style", "object");
 for (String child : validChildren) {
 Tag childTag = Tag.valueOf(child);
 assertTrue(tag.canContain(childTag));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContain_InvalidChild() {
 Tag parent = new Tag("p");
 Tag child = new Tag("div");
 parent.canContain(child);
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsInline() {
 Tag tag = new Tag("span");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 tag.setCanContainInline(false);
 tag.setEmpty(false);
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 tag.setEmpty(true);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 tag.setPreserveWhitespace(true);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("p").setAncestor("div");
 Tag implicitParent = tag.getImplicitParent();
 assertEquals("div", implicitParent.getName());
 }

 @Test
 public void testIsValidParent() {
 Tag parent = new Tag("div");
 Tag child = new Tag("p");
 assertTrue(parent.isValidParent(child));
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }
}