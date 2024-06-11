package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.relaxed(); // Using a relaxed whitelist for testing
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<h1>Hello World!</h1>");
 }

 @Test
 public void testCleanInvalidDocumentWithTagsNotAllowed() {
 String html = "<html><head></head><body><script>alert('XSS');</script><h1>Hello World!</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<h1>Hello World!</h1>");
 assertTrue(dirtyDocument.body().hasMetaCharset()); // Ensure the meta charset is not copied
 }

 @Test
 public void testCleanInvalidDocumentWithAttributesNotAllowed() {
 String html = "<html><head></head><body><a href='javascript:alert(\"XSS\")'><h1>Hello World!</h1></a></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<h1>Hello World!</h1>");
 }

 @Test
 public void testCleanInvalidDocumentWithScriptInsideTag() {
 String html = "<html><head></head><body><script><h1>Hello World!</h1><script>alert('XSS');</script></script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<h1>Hello World!</h1>");
 }

 @Test
 public void testCleanInvalidDocumentWithScriptInsideText() {
 String html = "<html><head></head><body><h1>Hello World!<script>alert('XSS');</script></h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<h1>Hello World!</h1>");
 }

 @Test
 public void testCleanInvalidDocumentWithScriptInsideComment() {
 String html = "<html><head></head><body><!--<script>alert('XSS');</script>--><h1>Hello World!</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().html(), "<h1>Hello World!</h1>");
 }

 @Test
 public void testIsValidDocumentWithAllowedTagsAndAttributes() {
 String html = "<html><head></head><body><h1 id='title' class='title' tabindex='1'>Hello World!</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithDisallowedTags() {
 String html = "<html><head></head><body><script>alert('XSS');</script><h1>Hello World!</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithDisallowedAttributes() {
 String html = "<html><head></head><body><a href='javascript:alert(\"XSS\")'><h1>Hello World!</h1></a></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithScriptInsideTag() {
 String html = "<html><head></head><body><script><h1>Hello World!</h1><script>alert('XSS');</script></script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithScriptInsideText() {
 String html = "<html><head></head><body><h1>Hello World!<script>alert('XSS');</script></h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithScriptInsideComment() {
 String html = "<html><head></head><body><!--<script>alert('XSS');</script>--><h1>Hello World!</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }
}