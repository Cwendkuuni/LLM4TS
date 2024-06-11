package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.List;

public class CleanerTest {

 private static final String TEST_HTML = "<html><body><div id=\"test\" foo=\"bar\">Test <script>alert('XSS');</script></div></body></html>";

 @BeforeClass
 public static void setup() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanNullDocument() {
 Document dirtyDocument = null;
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument);
 }

 @Test
 public void testCleanEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("", cleanDocument.body().outerHtml());
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<p>This is a valid document.</p>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<p>This is a valid document.</p>", cleanDocument.body().outerHtml());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 // The clean document should only contain the safe elements and attributes
 // and the number of discarded elements and attributes should be greater than zero
 assertNotEquals(TEST_HTML, cleanDocument.body().outerHtml());
 assertTrue(cleanDocument.body().childNodes().size() < dirtyDocument.body().childNodes().size());
 }

 @Test
 public void testIsValidNullDocument() {
 Document dirtyDocument = null;
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<p>This is a valid document.</p>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements(TEST_HTML);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }
}