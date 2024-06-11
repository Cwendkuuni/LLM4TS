package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class CleanerTest {

 private Document dirtyDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.simple(); // Using a simple whitelist for testing
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title' onclick='alert(\"XSS\")'>Title<a href='javascript:alert(\"XSS\")'>Link</a></body></html>");
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 /**
 * Test clean method with invalid document
 */
 @Test
 public void testCleanInvalidDocument() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test copySafeNodes method with empty source element
 */
 @Test
 public void testCopySafeNodesEmptySourceElement() {
 Element destination = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(null, destination);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with safe tag and safe attributes
 */
 @Test
 public void testCopySafeNodesSafeTagAndSafeAttributes() {
 Element destination = new Element(Tag.DIV);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().getFirstElementSibling(), destination);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with unsafe tag and discarded attributes
 */
 @Test
 public void testCopySafeNodesUnsafeTagAndDiscardedAttributes() {
 Element destination = new Element(Tag.DIV);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().getElementsByTag("script").get(0), destination);
 assertEquals(2, numDiscarded);
 }

 /**
 * Test copySafeNodes method with safe tag and discarded text
 */
 @Test
 public void testCopySafeNodesSafeTagAndDiscardedText() {
 Element destination = new Element(Tag.DIV);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().getElementsByTag("script").get(0), destination);
 assertEquals(1, numDiscarded);
 }

 /**
 * Test createSafeElement method with safe tag and safe attributes
 */
 @Test
 public void testCreateSafeElementSafeTagAndSafeAttributes() {
 Element sourceEl = dirtyDocument.body().getFirstElementSibling();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertEquals("div", meta.el.tagName());
 }

 /**
 * Test createSafeElement method with unsafe tag and discarded attributes
 */
 @Test
 public void testCreateSafeElementUnsafeTagAndDiscardedAttributes() {
 Element sourceEl = dirtyDocument.body().getElementsByTag("script").get(0);
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el);
 }

}