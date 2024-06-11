package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

public class TagTest {

 private static Tag tagA;
 private static Tag tagB;
 private static Tag tagC;
 private static Tag tagD;
 private static Tag tagE;
 private static Tag tagF;
 private static Tag tagG;
 private static Tag tagH;

 @BeforeClass
 public static void setup() {
 tagA = Tag.valueOf("A");
 tagB = Tag.valueOf("B");
 tagC = Tag.valueOf("C");
 tagD = Tag.valueOf("D");
 tagE = Tag.valueOf("E");
 tagF = Tag.valueOf("F");
 tagG = Tag.valueOf("G");
 tagH = Tag.valueOf("H");
 }

 @Test
 public void testTagName() {
 Tag tag = new Tag("TestTag");
 assertEquals("TestTag", tag.getName());
 }

 @Test
 public void testValueOfPredefinedTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("UnknownTag");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testCanContainSameTag() {
 Tag parent = Tag.valueOf("DIV");
 Tag child = Tag.valueOf("DIV");
 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCanContainBlockTag() {
 Tag parent = Tag.valueOf("DIV");
 Tag child = Tag.valueOf("P");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCanContainInlineTag() {
 Tag parent = Tag.valueOf("SPAN");
 Tag child = Tag.valueOf("A");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCanContainDataOnlyTag() {
 Tag parent = Tag.valueOf("SCRIPT");
 Tag child = Tag.valueOf("SCRIPT");
 assertTrue(parent.canContain(child));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainSelf() {
 Tag parent = Tag.valueOf("A");
 assertTrue(parent.canContain(parent)); // should throw exception
 }

 @Test
 public void testCanContainEmptyTag() {
 Tag parent = Tag.valueOf("DIV");
 Tag child = Tag.valueOf("IMG");
 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCanContainBlockAndInlineTags() {
 Tag parent = Tag.valueOf("DIV");
 Tag blockChild = Tag.valueOf("P");
 Tag inlineChild = Tag.valueOf("A");
 assertTrue(parent.canContain(blockChild));
 assertTrue(parent.canContain(inlineChild));
 }

 @Test
 public void testEmptyTagCanContainNothing() {
 Tag emptyTag = Tag.valueOf("IMG");
 assertFalse(emptyTag.canContainInline(tagA));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("PRE");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testIsDataTag() {
 Tag tag = Tag.valueOf("SCRIPT");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsBlockTag() {
 Tag tag = Tag.valueOf("P");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsEmptyTag() {
 Tag tag = Tag.valueOf("IMG");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsInlineTag() {
 Tag tag = Tag.valueOf("SPAN");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testIsDataOrEmptyTag() {
 Tag tag = Tag.valueOf("SCRIPT");
 assertTrue(tag.isDataOrEmpty());
 }

 @Test
 public void testGetAncestors() {
 Tag tag = Tag.valueOf("DIV");
 assertEmpty(tag.getAncestors());

 Tag parent = Tag.valueOf("BODY");
 tag.setAncestor(parent);
 assertArrayEquals(new String[]{"BODY"}, tag.getAncestors().toArray(new String[0]));
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = Tag.valueOf("DIV");
 assertNull(tag.getImplicitParent());

 Tag parent = Tag.valueOf("BODY");
 tag.setAncestor(parent);
 assertEquals("BODY", tag.getImplicitParent().getName());
 }

 @Test
 public void testIsValidParent() {
 Tag child = Tag.valueOf("SPAN");
 Tag parent = Tag.valueOf("DIV");
 assertTrue(parent.isValidParent(child));

 child = Tag.valueOf("DIV");
 assertFalse(parent.isValidParent(child));

 child = Tag.valueOf("P");
 parent = Tag.valueOf("DIV");
 assertFalse(parent.isValidParent(child));
 }

 @Test
 public void testHashCodeAndEquals() {
 Tag tag1 = new Tag("Tag1");
 Tag tag2 = new Tag("Tag1");
 assertEquals(tag1, tag2);
 assertEquals(tag1.hashCode(), tag2.hashCode());

 Tag tag3 = new Tag("Tag2");
 assertNotEquals(tag1, tag3);
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("TestTag");
 assertEquals("TestTag", tag.toString());
 }

 @Test
 public void testToString_whenTagIsEmpty_returnsTagName() {
 Tag tag = new Tag("IMG");
 assertEquals("IMG", tag.toString());
 }

 @Test
 public void testToString_whenTagHasAncestors_returnsTagNameAndAncestors() {
 Tag tag = new Tag("DIV").setAncestor("BODY");
 assertEquals("DIV[ancestor=BODY]", tag.toString());
 }

 private void assertArrayEquals(Object[] expected, Object[] actual) {
 if (expected.length != actual.length) {
 fail("Array lengths differ: expected=" + expected.length + ", actual=" + actual.length);
 }
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }

 private void assertEmpty(List<?> list) {
 assertTrue(list.isEmpty());
 }

}