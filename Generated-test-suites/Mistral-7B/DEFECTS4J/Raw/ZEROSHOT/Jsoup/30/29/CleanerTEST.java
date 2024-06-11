package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleTextWhitelist();
 dirtyDocument = Jsoup.parse("<html><body><div id='test' style='color: red;'>Test <script>alert('XSS');</script></div></body></html>");
 cleanDocument = Document.newDocument();
 }

 @Test
 public void testCreateCleanerWithNullWhitelist() {
 try {
 new Cleaner(null);
 fail("Should not be able to create Cleaner with null Whitelist");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<html><body></body></html>");
 }

 @Test
 public void testIsValidDocument() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document emptyDocument = Document.newDocument();
 boolean isValid = cleaner.isValid(emptyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><p>Test</p></body></html>");
 boolean isValid = cleaner.isValid(validDocument);
 assertTrue(isValid);
 }

 @Test
 public void testCopySafeNodesWithSafeTag() {
 Element root = Jsoup.parse("<html><body><div id='test'><p>Test</p></div></body></html>").body();
 Element destination = cleanDocument.body().addElement("div");
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(numDiscarded, 0);
 assertEquals(destination.html(), "<p>Test</p>");
 }

 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Element root = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>").body();
 Element destination = cleanDocument.body().addElement("div");
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(numDiscarded, 1);
 assertNull(destination.child(0));
 }

 @Test
 public void testCreateSafeElementWithSafeTag() {
 Element sourceEl = Jsoup.parse("<html><body><p id='test'>Test</p></body></html>").body().getElementsByTag("p").first();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertEquals(meta.el.tagName(), "p");
 assertEquals(meta.numAttribsDiscarded, 0);
 }

 @Test
 public void testCreateSafeElementWithUnsafeTag() {
 Element sourceEl = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>").body().getElementsByTag("script").first();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertEquals(meta.el.tagName(), ""); // An empty tag is returned when the tag is not allowed
 assertEquals(meta.numAttribsDiscarded, 1);
 }
}