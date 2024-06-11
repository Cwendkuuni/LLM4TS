package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

 Tag pTag = new Tag("P");
 Tag h1Tag = new Tag("H1");
 Tag ulTag = new Tag("UL");
 Tag liTag = new Tag("LI");
 Tag tableTag = new Tag("TABLE");
 Tag trTag = new Tag("TR");
 Tag thTag = new Tag("TH");
 Tag tdTag = new Tag("TD");

 @Test
 public void testCanContain() {
 // Test canContain method for various Tag combinations
 assertTrue(pTag.canContain(pTag));
 assertTrue(pTag.canContain(h1Tag));
 assertTrue(h1Tag.canContain(pTag));
 assertTrue(ulTag.canContain(liTag));
 assertTrue(liTag.canContain(pTag));
 assertTrue(tableTag.canContain(trTag));
 assertTrue(trTag.canContain(tdTag));
 assertTrue(tdTag.canContain(pTag));
 }

 @Test
 public void testIsBlock() {
 // Test isBlock method for various Tags
 assertTrue(pTag.isBlock());
 assertTrue(h1Tag.isBlock());
 assertFalse(ulTag.isBlock());
 assertFalse(liTag.isBlock());
 assertFalse(tableTag.isBlock());
 assertFalse(trTag.isBlock());
 assertFalse(thTag.isBlock());
 assertFalse(tdTag.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 // Test canContainBlock method for various Tags
 assertTrue(pTag.canContainBlock());
 assertTrue(h1Tag.canContainBlock());
 assertTrue(ulTag.canContainBlock());
 assertTrue(liTag.canContainBlock());
 assertFalse(tableTag.canContainBlock());
 assertFalse(trTag.canContainBlock());
 assertFalse(thTag.canContainBlock());
 assertFalse(tdTag.canContainBlock());
 }

 @Test
 public void testIsData() {
 // Test isData method for various Tags
 assertFalse(pTag.isData());
 assertFalse(h1Tag.isData());
 assertFalse(ulTag.isData());
 assertFalse(liTag.isData());
 assertFalse(tableTag.isData());
 assertFalse(trTag.isData());
 assertFalse(thTag.isData());
 assertFalse(tdTag.isData());
 }

 @Test
 public void testIsEmpty() {
 // Test isEmpty method for various Tags
 assertFalse(pTag.isEmpty());
 assertFalse(h1Tag.isEmpty());
 assertFalse(ulTag.isEmpty());
 assertFalse(liTag.isEmpty());
 assertFalse(tableTag.isEmpty());
 assertTrue(trTag.isEmpty());
 assertFalse(thTag.isEmpty());
 assertFalse(tdTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 // Test preserveWhitespace method for various Tags
 assertFalse(pTag.preserveWhitespace());
 assertFalse(h1Tag.preserveWhitespace());
 assertFalse(ulTag.preserveWhitespace());
 assertFalse(liTag.preserveWhitespace());
 assertFalse(tableTag.preserveWhitespace());
 assertFalse(trTag.preserveWhitespace());
 assertFalse(thTag.preserveWhitespace());
 assertFalse(tdTag.preserveWhitespace());
 }

 @Test
 public void testAncestors() {
 // Test ancestors property for various Tags
 assertEquals("", pTag.getImplicitParent().getName());
 assertEquals("p", pTag.getName());
 assertEquals("p", pTag.toString());
 assertEquals("body", h1Tag.getImplicitParent().getName());
 assertEquals("html", h1Tag.getAncestor(1).getName());
 assertEquals("html", h1Tag.toString());
 assertEquals("ul", ulTag.getImplicitParent().getName());
 assertEquals("li", liTag.getImplicitParent().getName());
 assertEquals("body", liTag.getAncestor(1).getName());
 assertEquals("html", liTag.toString());
 assertEquals("table", tableTag.getImplicitParent().getName());
 assertEquals("tr", trTag.getImplicitParent().getName());
 assertEquals("td", tdTag.getImplicitParent().getName());
 assertEquals("tr", tdTag.getAncestor(1).getName());
 assertEquals("html", tdTag.toString());
 }

 @Test
 public void testValidParent() {
 // Test isValidParent method for various Tag combinations
 assertTrue(pTag.isValidParent(pTag));
 assertTrue(pTag.isValidParent(h1Tag));
 assertTrue(h1Tag.isValidParent(pTag));
 assertTrue(ulTag.isValidParent(liTag));
 assertTrue(liTag.isValidParent(pTag));
 assertTrue(tableTag.isValidParent(trTag));
 assertTrue(trTag.isValidParent(tdTag));
 assertTrue(tdTag.isValidParent(pTag));
 assertTrue(pTag.isValidParent(ulTag));
 assertTrue(ulTag.isValidParent(pTag));
 }

}