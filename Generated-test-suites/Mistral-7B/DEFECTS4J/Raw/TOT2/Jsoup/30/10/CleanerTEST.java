package org.junit.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 @Test
 public void testCleanerConstructor() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDisallowedTag() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 boolean result = cleaner.isValid(dirtyDocument);
 assertFalse(result);
 }

 @Test
 public void testIsValidDisallowedAttribute() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><a href='javascript:alert(\"XSS\")'></a></body></html>");
 boolean result = cleaner.isValid(dirtyDocument);
 assertFalse(result);
 }

 @Test
 public void testIsValidDisallowedTagAndAttribute() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><script src='javascript:alert(\"XSS\")'></script><a href='javascript:alert(\"XSS\")'></a></body></html>");
 boolean result = cleaner.isValid(dirtyDocument);
 assertFalse(result);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidNullDocument() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 boolean result = cleaner.isValid(null);
 }

 @Test
 public void testIsValidEmptyDocument() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html></html>");
 boolean result = cleaner.isValid(dirtyDocument);
 assertTrue(result);
 }

}