package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTextOnly();
 dirtyDocument = Jsoup.parse("<html><body><div id='test'><script>alert('XSS');</script></div></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerWithNullWhitelist() {
 assertThrows(NullPointerException.class, () -> new Cleaner(null));
 }

 @Test
 public void testCleaner() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidWithValidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 assertFalse(cleaner.isValid(cleanDocument));
 }

 @Test
 public void testIsValidWithInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertTrue(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesWithEmptySource() {
 Element dest = cleanDocument.getElementsByTag("body").first();
 int numDiscarded = cleaner.copySafeNodes(null, dest);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithEmptySourceAndDest() {
 int numDiscarded = cleaner.copySafeNodes(null, null);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithValidSourceAndEmptyDest() {
 Element source = dirtyDocument.getElementsByTag("div").first();
 Element dest = cleanDocument.createElement("body");
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 assertEquals("<body></body>", dest.html());
 }

 @Test
 public void testCopySafeNodesWithValidSourceAndValidDest() {
 Element source = dirtyDocument.getElementsByTag("div").first();
 Element dest = cleanDocument.getElementsByTag("body").first();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 assertEquals("<body><div></div></body>", dest.html());
 }

 @Test
 public void testCopySafeNodesWithInvalidSource() {
 Element source = dirtyDocument.getElementsByTag("script").first();
 Element dest = cleanDocument.getElementsByTag("body").first();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(1, numDiscarded);
 assertNull(dest.getElementsByTag("script").first());
 }

 @Test
 public void testCopySafeNodesWithSourceContainingText() {
 Element source = dirtyDocument.getElementsByTag("div").first();
 Element textNode = source.getElementsByTag("p").first().getElementsByTag("text").first();
 Element dest = cleanDocument.getElementsByTag("body").first();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 assertEquals("", textNode.text());
 assertEquals("<body>" + textNode.text() + "</body>", dest.html());
 }

 @Test
 public void testCreateSafeElementWithValidAttributes() {
 Element source = dirtyDocument.getElementsByTag("div").first();
 ElementMeta meta = cleaner.createSafeElement(source);
 assertEquals("<div></div>", meta.el.html());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithInvalidAttributes() {
 Element source = dirtyDocument.getElementsByTag("div").first();
 source.attr("onclick", "alert('XSS');");
 ElementMeta meta = cleaner.createSafeElement(source);
 assertEquals("<div></div>", meta.el.html());
 assertEquals(1, meta.numAttribsDiscarded);
 assertNull(meta.el.attr("onclick"));
 }

 @Test
 public void testCreateSafeElementWithEmptySource() {
 Element source = null;
 ElementMeta meta = cleaner.createSafeElement(source);
 assertNull(meta.el);
 }

}