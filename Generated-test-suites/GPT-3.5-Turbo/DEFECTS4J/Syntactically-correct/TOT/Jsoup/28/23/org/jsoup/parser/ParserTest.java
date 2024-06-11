import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

  private Parser parser;

  @Before
  public void setUp() {
    parser = new Parser(new HtmlTreeBuilder());
  }

  @Test
  public void testParseInput() {
    String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
    String baseUri = "http://example.com";
    
    Document doc = parser.parseInput(html, baseUri);
    
    Element body = doc.body();
    assertEquals("Title", body.select("h1").text());
    assertEquals("Paragraph", body.select("p").text());
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testGetAndSetTreeBuilder() {
    TreeBuilder treeBuilder = new HtmlTreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    
    assertEquals(treeBuilder, parser.getTreeBuilder());
  }
  
  @Test
  public void testIsTrackErrors() {
    assertFalse(parser.isTrackErrors());
    
    parser.setTrackErrors(10);
    assertTrue(parser.isTrackErrors());
  }
  
  @Test
  public void testGetErrors() {
    List<ParseError> errors = parser.getErrors();
    assertNull(errors);
    
    parser.setTrackErrors(5);
    errors = parser.getErrors();
    assertEquals(0, errors.size());
  }
  
  @Test
  public void testStaticParse() {
    String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
    String baseUri = "http://example.com";
    
    Document doc = Parser.parse(html, baseUri);
    
    Element body = doc.body();
    assertEquals("Title", body.select("h1").text());
    assertEquals("Paragraph", body.select("p").text());
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testStaticParseFragment() {
    String fragmentHtml = "<h1>Title</h1><p>Paragraph</p>";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    
    assertEquals(2, nodes.size());
    assertEquals("Title", nodes.get(0).text());
    assertEquals("Paragraph", nodes.get(1).text());
    assertEquals("http://example.com", context.baseUri());
  }
  
  @Test
  public void testStaticParseBodyFragment() {
    String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
    String baseUri = "http://example.com";
    
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    Element body = doc.body();
    assertEquals("Title", body.select("h1").text());
    assertEquals("Paragraph", body.select("p").text());
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
    String baseUri = "http://example.com";
    
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    
    Element body = doc.body();
    assertEquals("Title", body.select("h1").text());
    assertEquals("Paragraph", body.select("p").text());
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testHtmlParser() {
    Parser parser = Parser.htmlParser();
    
    assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
  
  @Test
  public void testXmlParser() {
    Parser parser = Parser.xmlParser();
    
    assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
  }

}