package org.jsoup.safety;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataStructures;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.whitelist.Elements;
import org.jsoup.whitelist.Whitelist;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;

 @Before
 public void setup() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);

 String html = "<html><head><title>Test</title></head><body><div id='test' attr='test'><h1>Hello <script>alert('XSS')</script></h1><p>Text</p></div></body></html>";
 dirtyDocument = Jsoup.parse(html);
 }

 @Test
 public void testCleanerWithValidDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().nodeName().equals("body"));
 assertFalse(cleanDocument.body().hasMetaTag("script"));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidWithValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidWithInvalidDocument() {
 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 cleaner.clean(dirtyDocument); // clean it first to prevent Jsoup clean method from throwing an exception
 }

 @Test
 public void testCopySafeNodesWithValidDocument() {
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
 assertEquals(0, numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullDestination() {
 cleaner.copySafeNodes(dirtyDocument.body(), null);
 }

 @Test
 public void testCreateSafeElementWithValidElementAndWhitelistedTag() {
 Element sourceEl = dirtyDocument.selectFirst("div");
 Element dest = cleaner.createSafeElement(sourceEl);
 assertEquals("div", dest.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementWithInvalidElementAndUnwhitelistedTag() {
 Element sourceEl = dirtyDocument.selectFirst("script");
 Element dest = cleaner.createSafeElement(sourceEl);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testCreateSafeElementWithValidElementAndWhitelistedAttribute() {
 Element sourceEl = dirtyDocument.selectFirst("div[attr]");
 Element dest = cleaner.createSafeElement(sourceEl);
 assertNotNull(dest.attr("attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementWithValidElementAndUnwhitelistedAttribute() {
 Element sourceEl = dirtyDocument.selectFirst("div[unsafe]");
 Element dest = cleaner.createSafeElement(sourceEl);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testCreateSafeElementWithValidElementAndEnforcedAttribute() {
 Element sourceEl = dirtyDocument.selectFirst("div[class]");
 Element dest = cleaner.createSafeElement(sourceEl);
 assertNotNull(dest.attr("class"));
 }

 @Test
 public void testIsSafeTag() {
 assertTrue(Cleaner.class.getDeclaredMethod("isSafeTag", String.class).invoke(cleaner, "div"));
 assertFalse(Cleaner.class.getDeclaredMethod("isSafeTag", String.class).invoke(cleaner, "script"));
 }

 @Test
 public void testIsSafeAttribute() {
 assertTrue(Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, Element.class, Attribute.class).invoke(cleaner, "div", dirtyDocument.selectFirst("div"), new Attribute("attr", "test")));
 assertFalse(Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, Element.class, Attribute.class).invoke(cleaner, "div", dirtyDocument.selectFirst("div"), new Attribute("unsafe", "test")));
 }

 @Test
 public void testGetEnforcedAttributes() {
 Elements expected = DataStructures.hashSet("class");
 assertEquals(expected, cleaner.whitelist.getEnforcedAttributes("div"));
 }
}