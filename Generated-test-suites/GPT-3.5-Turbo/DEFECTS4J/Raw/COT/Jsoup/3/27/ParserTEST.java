
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Test", doc.title());
    assertEquals("Hello World!", doc.body().text());
  }

  @Test
  public void testParseBodyFragment() {
    String html = "<h2>Body Fragment</h2><p>This is a paragraph.</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(html, baseUri);
    assertNotNull(doc);
    assertEquals("Body Fragment", doc.body().children().get(0).text());
    assertEquals("This is a paragraph.", doc.body().children().get(1).text());
  }

  @Test
  public void testParseBodyFragment_RelaxedParsing() {
    String html = "<h2>Body Fragment</h2><p>This is a paragraph.</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(html, baseUri);
    assertNotNull(doc);
    assertEquals("Body Fragment", doc.body().children().get(0).text());
    assertEquals("This is a paragraph.", doc.body().children().get(1).text());
  }

  @Test
  public void testParse_Comment() {
    String html = "<!-- This is a comment -->";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals(" This is a comment ", doc.body().text());
  }

  @Test
  public void testParse_Cdata() {
    String html = "<![CDATA[This is some CDATA content]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("This is some CDATA content", doc.body().text());
  }

  @Test
  public void testParse_XmlDecl() {
    String html = "<?xml version='1.0' encoding='UTF-8'?><html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Hello World!", doc.body().text());
  }

  @Test
  public void testParse_EndTag() {
    String html = "<html><body><h1>Title</h1><p>This is a paragraph.</p></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Title", doc.body().getElementsByTag("h1").text());
    assertEquals("This is a paragraph.", doc.body().getElementsByTag("p").text());
  }

  @Test
  public void testParse_StartTag() {
    String html = "<html><body><h1>Title</h1><p>This is a paragraph.</p></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Title", doc.body().getElementsByTag("h1").text());
    assertEquals("This is a paragraph.", doc.body().getElementsByTag("p").text());
  }

  @Test
  public void testParse_TextNode() {
    String html = "<html><body>This is some text.</body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("This is some text.", doc.body().text());
  }

  @Test
  public void testParse_Cdata() {
    String html = "<![CDATA[This is some CDATA content]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("This is some CDATA content", doc.body().text());
  }
}
