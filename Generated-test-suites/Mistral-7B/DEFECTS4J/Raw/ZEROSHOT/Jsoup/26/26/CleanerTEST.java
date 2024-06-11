package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 private static final String HTML_SAFE_TAGS = "html, head, title, body, h1, h2, h3, p, a";
 private static final String HTML_SAFE_ATTRIBUTES = "class, id";

 @Test
 public void testCleanerWithSafeHTML() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText().addTags(HTML_SAFE_TAGS).addAttributes(HTML_SAFE_ATTRIBUTES));
 Document dirtyDocument = Jsoup.parse("<html><head></head><body><h1 class='test'>Test</h1><p id='test2'>Test2</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><h1 class='test'>Test</h1><p id='test2'>Test2</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanerWithInvalidHTML() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('Test')</script></head><body><h1 class='test'>Test</h1><p id='test2'>Test2</p><script>alert('Test2')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><h1 class='test'>Test</h1><p id='test2'>Test2</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidWithSafeHTML() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText().addTags(HTML_SAFE_TAGS).addAttributes(HTML_SAFE_ATTRIBUTES));
 Document dirtyDocument = Jsoup.parse("<html><head></head><body><h1 class='test'>Test</h1><p id='test2'>Test2</p></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidWithInvalidHTML() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('Test')</script></head><body><h1 class='test'>Test</h1><p id='test2'>Test2</p><script>alert('Test2')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testCleanerWithEmptyHTML() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html></html>", cleanDocument.html());
 }
}