package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title' >Test<h1><script>alert('XSS')</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
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
 * Test clean method with valid input
 */
 @Test
 public void testCleanValidInput() {
 Document cleaned = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument, cleaned);
 }

 /**
 * Test clean method with invalid input (script tag)
 */
 @Test
 public void testCleanInvalidInput() {
 Document cleaned = cleaner.clean(dirtyDocument);
 assertFalse(cleaned.body().hasText());
 assertNull(cleaned.body().getElementById("title"));
 }

 /**
 * Test isValid method with valid input
 */
 @Test
 public void testIsValidValidInput() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid input (script tag)
 */
 @Test
 public void testIsValidInvalidInput() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with empty element
 */
 @Test
 public void testCopySafeNodesEmptyElement() {
 Element emptyElement = new Element(Tag.VOID_ELEMENT, null);
 Element destination = new Element(Tag.BODY, null);
 int numDiscarded = cleaner.copySafeNodes(emptyElement, destination);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with valid element and safe attributes
 */
 @Test
 public void testCopySafeNodesValidElementSafeAttributes() {
 Element sourceElement = new Element("p", null, new Attributes().put("class", "test"));
 Element destination = new Element(Tag.BODY, null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destination);
 assertEquals(0, numDiscarded);
 Element copiedElement = destination.getFirstChild();
 assertEquals("p", copiedElement.tagName());
 assertEquals("class", copiedElement.tagName());
 assertEquals("test", copiedElement.classes());
 }

 /**
 * Test copySafeNodes method with valid element and unsafe attributes
 */
 @Test(expected = AssertionError.class)
 public void testCopySafeNodesValidElementUnsafeAttributes() {
 Element sourceElement = new Element("p", null, new Attributes().put("onclick", "alert('XSS')"));
 Element destination = new Element(Tag.BODY, null);
 cleaner.copySafeNodes(sourceElement, destination);
 }

 /**
 * Test createSafeElement method with empty element
 */
 @Test
 public void testCreateSafeElementEmptyElement() {
 Element emptyElement = new Element(Tag.VOID_ELEMENT, null);
 ElementMeta elementMeta = cleaner.createSafeElement(emptyElement);
 assertNull(elementMeta.el);
 }

 /**
 * Test createSafeElement method with valid element and safe attributes
 */
 @Test
 public void testCreateSafeElementValidElementSafeAttributes() {
 Element sourceElement = new Element("p", null, new Attributes().put("class", "test"));
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNotNull(elementMeta.el);
 assertEquals("p", elementMeta.el.tagName());
 }

 /**
 * Test createSafeElement method with valid element and unsafe attributes
 */
 @Test(expected = AssertionError.class)
 public void testCreateSafeElementValidElementUnsafeAttributes() {
 Element sourceElement = new Element("p", null, new Attributes().put("onclick", "alert('XSS')"));
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 }

}