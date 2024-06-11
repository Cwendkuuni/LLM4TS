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
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("UnknownTag");
 assertTrue(tag.isBlock());
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContain() {
 Tag parent = new Tag("div");
 Tag child1 = new Tag("p");
 Tag child2 = new Tag("div");

 assertTrue(parent.canContain(child1));
 assertTrue(parent.canContain(child2));
 }

 @Test
 public void testCannotContainSelf() {
 Tag tag = new Tag("a");
 assertFalse(tag.canContain(tag));
 }

 @Test
 public void testCannotContainBlockChildInInlineParent() {
 Tag parent = new Tag("span");
 Tag child = new Tag("div");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testEmptyTagCanContainOnlyEmptyTags() {
 Tag parent = new Tag("p");
 Tag child1 = new Tag("br");
 Tag child2 = new Tag("p");

 assertTrue(parent.canContain(child1));
 assertFalse(parent.canContain(child2));
 }

 @Test
 public void testCanContainSpecificChildren() {
 Tag head = new Tag("head");
 Tag script = new Tag("script");

 assertTrue(head.canContain(script));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTag() {
 Tag parent = new Tag("div");
 Tag child = null;

 assertFalse(parent.canContain(child));
 }

 @Test
 public void testIsBlockTag() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsInlineTag() {
 Tag tag = new Tag("span");
 assertFalse(tag.isBlock());
 assertTrue(tag.isInline());
 }

 @Test
 public void testIsDataOnlyTag() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 Tag tag = new Tag("img");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag parent = new Tag("div");
 Tag child = new Tag("p");
 child.setAncestor(parent);

 Tag implicitParent = child.getImplicitParent();
 assertEquals(parent, implicitParent);
 }

 @Test
 public void testIsValidParent() {
 Tag parent = new Tag("div");
 Tag child = new Tag("p");
 child.setAncestors(Collections.singletonList(parent));

 assertTrue(parent.isValidParent(child));
 }

 @Test
 public void testIsNotValidParent() {
 Tag parent = new Tag("div");
 Tag child = new Tag("div");

 assertFalse(parent.isValidParent(child));
 }
}