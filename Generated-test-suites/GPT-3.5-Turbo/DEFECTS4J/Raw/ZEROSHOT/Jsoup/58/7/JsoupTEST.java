
package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {
  
  @Test
  public void testParseWithBaseUri() {
    String html = "<html><head></head><body>Hello World!</body></html>";
    String baseUri = "http://example.com";
    
    Document document = Jsoup.parse(html, baseUri);
    
    assertNotNull(document);
    assertEquals(html, document.html());
  }
  
  @Test
  public void testParseWithParser() {
    String html = "<html><head></head><body>Hello World!</body></html>";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    Document document = Jsoup.parse(html, baseUri, parser);
    
    assertNotNull(document);
    assertEquals(html, document.html());
  }
  
  @Test
  public void testParseWithoutBaseUri() {
    String html = "<html><head></head><body>Hello World!</body></html>";
    
    Document document = Jsoup.parse(html);
    
    assertNotNull(document);
    assertEquals(html, document.html());
  }
  
  @Test
  public void testConnect() throws IOException {
    String url = "http://example.com";
    
    Connection connection = Jsoup.connect(url);
    
    assertNotNull(connection);
    assertEquals(url, connection.url());
  }
  
  @Test
  public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    Document document = Jsoup.parse(file, charsetName, baseUri);
    
    assertNotNull(document);
    assertEquals(file.getAbsolutePath(), document.baseUri());
  }
  
  @Test
  public void testParseFileWithCharsetName() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    
    Document document = Jsoup.parse(file, charsetName);
    
    assertNotNull(document);
    assertEquals(file.getAbsolutePath(), document.baseUri());
  }
  
  @Test
  public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    Document document = Jsoup.parse(inputStream, charsetName, baseUri);
    
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testParseInputStreamWithCharsetName() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    
    Document document = Jsoup.parse(inputStream, charsetName);
    
    assertNotNull(document);
    assertNotNull(document.baseUri());
  }
  
  @Test
  public void testParseBodyFragmentWithBaseUri() {
    String bodyHtml = "<div>Hello World!</div>";
    String baseUri = "http://example.com";
    
    Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(document);
    assertEquals(bodyHtml, document.body().html());
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testParseBodyFragmentWithoutBaseUri() {
    String bodyHtml = "<div>Hello World!</div>";
    
    Document document = Jsoup.parseBodyFragment(bodyHtml);
    
    assertNotNull(document);
    assertEquals(bodyHtml, document.body().html());
    assertNotNull(document.baseUri());
  }
  
  @Test
  public void testParseUrl() throws IOException {
    URL url = new URL("http://example.com");
    int timeoutMillis = 5000;
    
    Document document = Jsoup.parse(url, timeoutMillis);
    
    assertNotNull(document);
    assertEquals(url.toString(), document.location());
  }
  
  @Test
  public void testCleanWithBaseUriAndWhitelist() {
    String bodyHtml = "<div><script>alert('XSS')</script><p>Hello World!</p></div>";
    String baseUri = "http://example.com";
    Whitelist whitelist = Whitelist.basic();
    
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
    
    assertEquals("<p>Hello World!</p>", cleanHtml);
  }
  
  @Test
  public void testCleanWithoutBaseUriAndWhitelist() {
    String bodyHtml = "<div><script>alert('XSS')</script><p>Hello World!</p></div>";
    Whitelist whitelist = Whitelist.basic();
    
    String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
    
    assertEquals("<p>Hello World!</p>", cleanHtml);
  }
  
  @Test
  public void testCleanWithBaseUriWhitelistAndOutputSettings() {
    String bodyHtml = "<div><script>alert('XSS')</script><p>Hello World!</p></div>";
    String baseUri = "http://example.com";
    Whitelist whitelist = Whitelist.basic();
    Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
    
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    
    assertEquals("<p>\n" +
            " Hello World!\n" +
            "</p>", cleanHtml);
  }
  
  @Test
  public void testIsValidWithWhitelist() {
    String bodyHtml = "<div><script>alert('XSS')</script><p>Hello World!</p></div>";
    Whitelist whitelist = Whitelist.basic();
    
    boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
    
    assertFalse(isValid);
  }

}
