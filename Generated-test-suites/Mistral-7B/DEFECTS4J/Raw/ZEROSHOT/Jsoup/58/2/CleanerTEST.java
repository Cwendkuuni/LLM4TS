package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;

public class CleanerTest {

 private static final Whitelist WHITELIST = new BasicWhitelist();

 @Test
 public void testCleanerCreation() {
 Cleaner cleaner = new Cleaner(WHITELIST);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = createDocument("<html><body><script>alert('XSS');</script><h1>Hello</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1>Hello</h1></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testIsValidDocument() {
 Document validDocument = createDocument("<html><body><h1>Hello</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));

 Document invalidDocument = createDocument("<html><body><script>alert('XSS');</script><h1>Hello</h1></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element source = createElement("<div id='test' class='test' style='color:red;'><script>alert('XSS');</script><h1>Hello</h1></div>");
 Element destination = createElement("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);

 assertEquals("<div></div>", destination.html());
 assertEquals(4, numDiscarded);
 }

 @Test
 public void testCreateSafeElement() {
 Element source = createElement("<div id='test' class='test' style='color:red;'><h1>Hello</h1></div>");
 Element dest = cleaner.createSafeElement(source);

 assertEquals("<div></div>", dest.html());

 source = createElement("<div id='test' class='test' style='color:red;'><script>alert('XSS');</script><h1>Hello</h1></div>");
 dest = cleaner.createSafeElement(source);

 assertEquals("<div></div>", dest.html());
 }

 private Document createDocument(String html) {
 Parser parser = new Parser();
 Document document = parser.parseBodyFragment(html, "");
 return document;
 }

 private Element createElement(String html) {
 Document document = createDocument(html);
 return document.body().firstChild();
 }

}