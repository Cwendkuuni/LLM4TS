package org.jsoup.safety;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 whitelist = new Whitelist(); // Assuming Whitelist is a class that can be instantiated
 cleaner = new Cleaner(whitelist);

 String html = "<html><body><div id='test' attr1='test1'>Test <script>alert('Hello')</script></div></body></html>";
 dirtyDocument = Parser.parseBodyFragment(html);
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertFalse(cleanedDocument.body().childNodes().hasText()); // No text nodes should be present in the cleaned document

 Element div = cleanedDocument.selectFirst("div#test");
 assertNotNull(div);
 assertFalse(div.hasAttr("attr1"));
 assertFalse(div.hasAttr("script"));
 }

 @Test
 public void testIsValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 assertFalse(cleaner.isValid(Parser.parseBodyFragment("<html><body><script>alert('Hello')</script></body></html>")));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanDocumentNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerNullWhitelist() {
 try {
 new Cleaner(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCopySafeNodes() {
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertNotNull(numDiscarded);
 assertFalse(numDiscarded > 0);
 }

 @Test
 public void testCopySafeNodesWithScriptTag() {
 dirtyDocument.body().getElementsByTag("script").remove();
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(1, numDiscarded); // The script tag should have been discarded
 }

 @Test
 public void testCopySafeNodesWithScriptTagInsideElement() {
 Element scriptElement = new Element("div", null);
 scriptElement.appendText("Test <script>alert('Hello')</script>");
 dirtyDocument.body().appendChild(scriptElement);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(1, numDiscarded); // The script tag should have been discarded
 }

 @Test
 public void testCopySafeNodesWithSafeTagAndAttributes() {
 Element element = new Element("div", null);
 element.attr("id", "test");
 element.attr("attr1", "test1");
 dirtyDocument.body().appendChild(element);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(2, numDiscarded); // The attributes should have been discarded

 Element copiedElement = cleanDocument.body().selectFirst("div#test");
 assertEquals("div", copiedElement.tagName());
 assertFalse(copiedElement.hasAttr("attr1"));
 }

 @Test
 public void testCopySafeNodesWithSafeTagAndEnforcedAttributes() {
 Element element = new Element("input", null);
 element.attr("type", "text");
 dirtyDocument.body().appendChild(element);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(0, numDiscarded); // The enforced attributes should not be discarded

 Element copiedElement = cleanDocument.selectFirst("input[type=text]");
 assertEquals("input", copiedElement.tagName());
 assertTrue(copiedElement.hasAttr("type"));
 }

 @Test
 public void testCopySafeNodesWithComments() {
 Element commentElement = new Element("!-- This is a comment -->", null);
 dirtyDocument.body().appendChild(commentElement);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(1, numDiscarded); // The comment should have been discarded
 }

 @Test
 public void testCopySafeNodesWithEmptyDocument() {
 Document emptyDocument = Document.createRootElements(Tag.HTML);
 int numDiscarded = cleaner.copySafeNodes(emptyDocument.body(), cleanDocument.body());
 assertEquals(0, numDiscarded);
 }
}