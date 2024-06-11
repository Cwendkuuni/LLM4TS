import org.junit.*;
import static org.junit.Assert.*;

public class JsoupTest {

  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parse(html, baseUri);
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Test", document.title());
    assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
  }

  @Test
  public void testParseWithParser() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "https://www.example.com";
    Parser parser = Parser.xmlParser();
    Document document = Jsoup.parse(html, baseUri, parser);
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Test", document.title());
    assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
  }

  @Test
  public void testParseWithoutBaseUri() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
    Document document = Jsoup.parse(html);
    assertNotNull(document);
    assertEquals("", document.baseUri());
    assertEquals("Test", document.title());
    assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
  }

  @Test
  public void testConnect() {
    String url = "https://www.example.com";
    Connection connection = Jsoup.connect(url);
    assertNotNull(connection);
    assertEquals(url, connection.url());
  }

  @Test
  public void testParseFileWithCharsetAndBaseUri() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parse(file, charsetName, baseUri);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }

  @Test
  public void testParseFileWithCharset() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    Document document = Jsoup.parse(file, charsetName);
    assertNotNull(document);
    assertEquals(file.getAbsolutePath(), document.baseUri());
  }

  @Test
  public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parse(inputStream, charsetName, baseUri);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }

  @Test
  public void testParseInputStreamWithCharset() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    Document document = Jsoup.parse(inputStream, charsetName);
    assertNotNull(document);
    assertEquals("", document.baseUri());
  }

  @Test
  public void testParseBodyFragmentWithBaseUri() {
    String bodyHtml = "<h1>Hello, World!</h1>";
    String baseUri = "https://www.example.com";
    Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
    assertEquals("<h1>Hello, World!</h1>", document.body().html());
  }

  @Test
  public void testParseBodyFragmentWithoutBaseUri() {
    String bodyHtml = "<h1>Hello, World!</h1>";
    Document document = Jsoup.parseBodyFragment(bodyHtml);
    assertNotNull(document);
    assertEquals("", document.baseUri());
    assertEquals("<h1>Hello, World!</h1>", document.body().html());
  }

  @Test
  public void testParseURL() throws IOException {
    URL url = new URL("https://www.example.com");
    int timeoutMillis = 1000;
    Document document = Jsoup.parse(url, timeoutMillis);
    assertNotNull(document);
    assertEquals(url.toString(), document.baseUri());
  }

  @Test
  public void testCleanWithBaseUriAndWhitelist() {
    String bodyHtml = "<div><script>alert('XSS');</script><h1>Hello, World!</h1></div>";
    String baseUri = "https://www.example.com";
    Whitelist whitelist = Whitelist.relaxed();
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
    assertEquals("<div>&lt;h1&gt;Hello, World!&lt;/h1&gt;</div>", cleanHtml);
  }

  @Test
  public void testCleanWithWhitelist() {
    String bodyHtml = "<div><script>alert('XSS');</script><h1>Hello, World!</h1></div>";
    Whitelist whitelist = Whitelist.relaxed();
    String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
    assertEquals("<div>&lt;h1&gt;Hello, World!&lt;/h1&gt;</div>", cleanHtml);
  }

  @Test
  public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
    String bodyHtml = "<div><script>alert('XSS');</script><h1>Hello, World!</h1></div>";
    String baseUri = "https://www.example.com";
    Whitelist whitelist = Whitelist.relaxed();
    Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    assertEquals("<div>\n <h1>\n  Hello, World!\n </h1>\n</div>", cleanHtml);
  }

  @Test
  public void testIsValid() {
    String bodyHtml = "<div><script>alert('XSS');</script><h1>Hello, World!</h1></div>";
    Whitelist whitelist = Whitelist.relaxed();
    assertTrue(Jsoup.isValid(bodyHtml, whitelist));
  }
}