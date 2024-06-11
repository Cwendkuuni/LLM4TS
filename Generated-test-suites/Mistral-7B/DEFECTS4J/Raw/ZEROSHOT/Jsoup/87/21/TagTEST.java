package org.jsoup.parser;

import org.junit.Test;
import static org.junit. Assert.*;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testTagNormalize() {
 Tag tag = new Tag("Div");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("div");
 assertEquals(tag, Tag.valueOf("Div"));
 }

 @Test
 public void testIsBlockTrue() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlockFalse() {
 Tag tag = new Tag("input");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockTrue() {
 Tag tag = new Tag("p");
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlockFalse() {
 Tag tag = new Tag("input");
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTrue() {
 Tag tag = new Tag("div");
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 Tag tag = new Tag("input");
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testIsEmptyFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsEmptyTrue() {
 Tag tag = new Tag("img");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosingFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingTrue() {
 Tag tag = new Tag("img");
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagTrue() {
 Tag tag = new Tag("p");
 assertTrue(Tag.isKnownTag("p"));
 }

 @Test
 public void testIsKnownTagFalse() {
 Tag tag = new Tag("foo");
 assertFalse(Tag.isKnownTag("foo"));
 }

 @Test
 public void testPreserveWhitespaceFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testIsFormListedFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.isFormListed());
 }

 @Test
 public void testIsFormListedTrue() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testIsFormSubmittableFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testIsFormSubmittableTrue() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testNotEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 assertNotEquals(tag1, tag2);
 }
}