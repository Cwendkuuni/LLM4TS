package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Nodes;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void shouldCreateCleanerWithValidWhitelist() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void shouldThrowExceptionWhenNullWhitelistIsPassed() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void shouldCreateCleanDocumentFromDirtyDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element title = cleanDocument.selectFirst("h1");
 assertNotNull(title);
 assertFalse(cleanDocument.body().text().contains("alert('XSS')"));
 }

 @Test
 public void shouldReturnFalseWhenDocumentIsInvalid() {
 Document invalidDocument = Jsoup.parse("<html><body><h1 id='title' onload='alert('XSS')'></h1></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void shouldReturnTrueWhenDocumentIsValid() {
 Document validDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void shouldCopySafeNodesIntoCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 int numDiscardedNodes = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertTrue(numDiscardedNodes > 0);
 }

 @Test
 public void shouldNotCopyScriptNodesIntoCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 Nodes scriptNodes = cleanDocument.select("script");
 assertNull(scriptNodes);
 }

 @Test
 public void shouldCopySafeAttributesIntoCleanNodes() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='test' class='test' style='color:red;'><p id='inner'>Test</p></div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element div = cleanDocument.selectFirst("div");
 Element p = div.selectFirst("p");

 assertEquals("test", div.className());
 assertEquals("red", div.style().getPropertyValue("color"));
 assertEquals("inner", p.id());
 }

 @Test
 public void shouldEnforceSafeAttributesOnCleanNodes() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='test' class='test' onmouseover='alert('XSS')'><p id='inner'>Test</p></div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element div = cleanDocument.selectFirst("div");
 assertFalse(div.hasAttr("onmouseover"));
 }

 @Test
 public void shouldParseInputDocumentWithParseErrors() {
 Document dirtyDocument = null;
 try {
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test<script>alert('XSS')</script></h1></body></html>");
 } catch (ParseErrorList parseErrors) {
 // do nothing
 }

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenNullDocumentIsPassed() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenNullDocumentBodyIsPassed() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 cleaner.clean(dirtyDocument); // clean document without body
 cleaner.copySafeNodes(null, dirtyDocument.body());
 }
}

class ElementMeta {
 // Same as in Cleaner class
}

class CleaningVisitor {
 // Same as in Cleaner class
}