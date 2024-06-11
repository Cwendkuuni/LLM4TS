package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test cases for Cleaner class
 */
public class CleanerTest {

 private static final Whitelist WHITELIST = Whitelist.simpleText();

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(WHITELIST);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS!')</script></body></html>");
 Document cleanDocument = new Cleaner(WHITELIST).clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocumentHeadEmpty() {
 Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
 assertTrue(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyEmpty() {
 Document dirtyDocument = Jsoup.parse("<html><head></head></html>");
 assertFalse(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithAllowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"test\"></div></body></html>");
 assertTrue(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithDisallowedTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script id=\"test\">alert('XSS!')</script></body></html>");
 assertFalse(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithAllowedTagAndAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"test\" class=\"test-class\"></div></body></html>");
 assertTrue(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithDisallowedAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"test\" onclick=\"alert('XSS!')\"></div></body></html>");
 assertFalse(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithNestedAllowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"test\"><p>Hello World</p></div></body></html>");
 assertTrue(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithNestedDisallowedTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"test\"><script>alert('XSS!')</script></div></body></html>");
 assertFalse(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithParseErrors() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>document.write('Hello World')</script></body></html>", "text/html");
 ParseErrorList parseErrors = new Parser().parse(dirtyDocument.html(), dirtyDocument.baseUri());
 assertTrue(parseErrors.size() > 0);
 assertTrue(new Cleaner(WHITELIST).isValid(dirtyDocument));
 }

}