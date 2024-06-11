import static org.junit.Assert.*;
import org.junit.Test;

public class JsoupTest {

  @Test
  public void testParse() {
    String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
    String baseUri = "http://example.com";
    Document doc = Jsoup.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseWithParser() {
    String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
    String baseUri = "http://example.com";
    Parser parser = Parser.xmlParser();
    Document doc = Jsoup.parse(html, baseUri, parser);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseWithoutBaseUri() {
    String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
    Document doc = Jsoup.parse(html);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testConnect() {
    String url = "http://example.com";
    Connection connection = Jsoup.connect(url);
    assertNotNull(connection);
    assertEquals(url, connection.url().toString());
  }

  @Test
  public void testParseFileWithCharsetAndBaseUri() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Document doc = Jsoup.parse(file, charsetName, baseUri);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseFileWithCharset() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    Document doc = Jsoup.parse(file, charsetName);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
    InputStream in = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Document doc = Jsoup.parse(in, charsetName, baseUri);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseInputStreamWithCharset() throws IOException {
    InputStream in = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    Document doc = Jsoup.parse(in, charsetName);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseBodyFragmentWithBaseUri() {
    String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
    String baseUri = "http://example.com";
    Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseBodyFragmentWithoutBaseUri() {
    String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
    Document doc = Jsoup.parseBodyFragment(bodyHtml);
    assertNotNull(doc);
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testParseUrlWithTimeout() throws IOException {
    URL url = new URL("http://example.com");
    int timeoutMillis = 5000;
    Document doc = Jsoup.parse(url, timeoutMillis);
    assertNotNull(doc);
    assertEquals("Title", doc.title());
    assertEquals(1, doc.select("h1").size());
    assertEquals(1, doc.select("p").size());
  }

  @Test
  public void testCleanWithBaseUriAndWhitelist() {
    String bodyHtml = "<p>Paragraph</p><script>console.log('hacked');</script>";
    String baseUri = "http://example.com";
    Whitelist whitelist = Whitelist.basic();
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
    assertNotNull(cleanHtml);
    assertEquals("<p>Paragraph</p>", cleanHtml);
  }

  @Test
  public void testCleanWithWhitelist() {
    String bodyHtml = "<p>Paragraph</p><script>console.log('hacked');</script>";
    Whitelist whitelist = Whitelist.basic();
    String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
    assertNotNull(cleanHtml);
    assertEquals("<p>Paragraph</p>", cleanHtml);
  }

  @Test
  public void testCleanWithBaseUriWhitelistAndOutputSettings() {
    String bodyHtml = "<p>Paragraph</p><script>console.log('hacked');</script>";
    String baseUri = "http://example.com";
    Whitelist whitelist = Whitelist.basic();
    Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    assertNotNull(cleanHtml);
    assertEquals("<p>Paragraph</p>", cleanHtml);
  }

  @Test
  public void testIsValidWithWhitelist() {
    String bodyHtml = "<p>Paragraph</p><script>console.log('hacked');</script>";
    Whitelist whitelist = Whitelist.basic();
    boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
    assertFalse(isValid);
  }

}