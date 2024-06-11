package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello World</h1><script>alert('XSS')</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testClean() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument, cleanedDocument);
 List<Element> cleanedElements = cleanedDocument.body().children();
 assertEquals(1, cleanedElements.size());
 assertEquals("Hello World", cleanedElements.get(0).text());
 }

 @Test
 public void testIsValidInvalid() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidValid() {
 Document validDocument = Jsoup.parse("<html><body><h1 id='title'>Hello World</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }
}