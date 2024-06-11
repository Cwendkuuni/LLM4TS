package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup. nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private Whitelist getDefaultWhitelist() {
 return new Whitelist.SimpleTagNameWhitelist(Arrays.asList("div", "p", "a"));
 }

 @Test
 public void testCleanerCreation() {
 Whitelist whitelist = getDefaultWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testCleanDocument() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><script>alert('XSS')</script></div></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 Element body = cleanDocument.body();
 assertTrue(body.children().size() == 1);
 Element div = body.child(0);
 assertTrue(div.tagName().equals("div"));
 assertFalse(div.hasAttr("id"));
 assertFalse(div.hasAttr("script"));
 }

 @Test
 public void testIsValidDocument() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><script>alert('XSS')</script></div></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(whitelist);

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidDocument() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><img src='javascript:alert(\"XSS\")'></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(whitelist);

 assertTrue(cleaner.isValid(dirtyDocument)); // validate and clean in the same test
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText());
 }

 @Test
 public void testCopySafeNodesWithSafeTag() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><p>Hello world</p></div></body></html>", "http://test.com");
 Element sourceEl = dirtyDocument.body().selectFirst("div");
 Element dest = new Element(Tag.valueOf("div"));
 int numDiscarded = CleanerTest.copySafeNodes(sourceEl, dest);

 assertEquals(0, numDiscarded);
 Element p = dest.child(0);
 assertEquals("Hello world", p.text());
 }

 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><script id='test'>alert('XSS')</script></body></html>", "http://test.com");
 Element sourceEl = dirtyDocument.body().selectFirst("script");
 Element dest = new Element(Tag.valueOf("div"));
 int numDiscarded = CleanerTest.copySafeNodes(sourceEl, dest);

 assertEquals(1, numDiscarded);
 assertNull(dest.child(0));
 }

 @Test
 public void testCopySafeNodesWithSafeText() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'>Hello world <script>alert('XSS')</script></div></body></html>", "http://test.com");
 Element sourceEl = dirtyDocument.body().selectFirst("div");
 Element dest = new Element(Tag.valueOf("div"));
 int numDiscarded = CleanerTest.copySafeNodes(sourceEl, dest);

 assertEquals(1, numDiscarded);
 Element text = dest.child(0);
 assertEquals("Hello world", text.text());
 }

 @Test
 public void testCreateSafeElementWithSafeTag() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><div id='test'><p>Hello world</p></div></body></html>", "http://test.com");
 Element sourceEl = dirtyDocument.body().selectFirst("div");
 Element dest = new Element(Tag.valueOf("div"));
 int numDiscarded = CleanerTest.copySafeNodes(sourceEl, dest);
 ElementMeta meta = Cleaner.createSafeElement(sourceEl);

 assertEquals(0, numDiscarded);
 Element p = meta.el;
 assertEquals("Hello world", p.text());
 }

 @Test
 public void testCreateSafeElementWithUnsafeTag() {
 Whitelist whitelist = getDefaultWhitelist();
 Document dirtyDocument = Document.createFromString("<html><body><script id='test'>alert('XSS')</script></body></html>", "http://test.com");
 Element sourceEl = dirtyDocument.body().selectFirst("script");
 Element dest = new Element(Tag.valueOf("div"));
 int numDiscarded = Cleaner.copySafeNodes(sourceEl, dest);
 ElementMeta meta = Cleaner.createSafeElement(sourceEl);

 assertEquals(1, numDiscarded);
 assertNull(meta.el);
 }

 private static int copySafeNodes(Node source, Node dest) {
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 return cleaner.copySafeNodes(source, dest);
 }

 private static class ElementMeta {
 Element el;
 int numAttribsDiscarded;

 ElementMeta(Element el, int numAttribsDiscarded) {
 this.el = el;
 this.numAttribsDiscarded = numAttribsDiscarded;
 }
 }

}