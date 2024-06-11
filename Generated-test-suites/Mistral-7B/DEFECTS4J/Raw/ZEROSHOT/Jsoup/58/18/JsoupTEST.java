package org.jsoup.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URL;

public class JsoupTest {

 @Test
 public void testParseString() {
 Document doc = Jsoup.parse("<html><head></head><body><h1>Hello World</h1></body></html>");
 assertNotNull(doc);
 }

 @Test
 public void testParseStringWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 Element img = doc.select("img").first();
 assertNotNull(img);
 assertNotNull(img.attr("src"));
 assertNotNull(new URL(baseUri, img.attr("src")));
 }

 @Test
 public void testParseStringWithParser() {
 Document doc = Jsoup.parse("<html><head></head><body><h1>Hello World</h1></body></html>", Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseStringWithEmptyBaseUri() {
 Document doc = Jsoup.parse("<img src=\"image.jpg\" />");
 Element img = doc.select("img").first();
 assertNull(img.attr("src"));
 }

 @Test
 public void testConnect() {
 try {
 Connection connection = Jsoup.connect("http://google.com");
 Document doc = connection.get();
 assertNotNull(doc);
 } catch (IOException e) {
 // ignore
 }
 }

 @Test
 public void testParseFile() {
 try {
 Document doc = Jsoup.parse(new File("test.html"), "UTF-8");
 assertNotNull(doc);
 } catch (IOException e) {
 // ignore
 }
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = Jsoup.class.getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(inputStream, "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseInputStreamWithParser() throws IOException {
 InputStream inputStream = Jsoup.class.getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(inputStream, Parser.xmlParser(), "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Jsoup.parseBodyFragment("<p>Hello World</p>");
 assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parseBodyFragment(html, baseUri);
 Element img = doc.select("img").first();
 assertNull(img.attr("src"));
 }

 @Test
 public void testParseUrl() throws IOException {
 Document doc = Jsoup.parse(new URL("http://google.com"));
 assertNotNull(doc);
 }

 @Test
 public void testClean() {
 String html = "<script>alert('Hello World')</script><p>Hello World</p>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 String cleanedHtml = Jsoup.clean(html, "", whitelist);
 Document doc = Jsoup.parse(cleanedHtml);
 Element p = doc.select("p").first();
 assertNotNull(p.text());
 assertNull(doc.select("script").first());
 }

 @Test
 public void testCleanWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.simpleTagNames("img");
 String cleanedHtml = Jsoup.clean(html, baseUri, whitelist);
 Document doc = Jsoup.parse(cleanedHtml);
 Element img = doc.select("img").first();
 assertNull(img.attr("src"));
 }

 @Test
 public void testCleanWithOutputSettings() {
 String html = "<p><script>alert('Hello World')</script>Hello World</p>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true).escapeMode(Document.EscapeMode.HTML);
 String cleanedHtml = Jsoup.clean(html, "", whitelist, outputSettings);
 Document doc = Jsoup.parse(cleanedHtml);
 Element p = doc.select("p").first();
 assertNotNull(p.text());
 assertNull(doc.select("script").first());
 }

 @Test
 public void testIsValid() {
 String html = "<p><script>alert('Hello World')</script>Hello World</p>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 boolean isValid = Jsoup.isValid(html, whitelist);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidWithClean() {
 String html = "<p><script>alert('Hello World')</script>Hello World</p>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 String cleanedHtml = Jsoup.clean(html, "", whitelist);
 boolean isValid = Jsoup.isValid(cleanedHtml, whitelist);
 assertTrue(isValid);
 }
}