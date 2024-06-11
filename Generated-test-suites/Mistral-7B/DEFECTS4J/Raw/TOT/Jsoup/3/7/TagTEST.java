package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.optionalClosing());
 assertFalse(tag.isEmpty());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagValueOf() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 assertTrue(tag.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag parent = new Tag("div");
 Tag child1 = new Tag("p");
 Tag child2 = new Tag("div");

 parent.setCanContainBlock(false);
 assertFalse(parent.canContain(child1));
 assertFalse(parent.canContain(child2));

 parent.setCanContainBlock(true);
 assertTrue(parent.canContain(child1));
 assertTrue(parent.canContain(child2));
 }

 @Test
 public void testCanContainInline() {
 Tag parent = new Tag("p");
 Tag child1 = new Tag("span");
 Tag child2 = new Tag("img");

 parent.setCanContainInline(false);
 assertFalse(parent.canContain(child1));
 assertFalse(parent.canContain(child2));

 parent.setCanContainInline(true);
 assertTrue(parent.canContain(child1));
 assertTrue(parent.canContain(child2));
 }

 @Test
 public void testOptionalClosing() {
 Tag parent1 = new Tag("div");
 Tag child1 = new Tag("p");
 Tag tag = new Tag("p");
 tag.setOptionalClosing(true);

 parent1.canContain(child1);
 parent1.canContain(tag);

 assertFalse(parent1.isValidParent(child1));
 assertFalse(parent1.isValidParent(tag));
 }

 @Test
 public void testEmptyTag() {
 Tag tag = new Tag("img");
 tag.setEmpty();
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsDataTag() {
 Tag tag = new Tag("script");
 tag.setCanContainInline(false);
 tag.setEmpty(false);
 assertTrue(tag.isData());
 }

 @Test
 public void testIsBlockTag() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsInlineTag() {
 Tag tag = new Tag("span");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testIsDataOnlyTag() {
 Tag tag = new Tag("script");
 tag.setCanContainInline(false);
 tag.setEmpty(false);
 assertTrue(tag.isDataOnlyTag());
 }

 @Test
 public void testIsEmptyTag() {
 Tag tag = new Tag("img");
 tag.setEmpty();
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag parent = new Tag("pre");
 Tag child = new Tag("p");
 parent.setPreserveWhitespace(true);
 assertTrue(parent.preserveWhitespace());
 assertFalse(child.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("p");
 tag.setAncestor("div", "p");
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
 Tag tag3 = new Tag("div");
 assertEquals(tag1, tag1);
 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }
}