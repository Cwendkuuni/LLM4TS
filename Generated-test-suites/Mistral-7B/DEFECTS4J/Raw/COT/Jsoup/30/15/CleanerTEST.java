package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist getDefaultWhitelist() {
 return Whitelist.simpleTextOnly();
 }

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerConstructionNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testCleanMethod() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals(1, cleanDocument.body().childNodes().size());
 assertEquals("Hello", cleanDocument.body().getElementsByTag("h1").text());
 }

 @Test
 public void testIsValidMethodTrue() {
 Document validDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidMethodFalse() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesMethodEmptySource() {
 Element root = new Element(Tag.valueOf("html"), "", new Attributes());
 Element destination = new Element(Tag.valueOf("html"), "", new Attributes());
 int numDiscarded = Cleaner.copySafeNodes(root, destination);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodEmptyDestination() {
 Element root = new Element(Tag.valueOf("html"), "", new Attributes());
 Element destination = new Element(Tag.valueOf("html"), "", new Attributes());
 int numDiscarded = Cleaner.copySafeNodes(root, null);
 assertEquals(-1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodSafeElement() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = Cleaner.copySafeNodes(dirtyDocument.body().get(0), cleanDocument.body());
 assertEquals(0, numDiscarded);
 assertEquals(1, cleanDocument.body().childNodes().size());
 assertEquals("Hello", cleanDocument.body().getElementsByTag("h1").text());
 }

 @Test
 public void testCopySafeNodesMethodDiscardSafeElement() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='test'>Hello</h1></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = Cleaner.copySafeNodes(dirtyDocument.body().get(0), cleanDocument.body());
 assertEquals(1, numDiscarded);
 assertEquals(0, cleanDocument.body().childNodes().size());
 }

 @Test
 public void testCopySafeNodesMethodDiscardUnsafeAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><script id='test' src='javascript:alert(\"XSS\")'></script></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = Cleaner.copySafeNodes(dirtyDocument.body().get(0), cleanDocument.body());
 assertEquals(1, numDiscarded);
 assertEquals(0, cleanDocument.body().childNodes().size());
 }

 @Test
 public void testCopySafeNodesMethodDiscardUnsafeTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = Cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(1, numDiscarded);
 assertEquals(0, cleanDocument.body().childNodes().size());
 }

 @Test
 public void testIsSafeTagAndIsSafeAttributeMethods() {
 Whitelist whitelist = Whitelist.simpleTextOnly();
 assertTrue(Cleaner.isSafeTag("h1", whitelist));
 assertFalse(Cleaner.isSafeTag("script", whitelist));
 assertFalse(Cleaner.isSafeAttribute("onclick", "script", "h1", new Attributes(), whitelist));
 assertTrue(Cleaner.isSafeAttribute("id", "h1", new Attributes(), whitelist));
 }

}