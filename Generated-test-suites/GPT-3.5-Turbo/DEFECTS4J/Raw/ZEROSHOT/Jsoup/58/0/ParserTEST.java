import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.*;

public class ParserTest {
  
  @Test
  public void testParseInput() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document document = parser.parseInput(html, baseUri);
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Hello World!", document.select("h1").text());
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
    parser.setTrackErrors(10);
    assertTrue(parser.isTrackErrors());
    parser.setTrackErrors(0);
    assertFalse(parser.isTrackErrors());
  }
  
  @Test
  public void testGetErrors() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    List<ParseError> errors = parser.getErrors();
    assertNotNull(errors);
    assertEquals(0, errors.size());
    parser.setTrackErrors(10);
    assertEquals(10, parser.getErrors().size());
  }
  
  @Test
  public void testSettings() {
    Parser parser = new Parser(new HtmlTreeBuilder());
    ParseSettings settings = new ParseSettings(false);
    parser.settings(settings);
    assertEquals(settings, parser.settings());
  }
  
  @Test
  public void testParse() {
    String html = "<h1>Hello World!</h1>";
    String baseUri = "https://www.example.com";
    Document document = Parser.parse(html, baseUri);
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Hello World!", document.select("h1").text());
  }
  
  @Test
  public void testParseFragment() {
    String fragmentHtml = "<div><p>Test</p></div>";
    Element context = new Element("div");
    String baseUri = "https://www.example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    assertEquals("Test", nodes.get(0).select("p").first().text());
  }
  
  @Test
  public void testParseXmlFragment() {
    String fragmentXml = "<root><child>Test</child></root>";
    String baseUri = "https://www.example.com";
    List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    assertEquals("Test", nodes.get(0).select("child").first().text());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<p>Test</p>";
    String baseUri = "https://www.example.com";
    Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Test", document.select("p").text());
  }
  
  @Test
  public void testUnescapeEntities() {
    String string = "&lt;p&gt;Test&lt;/p&gt;";
    boolean inAttribute = false;
    String unescapedString = Parser.unescapeEntities(string, inAttribute);
    assertEquals("<p>Test</p>", unescapedString);
  }
  
  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<p>Test</p>";
    String baseUri = "https://www.example.com";
    Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Test", document.select("p").text());
  }
  
  @Test
  public void testHtmlParser() {
    Parser parser = Parser.htmlParser();
    assertNotNull(parser.getTreeBuilder());
    assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
  
  @Test
  public void testXmlParser() {
    Parser parser = Parser.xmlParser();
    assertNotNull(parser.getTreeBuilder());
    assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
  }

}