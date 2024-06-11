import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  // Test cases for parseInput method
  @Test
  public void testParseInput() {
    // Typical use case: valid HTML with base URI
    String html = "<html><head></head><body><p>Hello World!</p></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(new HtmlTreeBuilder());
    Document doc = parser.parseInput(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());

    // Typical use case: valid HTML without base URI
    String html = "<html><head></head><body><p>Hello World!</p></body></html>";
    String baseUri = null;
    Parser parser = new Parser(new HtmlTreeBuilder());
    Document doc = parser.parseInput(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertNull(doc.baseUri());

    // Edge case: empty HTML
    String html = "";
    String baseUri = "http://example.com";
    Parser parser = new Parser(new HtmlTreeBuilder());
    Document doc = parser.parseInput(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());

    // Error scenario: invalid HTML
    String html = "<html><head></head><body><p>Hello World!</p></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(new HtmlTreeBuilder());
    Document doc = parser.parseInput(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertTrue(parser.getErrors().size() > 0);
  }

  // Test cases for setTreeBuilder method
  @Test
  public void testSetTreeBuilder() {
    // Typical use case: setting a new TreeBuilder
    Parser parser = new Parser(new HtmlTreeBuilder());
    TreeBuilder newTreeBuilder = new XmlTreeBuilder();
    Parser newParser = parser.setTreeBuilder(newTreeBuilder);
    assertSame(newTreeBuilder, newParser.getTreeBuilder());

    // Edge case: setting a null TreeBuilder
    Parser parser = new Parser(new HtmlTreeBuilder());
    Parser newParser = parser.setTreeBuilder(null);
    assertNull(newParser.getTreeBuilder());
  }

  // Test cases for setTrackErrors method
  @Test
  public void testSetTrackErrors() {
    // Typical use case: enabling parse error tracking
    Parser parser = new Parser(new HtmlTreeBuilder());
    Parser newParser = parser.setTrackErrors(10);
    assertTrue(newParser.isTrackErrors());

    // Edge case: disabling parse error tracking
    Parser parser = new Parser(new HtmlTreeBuilder());
    Parser newParser = parser.setTrackErrors(0);
    assertFalse(newParser.isTrackErrors());
  }

  // Test cases for getErrors method
  @Test
  public void testGetErrors() {
    // Typical use case: getting parse errors after parsing
    Parser parser = new Parser(new HtmlTreeBuilder());
    parser.parseInput("<html></head><body><p>Hello World!</p></body></html>", null);
    List<ParseError> errors = parser.getErrors();
    assertNotNull(errors);
    assertTrue(errors.size() > 0);

    // Edge case: getting parse errors without parsing
    Parser parser = new Parser(new HtmlTreeBuilder());
    List<ParseError> errors = parser.getErrors();
    assertNotNull(errors);
    assertTrue(errors.isEmpty());
  }

  // Test cases for parse method
  @Test
  public void testParse() {
    // Typical use case: parsing valid HTML with base URI
    String html = "<html><head></head><body><p>Hello World!</p></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());

    // Typical use case: parsing valid HTML without base URI
    String html = "<html><head></head><body><p>Hello World!</p></body></html>";
    String baseUri = null;
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertNull(doc.baseUri());

    // Edge case: parsing empty HTML with base URI
    String html = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());

    // Error scenario: parsing invalid HTML with base URI
    String html = "<html><head></head><body><p>Hello World!</p></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertTrue(Parser.getErrors().size() > 0);
  }

  // Test cases for parseFragment method
  @Test
  public void testParseFragment() {
    // Typical use case: parsing valid HTML fragment with context element and base URI
    String fragmentHtml = "<p>Hello World!</p>";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertEquals(1, nodes.size());

    // Typical use case: parsing valid HTML fragment without context element and with base URI
    String fragmentHtml = "<p>Hello World!</p>";
    Element context = null;
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertEquals(1, nodes.size());

    // Edge case: parsing empty HTML fragment with context element and base URI
    String fragmentHtml = "";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertTrue(nodes.isEmpty());

    // Error scenario: parsing invalid HTML fragment with context element and base URI
    String fragmentHtml = "<p>Hello World!";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertTrue(Parser.getErrors().size() > 0);
  }

  // Test cases for parseBodyFragment method
  @Test
  public void testParseBodyFragment() {
    // Typical use case: parsing valid HTML body fragment with base URI
    String bodyHtml = "<p>Hello World!</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertEquals(1, doc.body().children().size());

    // Edge case: parsing empty HTML body fragment with base URI
    String bodyHtml = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertEquals(0, doc.body().children().size());

    // Error scenario: parsing invalid HTML body fragment with base URI
    String bodyHtml = "<p>Hello World!";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertTrue(Parser.getErrors().size() > 0);
  }

  // Test cases for deprecated parseBodyFragmentRelaxed method
  @Test
  public void testParseBodyFragmentRelaxed() {
    // Typical use case: parsing valid HTML body fragment with base URI
    String bodyHtml = "<p>Hello World!</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertEquals(1, doc.body().children().size());

    // Edge case: parsing empty HTML body fragment with base URI
    String bodyHtml = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertEquals(0, doc.body().children().size());

    // Error scenario: parsing invalid HTML body fragment with base URI
    String bodyHtml = "<p>Hello World!";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals(Document.OutputSettings.Syntax.html, doc.outputSettings().syntax());
    assertEquals(baseUri, doc.baseUri());
    assertTrue(Parser.getErrors().size() > 0);
  }

  // Test cases for htmlParser method
  @Test
  public void testHtmlParser() {
    // Typical use case: creating a new HTML parser
    Parser parser = Parser.htmlParser();
    assertNotNull(parser);
    assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);

    // Edge case: creating a new HTML parser with null TreeBuilder
    Parser parser = new Parser(null);
    assertNotNull(parser);
    assertNull(parser.getTreeBuilder());
  }

  // Test cases for xmlParser method
  @Test
  public void testXmlParser() {
    // Typical use case: creating a new XML parser
    Parser parser = Parser.xmlParser();
    assertNotNull(parser);
    assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);

    // Edge case: creating a new XML parser with null TreeBuilder
    Parser parser = new Parser(null);
    assertNotNull(parser);
    assertNull(parser.getTreeBuilder());
  }
  
}