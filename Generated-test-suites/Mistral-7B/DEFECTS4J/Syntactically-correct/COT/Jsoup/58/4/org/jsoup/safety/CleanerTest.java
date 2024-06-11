package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 // Test constructor
 @org.junit.Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 assertNotNull(cleaner);
 }

 // Test clean method with valid input
 @org.junit.Test
 public void testCleanValidInput() throws Exception {
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.body().html());
 }

 // Test clean method with invalid input
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidInput() throws Exception {
 Cleaner cleaner = new Cleaner(null);
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 cleaner.clean(dirtyDocument);
 fail("Expected IllegalArgumentException but got none.");
 }

 // Test isValid method with valid input
 @org.junit.Test
 public void testIsValidValidInput() throws Exception {
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 // Test isValid method with invalid input
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidInput() throws Exception {
 Cleaner cleaner = new Cleaner(null);
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 fail("Expected IllegalArgumentException but got none.");
 }

 // Test copySafeNodes method with empty source
 @org.junit.Test
 public void testCopySafeNodesEmptySource() throws Exception {
 Element source = Jsoup.parse("<html />").body();
 Element dest = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = Cleaner.this.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 }

 // Test copySafeNodes method with valid input
 @org.junit.Test
 public void testCopySafeNodesValidInput() throws Exception {
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 Element source = Jsoup.parse("<html><body><p class=\"test\">Hello World</p></body></html>").body();
 Element dest = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(1, numDiscarded);
 }

 // Test copySafeNodes method with invalid input
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesInvalidInput() throws Exception {
 Element source = null;
 Element dest = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = Cleaner.this.copySafeNodes(source, dest);
 fail("Expected IllegalArgumentException but got none.");
 }

 // Test isSafeTag method with safe tag
 @org.junit.Test
 public void testIsSafeTagSafeTag() {
 Whitelist whitelist = new Whitelist.SimpleTagElementParser();
 assertTrue(Cleaner.class.getMethod("isSafeTag", String.class).invoke(new Cleaner(whitelist), "p"));
 }

 // Test isSafeTag method with unsafe tag
 @org.junit.Test
 public void testIsSafeTagUnsafeTag() {
 Whitelist whitelist = new Whitelist.SimpleTagElementParser();
 assertFalse(Cleaner.class.getMethod("isSafeTag", String.class).invoke(new Cleaner(whitelist), "script"));
 }

 // Test isSafeAttribute method with safe attribute
 @org.junit.Test
 public void testIsSafeAttributeSafeAttribute() throws Exception {
 Whitelist whitelist = new Whitelist.SimpleTagAttributeParser();
 assertTrue(Cleaner.class.getMethod("isSafeAttribute", String.class, String.class, Element.class, Attribute.class)
 .invoke(new Cleaner(whitelist), "class", "", null, new Attribute("class", "test")));
 }

 // Test isSafeAttribute method with unsafe attribute
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testIsSafeAttributeUnsafeAttribute() throws Exception {
 Whitelist whitelist = new Whitelist.SimpleTagAttributeParser();
 assertFalse(Cleaner.class.getMethod("isSafeAttribute", String.class, String.class, Element.class, Attribute.class)
 .invoke(new Cleaner(whitelist), "onclick", "", null, new Attribute("onclick", "alert('XSS')")));
 }

 // Test getEnforcedAttributes method with empty tag
 @org.junit.Test
 public void testGetEnforcedAttributesEmptyTag() throws Exception {
 Whitelist whitelist = new Whitelist.SimpleTagElementParser();
 assertEquals(new Attributes(), Cleaner.class.getMethod("getEnforcedAttributes", String.class).invoke(new Cleaner(whitelist), "img"));
 }

 // Test getEnforcedAttributes method with non-empty tag
 @org.junit.Test
 public void testGetEnforcedAttributesNonEmptyTag() throws Exception {
 Whitelist whitelist = new Whitelist.SimpleTagElementParser();
 Parser parser = new Parser();
 Document dirtyDocument = parser.parse("<html><body><a href=\"javascript:alert('XSS')\">Evil Link</a></body></html>");
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(new Attributes().put("href", ""), cleanDocument.select("a").first().attributes());
 }
}