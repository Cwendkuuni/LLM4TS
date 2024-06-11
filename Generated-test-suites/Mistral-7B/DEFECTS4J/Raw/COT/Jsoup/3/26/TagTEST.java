package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfNull() {
 try {
 Tag tag = Tag.valueOf(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testValueOfEmpty() {
 try {
 Tag tag = Tag.valueOf("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testValueOfLowerCase() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfUpperCase() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testCanContainNull() {
 Tag parent = new Tag("div");
 try {
 parent.canContain(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCanContainSameTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertFalse(tag1.canContain(tag2));
 }

 @Test
 public void testCanContainEmptyTag() {
 Tag tag1 = new Tag("p");
 Tag img = new Tag("img");
 img.setEmpty();
 assertTrue(tag1.canContain(img));
 }

 @Test
 public void testCanContainBlockTag() {
 Tag tag1 = new Tag("p");
 Tag div = new Tag("div");
 assertTrue(tag1.canContain(div));
 }

 @Test
 public void testCanContainInlineTag() {
 Tag tag1 = new Tag("p");
 Tag a = new Tag("a");
 assertTrue(tag1.canContain(a));
 }

 @Test
 public void testCanContainOptionalClosingTag() {
 Tag tag1 = new Tag("p");
 Tag p = new Tag("p");
 p.setOptionalClosing();
 assertFalse(tag1.canContain(p));
 }

 @Test
 public void testCanContainAncestor() {
 Tag tag1 = new Tag("p");
 Tag body = new Tag("body");
 assertTrue(tag1.canContain(body));
 }

 @Test
 public void testCanContainHeadTag() {
 Tag tag1 = new Tag("p");
 Tag head = new Tag("head");
 assertFalse(tag1.canContain(head));
 }

 @Test
 public void testCanContainTagWithinHead() {
 Tag tag1 = new Tag("p");
 Tag title = new Tag("title");
 Tag head = new Tag("head");
 head.addChild(title);
 title.setAncestor(head);
 assertFalse(tag1.canContain(title));
 }

 @Test
 public void testCanContainDTTag() {
 Tag tag1 = new Tag("dl");
 Tag dt = new Tag("dt");
 assertFalse(tag1.canContain(dt));
 }

 @Test
 public void testCanContainDDTag() {
 Tag tag1 = new Tag("dl");
 Tag dd = new Tag("dd");
 assertFalse(tag1.canContain(dd));
 }

 @Test
 public void testCanContainBlockTagWithinBlockTag() {
 Tag tag1 = new Tag("div");
 Tag p = new Tag("p");
 assertTrue(tag1.canContain(p));
 }

 @Test
 public void testCanContainInlineTagWithinBlockTag() {
 Tag tag1 = new Tag("div");
 Tag a = new Tag("a");
 assertTrue(tag1.canContain(a));
 }

 @Test
 public void testCanContainBlockTagWithinInlineTag() {
 Tag tag1 = new Tag("span");
 Tag p = new Tag("p");
 assertFalse(tag1.canContain(p));
 }

 @Test
 public void testCanContainInlineTagWithinInlineTag() {
 Tag tag1 = new Tag("span");
 Tag a = new Tag("a");
 assertTrue(tag1.canContain(a));
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsInline() {
 Tag tag = new Tag("a");
 assertFalse(tag.isBlock());
 assertTrue(tag.isInline());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 tag.setEmpty();
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 tag.setPreserveWhitespace();
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParentNull() {
 Tag tag = new Tag("p");
 assertNull(tag.getImplicitParent());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("p");
 Tag body = new Tag("body");
 tag.setAncestor(body);
 assertEquals(body, tag.getImplicitParent());
 }

 @Test
 public void testGetImplicitParentMultipleAncestors() {
 Tag tag = new Tag("ul");
 Tag li = new Tag("li");
 Tag ol = new Tag("ol");
 Tag li2 = new Tag("li");
 Tag ul2 = new Tag("ul");
 tag.setAncestor(ol, li, ul2, li2);
 assertEquals(ol, tag.getImplicitParent());
 }

 @Test
 public void testGetImplicitParentChain() {
 Tag tag = new Tag("ul");
 Tag li = new Tag("li");
 Tag ol = new Tag("ol");
 Tag ul2 = new Tag("ul");
 Tag li2 = new Tag("li");
 Tag ul3 = new Tag("ul");
 tag.setAncestor(ol, li, ul2, li2, ul3);
 Tag expected = ol;
 Tag current = li;
 while (current != null) {
 assertEquals(expected, current);
 current = current.getParent();
 expected = expected.getParent();
 }
 assertEquals(null, tag.getParent());
 }

 @Test
 public void testIsValidParentNull() {
 Tag tag = new Tag("p");
 Tag nullChild = null;
 assertFalse(tag.isValidParent(nullChild));
 }

 @Test
 public void testIsValidParentSelf() {
 Tag tag = new Tag("p");
 Tag p = tag;
 assertTrue(tag.isValidParent(p));
 }

 @Test
 public void testIsValidParentSameTag() {
 Tag tag1 = new Tag("p");
 Tag p = tag1;
 assertFalse(tag1.isValidParent(p));
 }

 @Test
 public void testIsValidParentAncestor() {
 Tag tag1 = new Tag("p");
 Tag body = new Tag("body");
 tag1.setAncestor(body);
 Tag div = new Tag("div");
 div.setAncestor(body);
 assertTrue(tag1.isValidParent(div));
 }

 @Test
 public void testIsValidParentDescendant() {
 Tag tag1 = new Tag("p");
 Tag body = new Tag("body");
 tag1.setAncestor(body);
 Tag div = new Tag("div");
 Tag p = new Tag("p");
 div.setAncestor(p);
 assertTrue(tag1.isValidParent(div));
 }

 @Test
 public void testIsValidParentNonAncestor() {
 Tag tag1 = new Tag("p");
 Tag body = new Tag("body");
 tag1.setAncestor(body);
 Tag div = new Tag("div");
 assertFalse(tag1.isValidParent(div));
 }

 @Test
 public void testEqualsNull() {
 Tag tag = new Tag("p");
 assertFalse(tag.equals(null));
 }

 @Test
 public void testEqualsSameObject() {
 Tag tag = new Tag("p");
 assertTrue(tag.equals(tag));
 }

 @Test
 public void testEqualsSameTagName() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testEqualsDifferentTagName() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsDifferentCanContainBlock() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsDifferentCanContainInline() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsDifferentOptionalClosing() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 tag2.setOptionalClosing();
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsDifferentEmpty() {
 Tag tag1 = new Tag("p");
 Tag img = new Tag("img");
 img.setEmpty();
 assertFalse(tag1.equals(img));
 }

 @Test
 public void testEqualsDifferentPreserveWhitespace() {
 Tag tag1 = new Tag("pre");
 Tag tag2 = new Tag("p");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsSameValues() {
 Tag tag1 = new Tag("p").setCanContainBlock(false).setCanContainInline(true).setOptionalClosing(true).setEmpty(true).setPreserveWhitespace(false);
 Tag tag2 = new Tag("p").setCanContainBlock(false).setCanContainInline(true).setOptionalClosing(true).setEmpty(true).setPreserveWhitespace(false);
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testHashCodeSameObjects() {
 Tag tag = new Tag("p");
 int hashCode1 = tag.hashCode();
 Tag tag2 = tag;
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testHashCodeDifferentObjects() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testHashCodeSameValuesDifferentObjects() {
 Tag tag1 = new Tag("p").setCanContainBlock(false).setCanContainInline(true).setOptionalClosing(true).setEmpty(true).setPreserveWhitespace(false);
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("p").setCanContainBlock(false).setCanContainInline(true).setOptionalClosing(true).setEmpty(true).setPreserveWhitespace(false);
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertEquals(tag1.hashCode(), tag3.hashCode());
 }

}