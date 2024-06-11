package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTagNames("div", "p", "a");
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><p>Test</p><a href='http://example.com'>Link</a></div></body></html>", null);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 assertEquals("div", root.tagName());
 Element div = root.getElementById("test");
 assertEquals("p", div.childNodes().get(0).tagName());
 assertEquals("a", div.childNodes().get(1).tagName());
 }

 /**
 * Test clean method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", null);
 cleaner.clean(dirtyDocument);
 fail("Expected IllegalArgumentException");
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><p>Test</p><a href='http://example.com'>Link</a></div></body></html>", null);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", null);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with safe tag
 */
 @Test
 public void testCopySafeNodesSafeTag() {
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><p>Test</p><a href='http://example.com'>Link</a></div></body></html>", null);
 Element root = cleaner.clean(dirtyDocument).body();
 int numDiscarded = cleaner.copySafeNodes(root, Document.createShell(root.baseUri()).body());
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with unsafe tag
 */
 @Test
 public void testCopySafeNodesUnsafeTag() {
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><script>alert('XSS');</script></div></body></html>", null);
 Element root = cleaner.clean(dirtyDocument).body();
 int numDiscarded = cleaner.copySafeNodes(root, Document.createShell(root.baseUri()).body());
 assertNotEquals(0, numDiscarded);
 }

 /**
 * Test createSafeElement method with safe tag and safe attribute
 */
 @Test
 public void testCreateSafeElementSafeTagSafeAttribute() {
 Element dirtyElement = new Element(Tag.DIV, null, new Attributes().put("class", "safe"));
 Element meta = cleaner.createSafeElement(dirtyElement);
 assertEquals("div", meta.el.tagName());
 assertEquals("class", meta.el.tagName());
 assertEquals("safe", meta.el.classes().get(0));
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with safe tag and unsafe attribute
 */
 @Test
 public void testCreateSafeElementSafeTagUnsafeAttribute() {
 Element dirtyElement = new Element(Tag.DIV, null, new Attributes().put("class", "safe").put("onclick", "alert('XSS');"));
 Element meta = cleaner.createSafeElement(dirtyElement);
 assertEquals("div", meta.el.tagName());
 assertEquals(0, meta.el.attributeCount());
 assertEquals(1, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with unsafe tag
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementUnsafeTag() {
 Element dirtyElement = new Element(Tag.SCRIPT, null, null);
 Element meta = cleaner.createSafeElement(dirtyElement);
 fail("Expected IllegalArgumentException");
 }
}