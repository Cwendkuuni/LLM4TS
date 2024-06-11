package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test cases for the Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = new Whitelist(); // assuming Whitelist is instantiated correctly
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor with null whitelist
 */
 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 new Cleaner(null);
 }

 /**
 * Test clean method with valid document and whitelist
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><h1 id='title'>Test</h1><p>Content</p></body></html>", "http://test.com");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body><h1></h1><p></p></body></html>", cleanDocument.html());
 }

 /**
 * Test clean method with invalid document and whitelist
 */
 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "http://test.com");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 /**
 * Test isValid method with valid document and whitelist
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><h1 id='title'>Test</h1><p>Content</p></body></html>", "http://test.com");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document and whitelist
 */
 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "http://test.com");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with safe tag and safe attributes
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndSafeAttributes() {
 Element root = new Element(Tag.HTML, null, new Attributes());
 root.appendText("Test");
 Element destination = new Element(Tag.BODY, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(0, numDiscarded);
 assertEquals(1, destination.childrenSize());
 assertEquals("Test", destination.child(0).text());
 }

 /**
 * Test copySafeNodes method with unsafe tag
 */
 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Element root = new Element(Tag.SCRIPT, null, new Attributes());
 root.appendText("alert('XSS');");
 Element destination = new Element(Tag.BODY, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(1, numDiscarded);
 assertNull(destination.child(0));
 }

 /**
 * Test copySafeNodes method with safe tag and unsafe attribute
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndUnsafeAttribute() {
 Element root = new Element(Tag.INPUT, null, new Attributes().put("src", "javascript:alert('XSS')"));
 Element destination = new Element(Tag.BODY, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(1, numDiscarded);
 assertNull(destination.child(0));
 }

 /**
 * Test copySafeNodes method with safe tag and enforced attribute
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndEnforcedAttribute() {
 Element root = new Element(Tag.INPUT, null, new Attributes().put("type", "text"));
 Element destination = new Element(Tag.BODY, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(0, numDiscarded);
 assertEquals(1, destination.childrenSize());
 assertSame(root.tagName(), destination.child(0).tagName());
 }

 /**
 * Test createSafeElement method with safe tag and safe attributes
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndSafeAttributes() {
 Element sourceEl = new Element(Tag.DIV, null, new Attributes().put("id", "test"));
 Element meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with unsafe tag
 */
 @Test
 public void testCreateSafeElementWithUnsafeTag() {
 Element sourceEl = new Element(Tag.SCRIPT, null, new Attributes());
 Element meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el);
 }

 /**
 * Test createSafeElement method with safe tag and unsafe attribute
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndUnsafeAttribute() {
 Element sourceEl = new Element(Tag.INPUT, null, new Attributes().put("src", "javascript:alert('XSS')"));
 Element meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el);
 }

 /**
 * Test createSafeElement method with safe tag and enforced attribute
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndEnforcedAttribute() {
 Element sourceEl = new Element(Tag.INPUT, null, new Attributes().put("type", "text"));
 Element meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }

}