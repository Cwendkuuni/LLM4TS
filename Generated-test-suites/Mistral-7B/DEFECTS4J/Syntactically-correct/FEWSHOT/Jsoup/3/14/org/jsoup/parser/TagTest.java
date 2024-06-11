package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

 Tag pTag = new Tag("P");
 Tag divTag = new Tag("DIV");
 Tag h1Tag = new Tag("H1");
 Tag ulTag = new Tag("UL");
 Tag liTag = new Tag("LI");
 Tag tableTag = new Tag("TABLE");
 Tag trTag = new Tag("TR");
 Tag thTag = new Tag("TH");
 Tag tdTag = new Tag("TD");

 @Test
 public void testCanContain() {
 // Test that a P tag can contain a SPAN tag
 Tag spanTag = new Tag("SPAN");
 assertTrue(pTag.canContain(spanTag));

 // Test that a DIV tag can contain a P tag
 assertTrue(divTag.canContain(pTag));

 // Test that a H1 tag cannot contain a P tag
 assertFalse(h1Tag.canContain(pTag));

 // Test that an UL tag can contain a LI tag
 assertTrue(ulTag.canContain(liTag));

 // Test that an LI tag can contain a SPAN tag
 assertTrue(liTag.canContain(spanTag));

 // Test that a TABLE tag can contain a TR tag
 assertTrue(tableTag.canContain(trTag));

 // Test that a TR tag can contain a TD tag
 assertTrue(trTag.canContain(tdTag));
 }

 @Test
 public void testIsBlock() {
 // Test that a P tag is a block tag
 assertTrue(pTag.isBlock());

 // Test that a SPAN tag is an inline tag
 assertFalse(spanTag.isBlock());
 }

 @Test
 public void testIsData() {
 // Test that a SCRIPT tag is a data tag
 assertTrue(pTag.valueOf("SCRIPT").isData());

 // Test that a P tag is not a data tag
 assertFalse(pTag.isData());
 }

 @Test
 public void testIsEmpty() {
 // Test that an IMG tag is an empty tag
 assertTrue(pTag.valueOf("IMG").isEmpty());

 // Test that a P tag is not an empty tag
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testIsValidParent() {
 // Test that a P tag is a valid parent for a SPAN tag
 assertTrue(pTag.isValidParent(spanTag));

 // Test that a DIV tag is a valid parent for a P tag
 assertTrue(divTag.isValidParent(pTag));

 // Test that a H1 tag is not a valid parent for a P tag
 assertFalse(h1Tag.isValidParent(pTag));
 }

 @Test
 public void testEqualsAndHashCode() {
 // Test that two identical tags are equal
 Tag tag1 = new Tag("P");
 Tag tag2 = new Tag("P");
 assertTrue(tag1.equals(tag2));
 assertEquals(tag1.hashCode(), tag2.hashCode());

 // Test that two different tags are not equal
 Tag tag1_1 = new Tag("P");
 Tag tag2_1 = new Tag("DIV");
 assertFalse(tag1_1.equals(tag2_1));
 assertNotEquals(tag1_1.hashCode(), tag2_1.hashCode());

 // Test that a tag is equal to itself
 Tag tag = new Tag("P");
 assertTrue(tag.equals(tag));
 assertEquals(tag.hashCode(), tag.hashCode());
 }
}