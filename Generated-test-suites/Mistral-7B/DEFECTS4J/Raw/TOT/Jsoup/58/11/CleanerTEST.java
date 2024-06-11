package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String HTML_WITH_VALID_TAGS = "<html><body><h1 class='title'>Hello World</h1><p>Some content.</p></body></html>";
 private static final String HTML_WITH_INVALID_TAGS = "<html><body><script src='malicious.js'></script><h1 class='title'>Hello World</h1><p>Some content.</p></body></html>";

 @Test
 public void testCleanValidDocument() throws Exception {
 Whitelist whitelist = Whitelist.simpleText(); // a simple whitelist that only allows text nodes
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse(HTML_WITH_VALID_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertEquals("Clean document should have the same HTML structure as the original one", HTML_WITH_VALID_TAGS, cleanDocument.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCleanerWithNullWhitelist() {
 // test that Cleaner constructor throws an IllegalArgumentException when passed a null whitelist
 Cleaner cleaner = new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 // test that clean method throws a NullPointerException when passed a null document
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document dirtyDocument = null; // null document
 Document cleanDocument = cleaner.clean(dirtyDocument);
 }

 @Test
 public void testIsValidWithValidDocument() throws Exception {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document dirtyDocument = Jsoup.parse(HTML_WITH_VALID_TAGS);
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertTrue("Document should be considered valid if it only contains safe tags and attributes", isValid);
 }

 @Test
 public void testIsValidWithInvalidDocument() throws Exception {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document dirtyDocument = Jsoup.parse(HTML_WITH_INVALID_TAGS);
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertFalse("Document should be considered invalid if it contains unsafe tags or attributes", isValid);
 }
}