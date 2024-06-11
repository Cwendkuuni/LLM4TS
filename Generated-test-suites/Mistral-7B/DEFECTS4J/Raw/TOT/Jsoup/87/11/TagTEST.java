package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Tag class.
 */
public class TagTest {

 @Test
 public void testValueOf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertEquals("p", tag.getName());
 tagName = "DIV";
 Tag tagDiv = Tag.valueOf(tagName);
 assertEquals("DIV", tagDiv.getName());
 tagName = "Unknown";
 Tag tagUnknown = Tag.valueOf(tagName);
 assertEquals("Unknown", tagUnknown.getName());
 tagUnknown = Tag.valueOf(tagName, ParseSettings.preserveCase);
 assertNull(tagUnknown);
 }

 @Test
 public void testIsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagImg = Tag.valueOf("img");
 assertTrue(tagP.isBlock());
 assertFalse(tagImg.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagBr = Tag.valueOf("br");
 assertTrue(tagP.formatAsBlock());
 assertFalse(tagBr.formatAsBlock());
 }

 @Test
 public void testIsData() {
 Tag tagScript = Tag.valueOf("script");
 Tag tagInput = Tag.valueOf("input");
 assertFalse(tagScript.isData());
 assertTrue(tagInput.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagInput = Tag.valueOf("input");
 assertFalse(tagImg.isEmpty());
 assertFalse(tagInput.isEmpty());
 Tag tagBr = Tag.valueOf("br");
 assertTrue(tagBr.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagInput = Tag.valueOf("input");
 assertFalse(tagImg.isSelfClosing());
 assertFalse(tagInput.isSelfClosing());
 Tag tagImgSelfClosing = tagImg.setSelfClosing();
 assertTrue(tagImgSelfClosing.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("Unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tagPre = Tag.valueOf("pre");
 assertTrue(tagPre.preserveWhitespace());
 Tag tagDiv = Tag.valueOf("div");
 assertFalse(tagDiv.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag tagButton = Tag.valueOf("button");
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagButton.isFormListed());
 assertTrue(tagInput.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag tagButton = Tag.valueOf("button");
 Tag tagInput = Tag.valueOf("input");
 assertTrue(tagButton.isFormSubmittable());
 assertTrue(tagInput.isFormSubmittable());
 }
}