import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.util.List;

public class ParserTest {
  
  private Parser parser;
  
  @Before
  public void setUp() {
    parser = new Parser(new HtmlTreeBuilder());
  }
  
  @Test
  public void testParseInput() {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document result = parser.parseInput(html, baseUri);
    
    assertNotNull(result);
    assertEquals("https://www.example.com", result.baseUri());
    assertTrue(result.html().contains("<h1>Hello World!</h1>"));
  }
  
  @Test
  public void testGetTreeBuilder() {
    TreeBuilder result = parser.getTreeBuilder();
    
    assertNotNull(result);
  }
  
  @Test
  public void testSetTreeBuilder() {
    TreeBuilder treeBuilder = new XmlTreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    
    assertEquals(treeBuilder, parser.getTreeBuilder());
  }
  
  @Test
  public void testIsTrackErrors() {
    boolean result = parser.isTrackErrors();
    
    assertFalse(result);
  }
  
  @Test
  public void testSetTrackErrors() {
    parser.setTrackErrors(3);
    
    assertTrue(parser.isTrackErrors());
  }
  
  @Test
  public void testGetErrors() {
    List<ParseError> result = parser.getErrors();
    
    assertNotNull(result);
    assertEquals(0, result.size());
  }
  
  @Test
  public void testParse() {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document result = Parser.parse(html, baseUri);
    
    assertNotNull(result);
    assertEquals("https://www.example.com", result.baseUri());
    assertTrue(result.html().contains("<h1>Hello World!</h1>"));
  }
  
  @Test
  public void testParseFragment() {
    String fragmentHtml = "<p>This is a paragraph.</p>";
    Element context = new Element("div");
    String baseUri = "https://www.example.com";
    List<Node> result = Parser.parseFragment(fragmentHtml, context, baseUri);
    
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.get(0) instanceof Element);
    assertEquals("p", ((Element) result.get(0)).tagName());
    assertEquals("This is a paragraph.", ((Element) result.get(0)).text());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<p>This is a paragraph.</p>";
    String baseUri = "https://www.example.com";
    Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(result);
    assertEquals("https://www.example.com", result.baseUri());
    assertTrue(result.body().html().contains("<p>This is a paragraph.</p>"));
  }
  
  @Test
  public void testHtmlParser() {
    Parser result = Parser.htmlParser();
    
    assertNotNull(result);
    assertTrue(result.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
  
  @Test
  public void testXmlParser() {
    Parser result = Parser.xmlParser();
    
    assertNotNull(result);
    assertTrue(result.getTreeBuilder() instanceof XmlTreeBuilder);
  }
}