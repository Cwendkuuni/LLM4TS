package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 assertTrue(tag.isBlock());
 // Additional tests for other properties if needed
 }

 @Test
 public void testTagValueOfWithKnownTag() {
 Tag tag = Tag.valueOf("p");
 Tag knownTag = Tag.valueOf("p");
 assertEquals(tag, knownTag);
 }

 @Test
 public void testTagValueOfWithUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 Tag knownTag = Tag.valueOf("p");
 assertNotEquals(unknownTag, knownTag);
 }

 @Test
 public void testTagValueOfWithNormalizedTagName() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagSpan = Tag.valueOf("span");
 assertTrue(tagP.isBlock());
 assertFalse(tagSpan.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagSpan = Tag.valueOf("span");
 assertTrue(tagP.formatAsBlock());
 assertFalse(tagSpan.formatAsBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 assertTrue(tagDiv.canContainInline());
 assertTrue(tagP.canContainInline());
 }

 @Test
 public void testTagEmpty() {
 Tag tagImg = Tag.valueOf("img");
 assertTrue(tagImg.isEmpty());
 }

 @Test
 public void testTagSelfClosing() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagP = Tag.valueOf("p");
 assertFalse(tagP.isSelfClosing());
 assertTrue(tagImg.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tagPre = Tag.valueOf("pre");
 assertTrue(tagPre.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagInput.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagInput.isFormSubmittable());
 }
}