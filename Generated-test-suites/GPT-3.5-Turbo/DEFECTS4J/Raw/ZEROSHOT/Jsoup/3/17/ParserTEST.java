import org.jsoup.nodes.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

  @Test
  public void testParse() {
    String html = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertEquals("Test Page", doc.title());
    assertEquals("Hello, World!", doc.body().child(0).text());
  }

  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello, World!</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

    assertEquals("Hello, World!", doc.body().child(0).text());
  }

  @Test
  public void testParseComment() {
    String html = "<!-- This is a comment -->";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    parser.parseComment();

    Document doc = parser.parse();

    assertEquals("<!-- This is a comment -->", doc.toString());
  }

  @Test
  public void testParseXmlDecl() {
    String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    parser.parseXmlDecl();

    Document doc = parser.parse();

    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.toString());
  }

  @Test
  public void testParseEndTag() {
    String html = "<h1>Hello, World!</h1>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    parser.parseEndTag();

    Document doc = parser.parse();

    assertEquals("<h1></h1>", doc.toString());
  }

  @Test
  public void testParseStartTag() {
    String html = "<h1>Hello, World!</h1>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    parser.parseStartTag();

    Document doc = parser.parse();

    assertEquals("<h1>Hello, World!</h1>", doc.toString());
  }

  @Test
  public void testParseTextNode() {
    String html = "Hello, World!";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    parser.parseTextNode();

    Document doc = parser.parse();

    assertEquals("Hello, World!", doc.toString());
  }

  @Test
  public void testParseCdata() {
    String html = "<![CDATA[This is CDATA]]>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    parser.parseCdata();

    Document doc = parser.parse();

    assertEquals("<![CDATA[This is CDATA]]>", doc.toString());
  }

  @Test
  public void testAddChildToParent() {
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    Element child = new Element(Tag.valueOf("h2"), baseUri);
    parser.addChildToParent(child, false);

    Document doc = parser.parse();

    assertEquals("<html><body><h1>Hello, World!</h1><h2></h2></body></html>", doc.toString());
  }

  @Test
  public void testStackHasValidParent() {
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    Element child = new Element(Tag.valueOf("h1"), baseUri);
    boolean validAncestor = parser.stackHasValidParent(child.tag());

    assertTrue(validAncestor);
  }

  @Test
  public void testPopStackToSuitableContainer() {
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    Element parent = new Element(Tag.valueOf("body"), baseUri);
    parser.stack.addLast(parent);
    Element child = new Element(Tag.valueOf("h1"), baseUri);
    parser.popStackToSuitableContainer(child.tag());

    assertEquals(parent, parser.stack.getLast());
  }

  @Test
  public void testPopStackToClose() {
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    Element parent = new Element(Tag.valueOf("body"), baseUri);
    parser.stack.addLast(parent);
    Element child = new Element(Tag.valueOf("h1"), baseUri);
    parser.stack.addLast(child);
    Element elToClose = parser.popStackToClose(child.tag());

    assertEquals(child, elToClose);
    assertTrue(parser.stack.isEmpty());
  }

  @Test
  public void testLast() {
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(html, baseUri, false);
    Element parent = new Element(Tag.valueOf("body"), baseUri);
    parser.stack.addLast(parent);
    Element last = parser.last();

    assertEquals(parent, last);
  }

}