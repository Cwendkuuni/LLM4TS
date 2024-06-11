import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class CleanerTest {

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 }

 @Test
 public void testEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleTextOnly()).clean(dirtyDocument);
 assertTrue(cleanDocument.body().text().isEmpty());
 }

 @Test
 public void testSimpleTextDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body>Hello World!</body></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleTextOnly()).clean(dirtyDocument);
 Element cleanBody = cleanDocument.body();
 assertEquals("Hello World!", cleanBody.text());
 }

 @Test
 public void testScriptTag() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body><script>alert('XSS Attack!');</script></body></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleTextOnly()).clean(dirtyDocument);
 Element cleanBody = cleanDocument.body();
 assertTrue(cleanBody.children().isEmpty());
 }

 @Test
 public void testSimpleTag() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body><p>Hello World!</p></body></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleTextAndBasicHtml().addTags("p")).clean(dirtyDocument);
 Element cleanBody = cleanDocument.body();
 Element cleanParagraph = cleanBody.selectFirst("p");
 assertEquals("Hello World!", cleanParagraph.text());
 }

 @Test
 public void testAttributeTag() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body><p id='test' onclick='alert(\"XSS Attack!\");'>Hello World!</p></body></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleTextAndBasicHtml().addTags("p")).clean(dirtyDocument);
 Element cleanBody = cleanDocument.body();
 Element cleanParagraph = cleanBody.selectFirst("p");
 assertTrue(cleanParagraph.attributes().isEmpty());
 }

 @Test
 public void testInvalidDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body><script>alert('XSS Attack!');</script><p>Hello World!</p></body></html>");
 boolean isValid = new Cleaner(Whitelist.simpleTextOnly()).isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testValidDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body><p id='test'>Hello World!</p></body></html>");
 boolean isValid = new Cleaner(Whitelist.simpleTextAndBasicHtml().addTags("p")).isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testComplexDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html><body><script>alert('XSS Attack!');</script><p id='test' onclick='alert(\"XSS Attack!\");'>Hello World! <a href='javascript:alert(\"XSS Attack!\");'>Link</a></p></body></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleTextAndBasicHtml().addTags("p")).clean(dirtyDocument);
 Element cleanBody = cleanDocument.body();
 Element cleanParagraph = cleanBody.selectFirst("p");
 Element cleanLink = cleanParagraph.selectFirst("a[href]");
 assertTrue(cleanParagraph.attributes().isEmpty());
 assertTrue(cleanLink.attributes().isEmpty());
 }

}