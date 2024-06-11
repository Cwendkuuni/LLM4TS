package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithAllowedAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id=\"test\" class=\"test\" title=\"test\">Hello World</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithDisallowedAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id=\"test\" class=\"test\" title=\"test\" onClick=\"alert('XSS')\">Hello World</p></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanDocumentWithFrameset() {
 Document dirtyDocument = Jsoup.parse("<html><head><frameset rows=\"50%,50%\"><frame src=\"frame1.html\"><frame src=\"frame2.html\"></frameset></head><body></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
 }
}