package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().childNodes().isEmpty());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p><span style='color:red'>Hello World!</span></p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 assertEquals(2, root.childNodeSize());
 Element p = root.child(0);
 assertEquals(1, p.childNodeSize());
 Element span = p.child(0);
 assertEquals("span", span.tagName());
 assertEquals("style", span.tagNames().get(1).toString());
 }

 @Test
 public void testCopySafeNodesInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 assertTrue(root.childNodes().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWhitelist() {
 cleaner = new Cleaner(null);
 }
}