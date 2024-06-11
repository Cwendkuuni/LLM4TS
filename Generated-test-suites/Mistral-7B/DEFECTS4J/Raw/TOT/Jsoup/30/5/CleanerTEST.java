package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanMethodWithValidInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanMethodWithInvalidInput() {
 String html = "<html><body><script>alert('XSS Attack')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethodWithValidInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValidMethodWithInvalidInput() {
 String html = "<html><body><script>alert('XSS Attack')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidMethodWithEmptyInput() {
 Document dirtyDocument = Jsoup.parse("");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullInput() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidMethodWithNullInput() {
 cleaner.isValid(null);
 }
}