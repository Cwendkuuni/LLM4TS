package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOf_knownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 String expectedTagName = "p";

 assertEquals(expectedTagName, tag.getName());
 assertTrue(tag.isBlock());
 // Add more assertions based on other tag properties
 }

 @Test
 public void testValueOf_unknownTag() {
 String tagName = "test";
 Tag tag = Tag.valueOf(tagName);
 String expectedTagName = "tag"; // or any default tag name

 assertEquals(expectedTagName, tag.getName());
 assertFalse(tag.isBlock());
 // Add more assertions based on other tag properties
 }

 @Test
 public void testValueOf_caseInsensitive_knownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName.toUpperCase());
 String expectedTagName = "p";

 assertEquals(expectedTagName, tag.getName());
 assertTrue(tag.isBlock());
 // Add more assertions based on other tag properties
 }

 @Test (expected = IllegalArgumentException.class)
 public void testValueOf_emptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);

 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testIsKnownTag() {
 String tagName1 = "p";
 String tagName2 = "test";

 assertTrue(Tag.isKnownTag(tagName1));
 assertFalse(Tag.isKnownTag(tagName2));
 }

 // Add more tests for other methods and properties
}