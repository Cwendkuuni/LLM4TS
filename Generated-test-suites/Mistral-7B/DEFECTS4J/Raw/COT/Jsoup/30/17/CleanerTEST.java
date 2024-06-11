package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.AllowAllElementMatcher;
import org.jsoup.safety.Whitelist.AllowAllTagNameFilter;
import org.jsoup.safety.Whitelist.AttributeAllowAll;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Cleaner class.
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleTextWhitelist(new AllowAllTagNameFilter(), new AllowAllElementMatcher(),
 new AttributeAllowAll());
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor with null whitelist.
 */
 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 new Cleaner(null);
 }

 /**
 * Test clean method with null Document.
 */
 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 cleaner.clean(null);
 }

 /**
 * Test clean method with valid Document.
 */
 @Test
 public void testCleanWithValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><h1>Hello</h1></body></html>", "http://test.com");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertNotNull(cleanDocument.body());
 assertFalse(cleanDocument.body().childNodes().isEmpty());
 }

 /**
 * Test isValid method with null Document.
 */
 @Test(expected = NullPointerException.class)
 public void testIsValidWithNullDocument() {
 cleaner.isValid(null);
 }

 /**
 * Test isValid method with valid Document.
 */
 @Test
 public void testIsValidWithValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><h1>Hello</h1></body></html>", "http://test.com");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 /**
 * Test isValid method with invalid Document.
 */
 @Test
 public void testIsValidWithInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "http://test.com");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 /**
 * Test isValid method with empty Document.
 */
 @Test
 public void testIsValidWithEmptyDocument() {
 Document dirtyDocument = Document.createFromString("", "http://test.com");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 /**
 * Test copySafeNodes method with null root or destination Element.
 */
 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullRootOrDestination() {
 Element root = null;
 Element destination = null;
 cleaner.copySafeNodes(root, destination);
 }

 /**
 * Test copySafeNodes method with valid root and null destination.
 */
 @Test
 public void testCopySafeNodesWithValidRootAndNullDestination() {
 Element root = new Element(Tag.valueOf("html"), "http://test.com");
 cleaner.copySafeNodes(root, null);
 }

 /**
 * Test copySafeNodes method with valid root and destination, and safe element.
 */
 @Test
 public void testCopySafeNodesWithValidRootAndDestinationAndSafeElement() {
 Element root = new Element(Tag.valueOf("html"), "http://test.com");
 Element destination = new Element(Tag.valueOf("body"), "http://test.com");
 root.appendChild(new Element(Tag.valueOf("h1"), "http://test.com"));
 cleaner.copySafeNodes(root, destination);
 assertNotNull(destination.childNodes().get(0));
 }

 /**
 * Test copySafeNodes method with valid root and destination, and unsafe element.
 */
 @Test
 public void testCopySafeNodesWithValidRootAndDestinationAndUnsafeElement() {
 Element root = new Element(Tag.valueOf("html"), "http://test.com");
 Element destination = new Element(Tag.valueOf("body"), "http://test.com");
 root.appendChild(new Element(Tag.valueOf("script"), "http://test.com"));
 cleaner.copySafeNodes(root, destination);
 assertNull(destination.childNodes().get(0));
 }

 /**
 * Test createSafeElement method with null Element.
 */
 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementWithNullElement() {
 cleaner.createSafeElement(null);
 }

 /**
 * Test createSafeElement method with valid Element and safe tag.
 */
 @Test
 public void testCreateSafeElementWithValidElementAndSafeTag() {
 Element sourceEl = new Element(Tag.valueOf("h1"), "http://test.com");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 }

 /**
 * Test createSafeElement method with valid Element and unsafe tag.
 */
 @Test
 public void testCreateSafeElementWithValidElementAndUnsafeTag() {
 Element sourceEl = new Element(Tag.valueOf("script"), "http://test.com");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el);
 }
}