package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
  
  @Test
  public void testParseWithBaseUri() {
    String html = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com/";
    Document document = Jsoup.parse(html, baseUri);
    Assert.assertEquals(baseUri, document.baseUri());
    Assert.assertEquals(html, document.html());
  }
  
  @Test
  public void testParseWithParser() {
    String html = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com/";
    Parser parser = Parser.xmlParser();
    Document document = Jsoup.parse(html, baseUri, parser);
    Assert.assertEquals(baseUri, document.baseUri());
    Assert.assertEquals(html, document.html());
  }
  
  @Test
  public void testParse() {
    String html = "<html><body><h1>Hello World</h1></body></html>";
    Document document = Jsoup.parse(html);
    Assert.assertEquals("", document.baseUri());
    Assert.assertEquals(html, document.html());
  }
  
  @Test
  public void testConnect() {
    String url = "http://example.com/";
    Connection connection = Jsoup.connect(url);
    Assert.assertEquals(url, connection.url());
  }
  
  @Test
  public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com/";
    Document document = Jsoup.parse(file, charsetName, baseUri);
    Assert.assertEquals(baseUri, document.baseUri());
    Assert.assertEquals("", document.html());
  }
  
  @Test
  public void testParseFileWithCharsetName() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    Document document = Jsoup.parse(file, charsetName);
    Assert.assertEquals(file.getAbsolutePath(), document.baseUri());
    Assert.assertEquals("", document.html());
  }
  
  @Test
  public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com/";
    Document document = Jsoup.parse(inputStream, charsetName, baseUri);
    Assert.assertEquals(baseUri, document.baseUri());
    Assert.assertEquals("", document.html());
  }
  
  @Test
  public void testParseInputStreamWithCharsetName() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    Document document = Jsoup.parse(inputStream, charsetName, "");
    Assert.assertEquals("", document.baseUri());
    Assert.assertEquals("", document.html());
  }
  
  @Test
  public void testParseBodyFragmentWithBaseUri() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com/";
    Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
    Assert.assertEquals(baseUri, document.baseUri());
    Assert.assertEquals(bodyHtml, document.html());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World</h1>";
    Document document = Jsoup.parseBodyFragment(bodyHtml);
    Assert.assertEquals("", document.baseUri());
    Assert.assertEquals(bodyHtml, document.html());
  }
  
  @Test
  public void testParseURLWithTimeout() throws IOException {
    URL url = new URL("http://example.com/");
    int timeoutMillis = 5000;
    Document document = Jsoup.parse(url, timeoutMillis);
    Assert.assertEquals(url.toString(), document.baseUri());
    Assert.assertEquals("", document.html());
  }
  
  @Test
  public void testCleanWithBaseUriAndWhitelist() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com/";
    Whitelist whitelist = Whitelist.basic();
    String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
    Assert.assertEquals(bodyHtml, cleanedHtml);
  }
  
  @Test
  public void testCleanWithWhitelist() {
    String bodyHtml = "<h1>Hello World</h1>";
    Whitelist whitelist = Whitelist.basic();
    String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
    Assert.assertEquals(bodyHtml, cleanedHtml);
  }
  
  @Test
  public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com/";
    Whitelist whitelist = Whitelist.basic();
    Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    Assert.assertEquals(bodyHtml, cleanedHtml);
  }
  
  @Test
  public void testIsValid() {
    String bodyHtml = "<h1>Hello World</h1>";
    Whitelist whitelist = Whitelist.basic();
    boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
    Assert.assertTrue(isValid);
  }
}