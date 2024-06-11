package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.Map;

public class TagTest {

 @org.junit.BeforeClass
 static void setup() {
 Tag.initializeTags();
 }

 @org.junit.Test
 public void testTagName() {
 // Test the getName() method
 Tag htmlTag = Tag.valueOf("html");
 String htmlTagName = htmlTag.getName();
 assertEquals("html", htmlTagName);
 }

 @org.junit.Test
 public void testTagNameNormalization() {
 // Test the normalizeTag() method (not public, but can be tested via static methods)
 String tagName = "p";
 String normalizedTagName = Tag.normalizeTag(tagName);
 assertEquals("p", normalizedTagName);

 tagName = "P";
 normalizedTagName = Tag.normalizeTag(tagName);
 assertEquals("p", normalizedTagName);
 }

 @org.junit.Test
 public void testTagValueOf() {
 // Test the valueOf() method with known tag
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 // Test the valueOf() method with unknown tag
 Tag unknownTag = Tag.valueOf("unknownTag");
 assertFalse(unknownTag.isBlock());
assertEquals("unknownTag", unknownTag.getName());
 }

 @org.junit.Test
 public void testIsBlock() {
 // Test the isBlock() method
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isBlock());
 }

 @org.junit.Test
 public void testFormatAsBlock() {
 // Test the formatAsBlock() method
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.formatAsBlock());
 }

 @org.junit.Test
 public void testCanContainBlock() {
 // Test the canContainBlock() method (deprecated, use isBlock())
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.canContainBlock());
 }

 @org.junit.Test
 public void testIsDataTag() {
 // Test the isData() method
 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isData());

 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 }

 @org.junit.Test
 public void testIsEmptyTag() {
 // Test the isEmpty() method
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isEmpty());
 }

 @org.junit.Test
 public void testIsSelfClosing() {
 // Test the isSelfClosing() method
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isSelfClosing());

 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isSelfClosing());
 }

 @org.junit.Test
 public void testIsKnownTag() {
 // Test the isKnownTag() method (static)
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @org.junit.Test
 public void testPreserveWhitespace() {
 // Test the preserveWhitespace() method
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @org.junit.Test
 public void testIsFormListed() {
 // Test the isFormListed() method
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormListed());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isFormListed());
 }

 @org.junit.Test
 public void testIsFormSubmittable() {
 // Test the isFormSubmittable() method
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormSubmittable());

 Tag textareaTag = Tag.valueOf("textarea");
 assertTrue(textareaTag.isFormSubmittable());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isFormSubmittable());
 }

 @org.junit.Test
 public void testEquals() {
 // Test the equals() method
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 assertTrue(pTag1.equals(pTag2));

 Tag pTag3 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertFalse(pTag3.equals(divTag));

 Tag unknownTag1 = Tag.valueOf("unknownTag");
 Tag unknownTag2 = Tag.valueOf("unknownTag");
 assertTrue(unknownTag1.equals(unknownTag2));
 }

 @org.junit.Test
 public void testHashCode() {
 // Test the hashCode() method
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 assertEquals(pTag.hashCode(), pTag2.hashCode());

 Tag inputTag = Tag.valueOf("input");
 assertNotEquals(pTag.hashCode(), inputTag.hashCode());
 }
}