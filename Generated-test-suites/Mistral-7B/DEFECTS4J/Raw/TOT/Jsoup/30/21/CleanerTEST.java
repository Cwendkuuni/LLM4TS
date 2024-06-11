package org.jsoup.safety;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String HTML_WITH_SAFE_TAGS = "<html><body><p>Safe tag: <b>Hello World</b></p></body></html>";
 private static final String HTML_WITH_UNSAFE_TAGS = "<html><body><p>Unsafe tag: <script>alert('XSS attack!');</script></p></body></html>";
 private static final String HTML_WITH_SAFE_AND_UNSAFE_TAGS = "<html><body><p>Safe tag: <b>Hello World</b></p><script>alert('XSS attack!');</script></p></body></html>";
 private static final String HTML_EMPTY = "<html></html>";

 @Test
 public void testClean_SafeHtml() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document clean = cleaner.clean(dirtyDocument);
 Parser parser = new Parser.MalformedEncodingDetector(); // needed for JUnit 4
 String expectedCleanHtml = "<html><body><p><b>Hello World</b></p></body></html>";
 Assertions.assertEquals(parser.parseDocument(expectedCleanHtml).outerHtml(), clean.outerHtml());
 }

 @Test
 public void testClean_InvalidHtml_ReturnsNewDocument() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_UNSAFE_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document clean = cleaner.clean(dirtyDocument);
 Assertions.assertNotNull(clean);
 Assertions.assertNotEquals(dirtyDocument.outerHtml(), clean.outerHtml());
 }

 @Test
 public void testClean_InvalidHtml_DiscardsUnsafeTags() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_AND_UNSAFE_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document clean = cleaner.clean(dirtyDocument);
 String expectedCleanHtml = "<html><body><p><b>Hello World</b></p></body></html>";
 Assertions.assertEquals(parser.parseDocument(expectedCleanHtml).outerHtml(), clean.outerHtml());
 }

 @Test
 public void testIsValid_SafeHtml() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertTrue(isValid);
 }

 @Test
 public void testIsValid_InvalidHtml() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_UNSAFE_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertFalse(isValid);
 }

 @Test
 public void testIsValid_EmptyHtml() {
 Document dirtyDocument = Jsoup.parse(HTML_EMPTY);
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertTrue(isValid);
 }
}