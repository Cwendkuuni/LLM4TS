package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test'><script>alert('XSS')</script><h1>Test</h1></div></body></html>", null);
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 assertTrue(cleaner instanceof Cleaner);
 }

 /**
 * Test clean method.
 */
 @Test
 public void testClean() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 // Check if the cleaned document is not null.
 assertNotNull(cleanedDocument);

 // Check if the cleaned document is empty.
 assertTrue(cleanedDocument.body().child(0) instanceof TextNode);
 assertTrue(cleanedDocument.body().child(0).text().isEmpty());
 }

 /**
 * Test isValid method.
 */
 @Test
 public void testIsValid() {
 // Test valid document.
 assertTrue(cleaner.isValid(dirtyDocument));

 // Test invalid document.
 dirtyDocument.body().appendElement("script").attr("src", "javascript:alert('XSS')");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with valid nodes.
 */
 @Test
 public void testCopySafeNodesWithValidNodes() {
 cleanDocument.body().appendChild(new TextNode("Test"));
 Element sourceElement = cleanDocument.body().appendElement("div");
 sourceElement.appendText("Test");
 sourceElement.attr("id", "test");
 sourceElement.attr("class", "testClass");
 sourceElement.attr("style", "color: red; background-color: blue;");
 cleaner.copySafeNodes(sourceElement, cleanDocument.body());

 Element destinationElement = cleanDocument.body();

 // Check if the destination element is not null.
 assertNotNull(destinationElement);

 // Check if the destination element has the text node.
 TextNode textNode = (TextNode) destinationElement.child(0);
 assertTrue(textNode.text().equals("Test"));

 // Check if the destination element has the div element.
 Element divElement = (Element) destinationElement.child(1);
 assertTrue(divElement.tagName().equals("div"));
 assertTrue(divElement.attr("id").equals("test"));
 assertTrue(divElement.attr("class").equals("testClass"));
 assertTrue(divElement.attr("style").equals("color: red; background-color: blue;"));
 }

 /**
 * Test copySafeNodes method with invalid nodes.
 */
 @Test(expected = ParseError.class)
 public void testCopySafeNodesWithInvalidNodes() {
 Element sourceElement = dirtyDocument.body().appendElement("script");
 sourceElement.attr("src", "javascript:alert('XSS')");
 cleaner.copySafeNodes(sourceElement, cleanDocument.body());
 }

 /**
 * Test copySafeNodes method with comments and xml proc instructions.
 */
 @Test
 public void testCopySafeNodesWithCommentsAndXmlProcInstructions() {
 // Test comments.
 dirtyDocument.body().appendText("<!-- Test Comment -->");
 cleanDocument.body().appendChild(new TextNode("<!-- Test Comment -->"));
 cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 // Test xml proc instructions.
 dirtyDocument.body().appendText("<?xml version='1.0' encoding='UTF-8'?>");
 cleanDocument.body().appendChild(new TextNode("<?xml version='1.0' encoding='UTF-8'?>"));
 cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 }

 /**
 * Test copySafeNodes method with empty elements.
 */
 @Test
 public void testCopySafeNodesWithEmptyElements() {
 dirtyDocument.body().appendChildElement("p");
 cleanDocument.body().appendChild(new Element("p"));
 cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 }

 /**
 * Test createSafeElement method with valid attributes.
 */
 @Test
 public void testCreateSafeElementWithValidAttributes() {
 Element sourceElement = new Element("div");
 sourceElement.attr("id", "test");
 sourceElement.attr("class", "testClass");
 sourceElement.attr("style", "color: red; background-color: blue;");
 ElementMeta result = cleaner.createSafeElement(sourceElement);

 // Check if the result is not null.
 assertNotNull(result);

 // Check if the result element is not null.
 assertNotNull(result.el);

 // Check if the result element has the correct attributes.
 assertTrue(result.el.tagName().equals("div"));
 assertTrue(result.el.attr("id").equals("test"));
 assertTrue(result.el.attr("class").equals("testClass"));
 assertTrue(result.el.attr("style").equals("color: red; background-color: blue;"));

 // Check if the number of discarded attributes is zero.
 assertTrue(result.numAttribsDiscarded == 0);
 }

 /**
 * Test createSafeElement method with invalid attributes.
 */
 @Test
 public void testCreateSafeElementWithInvalidAttributes() {
 Element sourceElement = new Element("div");
 sourceElement.attr("id", "test");
 sourceElement.attr("onclick", "alert('XSS')");
 ElementMeta result = cleaner.createSafeElement(sourceElement);

 // Check if the result is not null.
 assertNotNull(result);

 // Check if the result element is not null.
 assertNotNull(result.el);

 // Check if the result element has no onClick attribute.
 assertNull(result.el.attr("onclick"));

 // Check if the number of discarded attributes is one.
 assertTrue(result.numAttribsDiscarded == 1);
 }

}