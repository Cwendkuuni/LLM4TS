
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
  @Test
  public void testParseInput() {
    String html = "<html><body>Hello, World!</body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(new HtmlTreeBuilder());
    Document doc = parser.parseInput(html, baseUri);
    assertNotNull(doc);
    assertEquals("Hello, World!", doc.body().text());
  }

  @Test
  public void testGetTreeBuilder() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    TreeBuilder treeBuilder = parser.getTreeBuilder();
    assertNotNull(treeBuilder);
    assertTrue(treeBuilder instanceof HtmlTreeBuilder);
  }

  @Test
  public void testSetTreeBuilder() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    TreeBuilder treeBuilder = new XmlTreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    assertEquals(treeBuilder, parser.getTreeBuilder());
  }

  @Test
  public void testIsTrackErrors() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    assertFalse(parser.isTrackErrors());
    parser.setTrackErrors(10);
    assertTrue(parser.isTrackErrors());
  }

  @Test
  public void testSetTrackErrors() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    parser.setTrackErrors(5);
    assertTrue(parser.isTrackErrors());
    assertEquals(5, parser.getErrors().size());
  }

  @Test
  public void testGetErrors() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    List<ParseError> errors = parser.getErrors();
    assertNotNull(errors);
    assertTrue(errors.isEmpty());
  }

  @Test
  public void testParse() {
    String html = "<html><body>Hello, World!</body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Hello, World!", doc.body().text());
  }

  @Test
  public void testParseFragment() {
    String fragmentHtml = "<div>Hello, World!</div>";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    assertEquals("Hello, World!", nodes.get(0).outerHtml());
  }

  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<div>Hello, World!</div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals("Hello, World!", doc.body().text());
  }

  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<div>Hello, World!</div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals("Hello, World!", doc.body().text());
  }

  @Test
  public void testHtmlParser() {
    Parser parser = Parser.htmlParser();
    assertNotNull(parser);
    assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
  }

  @Test
  public void testXmlParser() {
    Parser parser = Parser.xmlParser();
    assertNotNull(parser);
    assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
  }
}
