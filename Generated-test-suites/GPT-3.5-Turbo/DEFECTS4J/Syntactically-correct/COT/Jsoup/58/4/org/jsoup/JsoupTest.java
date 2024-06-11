import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

  //

  @Test
  public void testParse() {
    String html = "<html><head></head><body>Hello World</body></html>";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parse(html, baseUri);
    assertNotNull(document);
    assertEquals(html, document.outerHtml());
  }

  @Test
  public void testParseWithParser() {
    String html = "<html><head></head><body>Hello World</body></html>";
    String baseUri = "https://www.example.com";
    Parser parser = Parser.htmlParser();
    Document document = Jsoup.parse(html, baseUri, parser);
    assertNotNull(document);
    assertEquals(html, document.outerHtml());
  }

  @Test
  public void testParseWithoutBaseUri() {
    String html = "<html><head></head><body>Hello World</body></html>";
    Document document = Jsoup.parse(html);
    assertNotNull(document);
    assertEquals(html, document.outerHtml());
  }

  @Test
  public void testConnect() {
    String url = "https://www.example.com";
    Connection connection = Jsoup.connect(url);
    assertNotNull(connection);
    assertEquals(url, connection.url().toString());
  }

  @Test
  public void testParseFileWithCharset() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parse(file, charsetName, baseUri);
    assertNotNull(document);
    file.delete(); // Clean up
  }

  @Test
  public void testParseFileWithoutCharset() throws IOException {
    File file = new File("test.html");
    String charsetName = null;
    Document document = Jsoup.parse(file, charsetName);
    assertNotNull(document);
    file.delete(); // Clean up
  }

  @Test
  public void testParseInputStreamWithCharset() throws IOException {
    FileInputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parse(inputStream, charsetName, baseUri);
    assertNotNull(document);
    inputStream.close(); // Clean up
  }

  @Test
  public void testParseInputStreamWithParser() throws IOException {
    FileInputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "https://www.example.com";
    Parser parser = Parser.htmlParser();
    Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
    assertNotNull(document);
    inputStream.close(); // Clean up
  }

  @Test
  public void testParseBodyFragmentWithBaseUri() {
    String bodyHtml = "<div>Hello World</div>";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(document);
    assertEquals(bodyHtml, document.body().html());
  }

  @Test
  public void testParseBodyFragmentWithoutBaseUri() {
    String bodyHtml = "<div>Hello World</div>";
    Document document = Jsoup.parseBodyFragment(bodyHtml);
    assertNotNull(document);
    assertEquals(bodyHtml, document.body().html());
  }

  @Test
  public void testParseURLWithTimeout() throws IOException {
    URL url = new URL("https://www.example.com");
    int timeoutMillis = 5000;
    Document document = Jsoup.parse(url, timeoutMillis);
    assertNotNull(document);
    assertEquals(url.toString(), document.location());
  }

  @Test
  public void testCleanWithWhitelist() {
    String bodyHtml = "<div><script>alert('XSS');</script></div>";
    String baseUri = "https://www.example.com";
    Whitelist whitelist = Whitelist.basic();
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
    String expectedHtml = "<div></div>";
    assertEquals(expectedHtml, cleanHtml);
  }

  @Test
  public void testCleanWithWhitelistWithoutBaseUri() {
    String bodyHtml = "<div><script>alert('XSS');</script></div>";
    Whitelist whitelist = Whitelist.basic();
    String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
    String expectedHtml = "<div></div>";
    assertEquals(expectedHtml, cleanHtml);
  }

  @Test
  public void testCleanWithWhitelistAndOutputSettings() {
    String bodyHtml = "<div><script>alert('XSS');</script></div>";
    String baseUri = "https://www.example.com";
    Whitelist whitelist = Whitelist.basic();
    Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    String expectedHtml = "<div></div>";
    assertEquals(expectedHtml, cleanHtml);
  }

  @Test
  public void testIsValid() {
    String bodyHtml = "<div><p>Hello World</p></div>";
    Whitelist whitelist = Whitelist.basic();
    boolean valid = Jsoup.isValid(bodyHtml, whitelist);
    assertTrue(valid);
  }

  //

}