import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
  
  private Parser parser;
  
  @Before
  public void setUp() {
    parser = new Parser(new HtmlTreeBuilder());
  }
  
  @Test
  public void testParseInput() {
    // Test with valid HTML and non-null baseUri
    String html1 = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri1 = "http://example.com";
    Document doc1 = parser.parseInput(html1, baseUri1);
    assertNotNull(doc1);
    assertEquals("Hello World", doc1.select("h1").text());
    
    // Test with invalid HTML and null baseUri
    String html2 = "<html><body><h1>Hello World</h1></body>";
    String baseUri2 = null;
    Document doc2 = parser.parseInput(html2, baseUri2);
    assertNotNull(doc2);
    assertEquals("", doc2.select("h1").text());
  }
  
  @Test
  public void testGetTreeBuilder() {
    TreeBuilder builder = parser.getTreeBuilder();
    assertNotNull(builder);
  }
  
  @Test
  public void testSetTreeBuilder() {
    TreeBuilder builder = new HtmlTreeBuilder();
    Parser newParser = parser.setTreeBuilder(builder);
    assertEquals(builder, newParser.getTreeBuilder());
  }
  
  @Test
  public void testIsTrackErrors() {
    boolean trackErrors = parser.isTrackErrors();
    assertFalse(trackErrors);
  }
  
  @Test
  public void testSetTrackErrors() {
    Parser newParser = parser.setTrackErrors(3);
    assertTrue(newParser.isTrackErrors());
  }
  
  @Test
  public void testGetErrors() {
    List<ParseError> errors = parser.getErrors();
    assertNotNull(errors);
    assertEquals(0, errors.size());
  }
  
  @Test
  public void testParse() {
    String html = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNotNull(doc);
    assertEquals("Hello World", doc.select("h1").text());
  }
  
  @Test
  public void testParseFragment() {
    String fragmentHtml = "<h2>Subtitle</h2>";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    assertNotNull(nodes);
    assertEquals("Subtitle", nodes.get(0).toString());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<p>This is a paragraph.</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals("This is a paragraph.", doc.select("p").text());
  }
  
  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<p>This is a paragraph.</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    assertNotNull(doc);
    assertEquals("This is a paragraph.", doc.select("p").text());
  }
  
  @Test
  public void testHtmlParser() {
    Parser htmlParser = Parser.htmlParser();
    assertNotNull(htmlParser);
    assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
  
  @Test
  public void testXmlParser() {
    Parser xmlParser = Parser.xmlParser();
    assertNotNull(xmlParser);
    assertFalse(xmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
}