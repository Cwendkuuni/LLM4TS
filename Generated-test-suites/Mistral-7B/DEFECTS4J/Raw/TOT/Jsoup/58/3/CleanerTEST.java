package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanMethod() {
 String html = "<html><body><h1 id='title'>Hello</h1><script>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 String expectedCleanHtml = "<html><body><h1 id='title'>Hello</h1></body></html>";
 String actualCleanHtml = cleanDocument.body().html();

 assertTrue(cleanDocument.body().children().size() <= dirtyDocument.body().children().size());
 assertEquals(expectedCleanHtml, actualCleanHtml);
 }

 @Test
 public void testIsValidMethod() {
 String html = "<html><body><h1 id='title'>Hello</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidMethod() {
 String html = "<html><body><h1 id='title'>Hello</h1><script>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanMethodWithEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanDocument.body().getText().isEmpty());
 }

 @Test
 public void testCleanMethodWithEmptyBody() {
 Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanDocument.body().getText().isEmpty());
 }

 @Test
 public void testCleanMethodWithWhitespaceOnlyDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body>&nbsp;</body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanDocument.body().getText().isEmpty());
 }

 @Test
 public void testCleanMethodWithComments() {
 String html = "<html><!--comment--><body><h1 id='title'>Hello</h1><script>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 Document cleanDocument = cleaner.clean(dirtyDocument);
 String expectedCleanHtml = "<html><body><h1 id='title'>Hello</h1></body></html>";
 String actualCleanHtml = cleanDocument.body().html();

 assertEquals(expectedCleanHtml, actualCleanHtml);
 }

 @Test
 public void testCleanMethodWithXMLProcessingInstructions() {
 String html = "<html xmlns='http://www.w3.org/1999/xhtml'><!--comment--><body>&xml-declaration;<h1 id='title'>Hello</h1><script>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 Document cleanDocument = cleaner.clean(dirtyDocument);
 String expectedCleanHtml = "<html><body><h1 id='title'>Hello</h1></body></html>";
 String actualCleanHtml = cleanDocument.body().html();

 assertEquals(expectedCleanHtml, actualCleanHtml);
 }
}