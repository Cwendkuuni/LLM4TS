package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.jsoup.Whitelist;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
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
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
 Document cleanDocument = new Cleaner(whitelist).clean(dirtyDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 /**
 * Test clean method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 Document dirtyDocument = null;
 new Cleaner(whitelist).clean(dirtyDocument);
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
 assertTrue(new Cleaner(whitelist).isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = null;
 new Cleaner(whitelist).isValid(dirtyDocument);
 }

 /**
 * Test copySafeNodes method with safe nodes
 */
 @Test
 public void testCopySafeNodesWithSafeNodes() {
 Element root = new Element("html");
 root.appendElement(new Element("head"));
 root.appendElement(new Element("body").appendText("Test"));
 Element destination = new Element("html");
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(root, destination);
 assertEquals(0, numDiscarded);
 assertTrue(destination.body().text().equals("Test"));
 }

 /**
 * Test copySafeNodes method with unsafe nodes
 */
 @Test
 public void testCopySafeNodesWithUnsafeNodes() {
 Element root = new Element("html");
 root.appendElement(new Element("script").attr("src", "javascript:alert('XSS')"));
 Element destination = new Element("html");
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(root, destination);
 assertEquals(1, numDiscarded);
 assertTrue(destination.body().elementTexts().isEmpty());
 }

 /**
 * Test createSafeElement method with safe tag and attributes
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndAttributes() {
 Element sourceEl = new Element("div").attr("class", "test-class").attr("id", "test-id");
 ElementMeta meta = new Cleaner(whitelist).createSafeElement(sourceEl);
 assertEquals("<div class='test-class' id='test-id'></div>", meta.el.html());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with unsafe tag and attributes
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementWithUnsafeTagAndAttributes() {
 Element sourceEl = new Element("script").attr("src", "javascript:alert('XSS')");
 new Cleaner(whitelist).createSafeElement(sourceEl);
 }

 /**
 * Test createSafeElement method with safe tag and unsafe attribute
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndUnsafeAttribute() {
 Element sourceEl = new Element("div").attr("class", "test-class").attr("onclick", "alert('XSS')");
 ElementMeta meta = new Cleaner(whitelist).createSafeElement(sourceEl);
 assertEquals("<div class='test-class'></div>", meta.el.html());
 assertEquals(1, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with unsafe tag and safe attribute
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementWithUnsafeTagAndSafeAttribute() {
 Element sourceEl = new Element("script").attr("class", "test-class");
 new Cleaner(whitelist).createSafeElement(sourceEl);
 }

}