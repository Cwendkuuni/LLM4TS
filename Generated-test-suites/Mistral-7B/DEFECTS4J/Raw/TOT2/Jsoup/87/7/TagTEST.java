package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 // Test for getName() method
 @Test
 public void testGetName() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 }

 // Test for valueOf() method with known tag
 @Test
 public void testValueOfKnownTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 // Test for valueOf() method with unknown tag
 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isBlock());
 }

 // Test for valueOf() method with case sensitive tag
 @Test
 public void testValueOfCaseSensitiveTag() {
 Tag tag = Tag.valueOf("P");
 assertTrue(tag.isBlock());
 }

 // Test for isBlock() method
 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 // Test for formatAsBlock() method
 @Test
 public void testFormatAsBlock() {
 Tag tag = new Tag("span");
 tag.formatAsBlock(false);
 assertFalse(tag.formatAsBlock());
 }

 // Test for canContainBlock() method (since it's deprecated, test it with isBlock())
 @Test
 public void testCanContainBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 // Test for isData() method
 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertFalse(tag.isEmpty());
 }

 // Test for isSelfClosing() method
 @Test
 public void testIsSelfClosing() {
 Tag tag = new Tag("img");
 assertFalse(tag.isSelfClosing());
 }

 // Test for isKnownTag() method
 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 }

 // Test for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 // Test for isFormListed() method
 @Test
 public void testIsFormListed() {
 Tag tag = new Tag("button");
 assertTrue(tag.isFormListed());
 }

 // Test for isFormSubmittable() method
 @Test
 public void testIsFormSubmittable() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormSubmittable());
 }

 // Test for equals() method
 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 }

 // Test for equals() method with different tags
 @Test
 public void testEqualsDifferentTags() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 // Test for equals() method with null object
 @Test(expected = NullPointerException.class)
 public void testEqualsNull() {
 Tag tag = new Tag("p");
 tag.equals(null);
 }

 // Test for hashCode() method
 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test for toString() method
 @Test
 public void testToString() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.toString());
 }
}