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
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void cleanTest() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'>Hello</h1></body></html>", cleanDocument.html());
 }

 @Test
 public void isValidTest() {
 Document validDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));

 Document invalidDocument = Jsoup.parse("<html><body><h1 id='title'>Hello<script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void copySafeNodesTest() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title' class='test'><span>Hello</span></h1></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(0, numDiscarded);

 Element title = cleanDocument.selectFirst("h1");
 assertEquals("h1", title.tagName());
 assertEquals("class", title.attributeNames().get(0));
 assertEquals("test", title.classAttribute());
 }
}