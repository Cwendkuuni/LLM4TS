package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<>(); // map of known tags

 @Test
 public void testTagValues() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagUnknown = Tag.valueOf("unknown");

 // test known tags
 assertEquals("p", tagP.getName());
 assertEquals("div", tagDiv.getName());
 // test unknown tag
 assertEquals("unknown", tagUnknown.getName());
 // test equals
 assertTrue(tagP.equals(tagP));
 assertTrue(tagDiv.equals(tagDiv));
 assertFalse(tagP.equals(tagDiv));
 assertFalse(tagP.equals(null));
 // test hashcode
 int hashcodeTagP = tagP.hashCode();
 int hashcodeTagP2 = tagP.hashCode();
 assertEquals(hashcodeTagP, hashcodeTagP2);
 int hashcodeTagDiv = tagDiv.hashCode();
 assertNotEquals(hashcodeTagP, hashcodeTagDiv);
 }

 @Test
 public void testIsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagP.isBlock());
 assertFalse(tagInput.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagP.formatAsBlock());
 assertFalse(tagInput.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagP.canContainBlock());
 assertFalse(tagInput.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Tag tagP = Tag.valueOf("p");
 Tag tagInput = Tag.valueOf("input");
 assertFalse(tagP.isInline());
 assertTrue(tagInput.isInline());
 }

 @Test
 public void testIsData() {
 Tag tagP = Tag.valueOf("p");
 Tag tagScript = Tag.valueOf("script");
 assertFalse(tagP.isData());
 assertTrue(tagScript.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagP = Tag.valueOf("p");
 assertTrue(tagImg.isEmpty());
 assertFalse(tagP.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagP = Tag.valueOf("p");
 assertTrue(tagImg.isSelfClosing());
 assertFalse(tagP.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag tagP = Tag.valueOf("p");
 Tag tagUnknown = Tag.valueOf("unknown");
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tagPre = Tag.valueOf("pre");
 Tag tagDiv = Tag.valueOf("div");
 assertTrue(tagPre.preserveWhitespace());
 assertFalse(tagDiv.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag tagInput = Tag.valueOf("input");
 Tag tagButton = Tag.valueOf("button");
 assertTrue(tagInput.isFormListed());
 assertTrue(tagButton.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag tagInput = Tag.valueOf("input");
 Tag tagButton = Tag.valueOf("button");
 assertTrue(tagInput.isFormSubmittable());
 assertTrue(tagButton.isFormSubmittable());
 }
}