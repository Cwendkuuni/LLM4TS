package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleTextWhitelist();
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title' onClick='alert(\"XSS\")'>Test</h1></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1>Test</h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidDocument() {
 dirtyDocument.getElementsByTag("script").first().text("alert('XSS')");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element dirtyBody = dirtyDocument.body();
 Element cleanBody = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopyUnsafeNodes() {
 dirtyDocument.getElementsByTag("script").first().remove();
 dirtyDocument.getElementsByTag("script").first().text("");
 dirtyDocument.getElementsByTag("img").first().attr("src", "javascript:alert('XSS')");
 int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);
 assertEquals(2, numDiscarded);
 }

 @Test
 public void testCreateSafeElement() {
 Element dirtyEl = dirtyDocument.getElementsByTag("h1").first();
 ElementMeta meta = cleaner.createSafeElement(dirtyEl);
 Element cleanEl = meta.el;
 assertEquals("h1", cleanEl.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateUnsafeElement() {
 dirtyDocument.getElementsByTag("script").first().remove();
 dirtyDocument.getElementsByTag("script").first().text("");
 dirtyDocument.getElementsByTag("img").first().attr("src", "javascript:alert('XSS')");
 Element dirtyEl = dirtyDocument.getElementsByTag("h1").first();
 ElementMeta meta = cleaner.createSafeElement(dirtyEl);
 Element cleanEl = meta.el;
 assertEquals("h1", cleanEl.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 }
}