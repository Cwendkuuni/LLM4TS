package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 // Initialize whitelist and create dirty document for testing
 whitelist = new Whitelist();
 dirtyDocument = Document.createFromString("<html><body><div id='test' attr1='value1'>Hello <script>alert('XSS')</script> World</div></body></html>", "http://test.com");
 }

 /**
 * Test constructor of Cleaner class.
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid document.
 */
 @Test
 public void testCleanValidDocument() {
 Cleaner cleaner = new Cleaner(whitelist);
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><div id='test'>Hello World</div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with invalid document.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 Cleaner cleaner = new Cleaner(whitelist);
 cleanDocument = cleaner.clean(null);
 }

 /**
 * Test isValid method with valid document.
 */
 @Test
 public void testIsValidValidDocument() {
 boolean isValid = new Cleaner(whitelist).isValid(dirtyDocument);
 assertTrue(isValid);
 }

 /**
 * Test isValid method with invalid document.
 */
 @Test
 public void testIsValidInvalidDocument() {
 boolean isValid = new Cleaner(whitelist).isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test copySafeNodes method with safe tag and safe attributes.
 */
 @Test
 public void testCopySafeNodesSafeTagAndAttributes() {
 Cleaner cleaner = new Cleaner(whitelist);
 cleanDocument = Document.createFromString("<html><body><div id='test' attr1='value1'>Hello <span attr2='value2'>World</span></div></body></html>", "http://test.com");
 int numDiscarded = cleaner.copySafeNodes(cleanDocument.body(), cleanDocument.body());
 assertEquals(0, numDiscarded);
 assertEquals("<html><body><div id='test'><span attr2='value2'>World</span></div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test copySafeNodes method with not safe tag but safe children.
 */
 @Test
 public void testCopySafeNodesNotSafeTagSafeChildren() {
 Cleaner cleaner = new Cleaner(whitelist);
 cleanDocument = Document.createFromString("<html><body><div id='test'><script>alert('XSS')</script></div></body></html>", "http://test.com");
 int numDiscarded = cleaner.copySafeNodes(cleanDocument.body(), cleanDocument.body());
 assertEquals(1, numDiscarded);
 assertEquals("<html><body><div id='test'></div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test copySafeNodes method with not safe tag and not safe children.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesNotSafeTagNotSafeChildren() {
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dirtyDocument.body());
 }

 /**
 * Test CreateSafeElement method with safe tag and safe attributes.
 */
 @Test
 public void testCreateSafeElementSafeTagAndAttributes() {
 ElementMeta meta = new Cleaner().createSafeElement(dirtyDocument.body().getElementsByTag("div").first());
 assertEquals("div", meta.el.tagName());
 }

 /**
 * Test CreateSafeElement method with not safe tag and safe attributes.
 */
 @Test
 public void testCreateSafeElementNotSafeTagSafeAttributes() {
 Element element = dirtyDocument.body().getElementsByTag("script").first();
 ElementMeta meta = new Cleaner().createSafeElement(element);
 assertNull(meta.el);
 }

 /**
 * Test CreateSafeElement method with safe tag and not safe attributes.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementSafeTagNotSafeAttributes() {
 Element element = dirtyDocument.body().getElementsByTag("div").first();
 ElementMeta meta = new Cleaner().createSafeElement(element);
 meta.el.tagName(); // Accessing tagName() will throw an exception as the element is null
 }

 /**
 * Test CreateSafeElement method with not safe tag and not safe attributes.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementNotSafeTagNotSafeAttributes() {
 Element element = dirtyDocument.body().getElementsByTag("script").first();
 ElementMeta meta = new Cleaner().createSafeElement(element);
 }

}