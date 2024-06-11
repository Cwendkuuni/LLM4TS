import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testgetValueOfValidTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testgetValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testCanContainBlockTag() {
 Tag parent = new Tag("div");
 Tag child = new Tag("p");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainBlockInsideBlock() {
 Tag parent = new Tag("p");
 Tag child = new Tag("p");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainInlineTag() {
 Tag parent = new Tag("div");
 Tag child = new Tag("span");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainInlineInsideInline() {
 Tag parent = new Tag("span");
 Tag child = new Tag("span");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainSelf() {
 Tag tag = new Tag("p");
 assertFalse(tag.canContain(tag));
 }

 @Test
 public void testCanContainHeadTag() {
 Tag parent = new Tag("head");
 Tag child = new Tag("base");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainBlockInsideHead() {
 Tag parent = new Tag("head");
 Tag child = new Tag("div");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainDTDDEqualTags() {
 Tag parent = new Tag("dt");
 Tag child = new Tag("dd");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainDDTEqualTags() {
 Tag parent = new Tag("dd");
 Tag child = new Tag("dt");
 assertFalse(parent.canContain(child));
 }

 @Test(expected = NullPointerException.class)
 public void testCanContainNullTag() {
 Tag parent = new Tag("div");
 Tag child = null;
 parent.canContain(child);
 }

 @Test
 public void testIsDataTag() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 Tag tag = new Tag("img");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testCanContainBlockTagWithEmptyAncestor() {
 Tag parent = new Tag("body");
 Tag child = new Tag("p");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainBlockTagWithNonEmptyAncestor() {
 Tag parent = new Tag("p");
 Tag child = new Tag("div");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainInlineTagWithEmptyAncestor() {
 Tag parent = new Tag("body");
 Tag child = new Tag("span");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainInlineTagWithNonEmptyAncestor() {
 Tag parent = new Tag("span");
 Tag child = new Tag("span");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainEmptyTagWithEmptyAncestor() {
 Tag parent = new Tag("body");
 Tag child = new Tag("img");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainEmptyTagWithNonEmptyAncestor() {
 Tag parent = new Tag("img");
 Tag child = new Tag("p");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testIsValidParent() {
 Tag parent = new Tag("div");
 Tag child = new Tag("p");
 assertTrue(parent.isValidParent(child));
 }

 @Test
 public void testInvalidParent() {
 Tag parent = new Tag("p");
 Tag child = new Tag("div");
 assertFalse(parent.isValidParent(child));
 }
}