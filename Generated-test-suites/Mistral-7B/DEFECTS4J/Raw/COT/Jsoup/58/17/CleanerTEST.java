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
 * Test class for Cleaner class
 */
public class CleanerTest {

 private static Whitelist getDefaultWhitelist() {
 return Whitelist.relaxed();
 }

 /**
 * Test constructor
 */
 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><script>alert('XSS')</script></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 cleaner.clean(dirtyDocument); // should throw an exception
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 cleaner.isValid(dirtyDocument); // should throw an exception
 }

 /**
 * Test clean method with empty document
 */
 @Test
 public void testCleanEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html></html>", cleanDocument.html());
 }

 /**
 * Test clean method with document containing only comments
 */
 @Test
 public void testCleanDocumentContainingOnlyComments() {
 Document dirtyDocument = Jsoup.parse("<html><!-- Comment 1 --><!-- Comment 2 --></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html></html>", cleanDocument.html());
 }

 /**
 * Test clean method with document containing only text
 */
 @Test
 public void testCleanDocumentContainingOnlyText() {
 Document dirtyDocument = Jsoup.parse("<html><body>Hello World</body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 /**
 * Test clean method with document containing only script tags
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanDocumentContainingOnlyScriptTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 cleaner.clean(dirtyDocument); // should throw an exception
 }

 /**
 * Test clean method with document containing script tags and other tags
 */
 @Test
 public void testCleanDocumentContainingScriptTagsAndOtherTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Hello World</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with document containing inline styles
 */
 @Test
 public void testCleanDocumentContainingInlineStyles() {
 Document dirtyDocument = Jsoup.parse("<html><body><div style='color: red;'>Hello World</div></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><div></div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with document containing id attribute
 */
 @Test
 public void testCleanDocumentContainingIdAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='test'>Hello World</div></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><div></div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with document containing class attribute
 */
 @Test
 public void testCleanDocumentContainingClassAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><div class='test'>Hello World</div></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><div></div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with document containing div tag and script tag
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanDocumentContainingDivTagAndScriptTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><div><script>alert('XSS')</script></div></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 cleaner.clean(dirtyDocument); // should throw an exception
 }

 /**
 * Test clean method with document containing script tag inside script tag
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanDocumentContainingScriptTagInsideScriptTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script><script>alert('XSS')</script></script></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 cleaner.clean(dirtyDocument); // should throw an exception
 }

 /**
 * Test clean method with document containing xml declaration
 */
 @Test
 public void testCleanDocumentContainingXmlDeclaration() {
 Document dirtyDocument = Jsoup.parse("<html xmlns='http://www.w3.org/1999/xhtml'><body><h1>Hello World</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with document containing doctype declaration
 */
 @Test
 public void testCleanDocumentContainingDoctypeDeclaration() {
 Document dirtyDocument = Jsoup.parse("<!DOCTYPE html SYSTEM 'about:legacy-compat'><html><body><h1>Hello World</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
 }

 /**
 * Test copySafeNodes method with valid node
 */
 @Test
 public void testCopySafeNodesValidNode() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
 Element source = dirtyDocument.body().getElementsByTag("h1").first();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);

 assertEquals(0, numDiscarded);
 assertEquals(1, destination.children().size());
 assertEquals("h1", destination.tagName());
 assertEquals("Hello World", destination.text());
 }

 /**
 * Test copySafeNodes method with invalid node
 */
 @Test
 public void testCopySafeNodesInvalidNode() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Element source = dirtyDocument.body().getElementsByTag("script").first();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);

 assertEquals(1, numDiscarded);
 assertTrue(destination.isEmpty());
 }

 /**
 * Test createSafeElement method with valid element
 */
 @Test
 public void testCreateSafeElementValidElement() {
 Element sourceElement = new Element("div", "id", "test");
 Attribute idAttribute = new Attribute("id", "test");
 Element destElement = cleaner.createSafeElement(sourceElement);

 assertEquals("div", destElement.tagName());
 assertEquals(1, destElement.attributes().size());
 assertEquals(idAttribute.getValue(), destElement.attribute(idAttribute.getKey()).getValue());
 }

 /**
 * Test createSafeElement method with invalid element and attribute
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementInvalidElementAndAttribute() {
 Element sourceElement = new Element("script", "test", null);
 Attribute testAttribute = new Attribute("test", "test");
 cleaner.createSafeElement(sourceElement, testAttribute); // should throw an exception
 }

 /**
 * Test createSafeElement method with invalid element
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementInvalidElement() {
 Element sourceElement = new Element("script", null, null);
 cleaner.createSafeElement(sourceElement); // should throw an exception
 }

}