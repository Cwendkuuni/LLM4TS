package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private static Whitelist getDefaultWhitelist() {
 return Whitelist.simpleTextOnly(); // Use a simple whitelist for testing
 }

 /**
 * Test constructor
 */
 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid input
 */
 @Test
 public void testCleanValidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><div>Valid content</div></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 Element body = cleanDocument.body();
 Element div = body.selectFirst("div");
 assertEquals("Valid content", div.text());
 }

 /**
 * Test clean method with invalid input containing unsafe tags
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 cleaner.clean(dirtyDocument); // Should throw an exception
 }

 /**
 * Test isValid method with valid input
 */
 @Test
 public void testIsValidValidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><div>Valid content</div></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid input containing unsafe tags
 */
 @Test
 public void testIsValidInvalidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with valid input
 */
 @Test
 public void testCopySafeNodesValidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='validDiv' data-custom='data'>Valid content</div></body></html>");
 ElementMeta meta = createElementMeta(dirtyDocument.selectFirst("div"));
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = new Document(cleaner.clean(dirtyDocument));
 Element body = cleanDocument.body();
 Element div = body.selectFirst("#validDiv");
 assertEquals("Valid content", div.text());
 assertEquals(meta.numAttribsDiscarded, div.attributes().size());
 }

 /**
 * Test copySafeNodes method with invalid input containing unsafe tags
 */
 @Test
 public void testCopySafeNodesInvalidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 ElementMeta meta = createElementMeta(dirtyDocument.body().childNodes().get(0));
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = new Document(cleaner.clean(dirtyDocument));
 Element body = cleanDocument.body();
 assertNull(body.childNodes().get(0));
 assertEquals(meta.numAttribsDiscarded, 0);
 }

 /**
 * Test createSafeElement method with valid input
 */
 @Test
 public void testCreateSafeElementValidInput() {
 Element dirtyElement = new Element(Tag.DIV, null, new Attributes());
 dirtyElement.attributes().put("id", "validDiv");
 dirtyElement.attributes().put("data-custom", "data");
 dirtyElement.text("Valid content");
 ElementMeta meta = new Cleaner().createSafeElement(dirtyElement);
 assertEquals("div", meta.el.tagName());
 assertEquals("validDiv", meta.el.attribute("id"));
 assertEquals("data", meta.el.attribute("data-custom"));
 assertEquals("Valid content", meta.el.text());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with invalid input containing unsafe tags
 */
 @Test
 public void testCreateSafeElementInvalidInput() {
 Element dirtyElement = new Element(Tag.SCRIPT, null, new Attributes());
 dirtyElement.text("alert('XSS');");
 ElementMeta meta = new Cleaner().createSafeElement(dirtyElement);
 assertNull(meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Helper method to create ElementMeta instance
 */
 private ElementMeta createElementMeta(Node node) {
 return new ElementMeta(new Element(Tag.valueOf(node.tagName()), node.baseUri(), new Attributes()), 0);
 }

 /**
 * Inner class to hold Element and number of discarded attributes
 */
 private static class ElementMeta {
 Element el;
 int numAttribsDiscarded;

 ElementMeta(Element el, int numAttribsDiscarded) {
 this.el = el;
 this.numAttribsDiscarded = numAttribsDiscarded;
 }
 }

}