package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DataNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private static final String TEST_HTML = "<html><head><title>Test</title></head>" +
         "<body><div id='test' data-test='test' onclick='alert(\"XSS\")'>Hello World</div></body></html>";
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Parser.parseBodyFragment(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Parser.parseBodyFragment(TEST_HTML);
 assertFalse(cleaner.isValid(dirtyDocument));

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><head><script>alert('XSS')</script></head><body></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document dirtyDocument = Parser.parseBodyFragment("");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanWithScriptTag() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithDataNode() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><data:foo bar='baz'></data></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithStyleNode() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><style type='text/css'>body { color: red; }</style></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithCommentNode() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><!-- comment --></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithUnknownTag() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><zoo></zoo></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCopySafeNodes() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test' data-test='test'>Hello World <script>alert('XSS')</script></div></body></html>");
 Element dest = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
 assertNotNull(dest);
 assertEquals("<html><body></body></html>", dest.html());
 assertTrue(numDiscarded > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesNullSource() {
 cleaner.copySafeNodes(null, new Element("body"));
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesNullDestination() {
 cleaner.copySafeNodes(new Element("body"), null);
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceEl = new Element("div", null, new Attributes().put("id", "test").put("data-test", "test"));
 Element dest = cleaner.createSafeElement(sourceEl);
 assertNotNull(dest);
 assertEquals("div", dest.tagName());
 assertEquals("test", dest.attr("id"));
 assertEquals("test", dest.attr("data-test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementInvalidTag() {
 Element sourceEl = new Element("zoo", null, new Attributes());
 cleaner.createSafeElement(sourceEl);
 }
}