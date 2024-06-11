package org.jsoup.safety;

import static org.junit.Assert.*;

import org.Jsoup.nodes.Attribute;
import org.Jsoup.nodes.Document;
import org.Jsoup.nodes.Element;
import org.Jsoup.nodes.TextNode;
import org.Jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Document.fromHtml("<html><body><h1>Test<h1><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocumentTrue() {
 Document validDocument = Document.fromHtml("<html><body><h1>Test</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidDocumentFalse() {
 Document invalidDocument = Document.fromHtml("<html><body><h1>Test<script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesWithEmptyDocument() {
 Element root = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(root, new Element(Tag.BODY));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithSafeTag() {
 Document dirtyDocument = Document.fromHtml("<html><body><h1 id='test'>Test</h1></body></html>");
 Element root = dirtyDocument.body();
 int numDiscarded = cleaner.copySafeNodes(root, new Element(Tag.BODY));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Document dirtyDocument = Document.fromHtml("<html><body><h1>Test<script>alert('XSS');</script></body></html>");
 Element root = dirtyDocument.body();
 int numDiscarded = cleaner.copySafeNodes(root, new Element(Tag.BODY));
 assertEquals(1, numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullDocument() {
 cleaner.copySafeNodes(null, new Element(Tag.BODY));
 }

 @Test
 public void testCopySafeNodesWithNullElement() {
 Element root = null;
 int numDiscarded = cleaner.copySafeNodes(new Element(Tag.BODY), null);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCreateSafeElementWithSafeTagAndSafeAttribute() {
 Element sourceElement = new Element(Tag.H1, "testId", new Attribute("style", "color:red"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertEquals(new Element(Tag.H1, null, new Attributes()), meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithSafeTagAndUnsafeAttribute() {
 Element sourceElement = new Element(Tag.H1, "testId", new Attribute("style", "color:red; onmouseover='alert('XSS');'"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertEquals(new Element(Tag.H1, null, new Attributes()), meta.el);
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithUnsafeTag() {
 Element sourceElement = new Element(Tag.SCRIPT, null, null);
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertNull(meta.el);
 }

 @Test
 public void testCreateSafeElementWithNullElement() {
 Element sourceElement = null;
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertNull(meta.el);
 }

}