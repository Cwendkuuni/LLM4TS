package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

 // Test constructor
 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 assertNotNull(cleaner);
 }

 // Test clean method with valid input
 @Test
 public void testCleanValidInput() {
 String html = "<html><body><p>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<p>Hello World!</p>", cleanDocument.body().html());
 }

 // Test isValid method with valid input
 @Test
 public void testIsValidValidInput() {
 String html = "<html><body><p>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 // Test clean method with invalid input - unsafe tags
 @Test(expected = AssertionError.class)
 public void testCleanInvalidInputUnsafeTags() {
 String html = "<html><body><script>alert('Hello World!');</script></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(dirtyDocument); // expected AssertionError
 }

 // Test clean method with invalid input - unsafe attributes
 @Test(expected = AssertionError.class)
 public void testCleanInvalidInputUnsafeAttributes() {
 String html = "<html><body><p id='test' onclick='alert(\"Hello World!\")'>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(dirtyDocument); // expected AssertionError
 }

 // Test clean method with invalid input - self closing tag
 @Test(expected = AssertionError.class)
 public void testCleanInvalidInputSelfClosingTag() {
 String html = "<html><body><img src='test'/></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(dirtyDocument); // expected AssertionError
 }

 // Test isValid method with invalid input - unsafe tags
 @Test
 public void testIsValidInvalidInputUnsafeTags() {
 String html = "<html><body><script>alert('Hello World!');</script></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 // Test isValid method with invalid input - unsafe attributes
 @Test
 public void testIsValidInvalidInputUnsafeAttributes() {
 String html = "<html><body><p id='test' onclick='alert(\"Hello World!\")'>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 // Test isValid method with invalid input - empty body
 @Test
 public void testIsValidInvalidInputEmptyBody() {
 String html = "<html><head></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0]; // empty body
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 // Test clean method with empty input
 @Test
 public void testCleanEmptyInput() {
 Document dirtyDocument = Jsoup.parseBodyFragments("")[0];
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("", cleanDocument.body().html());
 }

 // Test clean method with null input
 @Test(expected = NullPointerException.class)
 public void testCleanNullInput() {
 Document dirtyDocument = null;
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(dirtyDocument); // expected NullPointerException
 }

 // Test copySafeNodes method with valid input
 @Test
 public void testCopySafeNodesValidInput() {
 String html = "<html><body><p>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Element destElement = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destElement);
 assertEquals(0, numDiscarded);
 }

 // Test copySafeNodes method with invalid input - unsafe tags
 @Test
 public void testCopySafeNodesInvalidInputUnsafeTags() {
 String html = "<html><body><script>alert('Hello World!');</script></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Element destElement = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destElement);
 assertEquals(1, numDiscarded);
 }

 // Test copySafeNodes method with invalid input - unsafe attributes
 @Test
 public void testCopySafeNodesInvalidInputUnsafeAttributes() {
 String html = "<html><body><p id='test' onclick='alert(\"Hello World!\")'>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Element destElement = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destElement);
 assertEquals(2, numDiscarded);
 }

 // Test copySafeNodes method with invalid input - self closing tag
 @Test
 public void testCopySafeNodesInvalidInputSelfClosingTag() {
 String html = "<html><body><img src='test'/></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragments(html)[0];
 Element destElement = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destElement);
 assertEquals(1, numDiscarded);
 }

 // Test createSafeElement method with valid input
 @Test
 public void testCreateSafeElementValidInput() {
 Element sourceElement = new Element("p");
 sourceElement.attr("class", "test");
 sourceElement.attr("id", "test2");
 Attributes sourceAttributes = sourceElement.attributes();
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertEquals("p", elementMeta.el.tagName());
 assertEquals("class", elementMeta.el.tagName(), "class");
 assertEquals("test", elementMeta.el.attr("class"));
 assertEquals("id", elementMeta.el.tagName(), "id");
 assertEquals("test2", elementMeta.el.attr("id"));
 assertEquals(2, elementMeta.numAttribsDiscarded);
 }

 // Test createSafeElement method with invalid input - unsafe tag
 @Test(expected = AssertionError.class)
 public void testCreateSafeElementInvalidInputUnsafeTag() {
 Element sourceElement = new Element("script");
 sourceElement.attr("src", "test.js");
 Attributes sourceAttributes = sourceElement.attributes();
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNull(elementMeta.el);
 }

 // Test createSafeElement method with invalid input - unsafe attribute
 @Test(expected = AssertionError.class)
 public void testCreateSafeElementInvalidInputUnsafeAttribute() {
 Element sourceElement = new Element("p");
 sourceElement.attr("onclick", "alert('Hello World!')");
 Attributes sourceAttributes = sourceElement.attributes();
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNull(elementMeta.el);
 }

 // Test createSafeElement method with invalid input - empty tag
 @Test(expected = AssertionError.class)
 public void testCreateSafeElementInvalidInputEmptyTag() {
 Element sourceElement = new Element("img");
 Attributes sourceAttributes = sourceElement.attributes();
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNull(elementMeta.el);
 }

}