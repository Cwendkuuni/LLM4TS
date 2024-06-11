package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataInputSerializationHelper;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);

 String html = "<html><body>" +
 "<h1 id='title' onclick='alert(\"XSS\")'>Title</h1>" +
 "<p id='paragraph'>Paragraph<script>alert(\"XSS\")</script></p>" +
 "<ul>" +
 "<li>Item 1</li>" +
 "<li id='item2' onclick='alert(\"XSS\")'>Item 2</li>" +
 "</ul>" +
 "</body></html>";

 try {
 dirtyDocument = Jsoup.parse(DataInputSerializationHelper.fromString(html));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateNewCleaner() {
 Validate.notNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 @Test
 public void testCleanDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 Element title = cleanedDocument.selectFirst("h1");
 Assert.assertNotNull(title);
 Assert.assertEquals("Title", title.text());
 Assert.assertFalse(title.hasAttr("onclick"));

 Element paragraph = cleanedDocument.selectFirst("p");
 Assert.assertNotNull(paragraph);
 Assert.assertEquals("Paragraph", paragraph.text());
 Assert.assertFalse(paragraph.hasAttrs());

 Element list = cleanedDocument.selectFirst("ul");
 Assert.assertNotNull(list);

 List<Element> listItems = list.children();
 Assert.assertEquals(2, listItems.size());

 Element item1 = listItems.get(0);
 Assert.assertEquals("Item 1", item1.text());
 Assert.assertFalse(item1.hasAttrs());

 Element item2 = listItems.get(1);
 Assert.assertEquals("Item 2", item2.text());
 Assert.assertFalse(item2.hasAttr("onclick"));
 }

 @Test
 public void testIsValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodes() {
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(12, numDiscarded);
 }
}

class Whitelist {
 // Empty whitelist for testing purposes
}