import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class ParserTest {
  
  private Parser parser;
  private TreeBuilder treeBuilder;
  
  @Before
  public void setUp() {
    treeBuilder = new HtmlTreeBuilder();
    parser = new Parser(treeBuilder);
  }
  
  @Test
  public void testParseInput() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "https://www.example.com";
    
    Document document = parser.parseInput(html, baseUri);
    
    // Assertions
    assertNotNull(document);
    assertEquals("Test", document.title());
    assertEquals("Hello World", document.body().getElementsByTag("h1").text());
  }
  
  @Test
  public void testGetTreeBuilder() {
    TreeBuilder result = parser.getTreeBuilder();
    assertEquals(treeBuilder, result);
  }
  
  @Test
  public void testSetTreeBuilder() {
    TreeBuilder newTreeBuilder = new XmlTreeBuilder();
    parser.setTreeBuilder(newTreeBuilder);
    
    TreeBuilder result = parser.getTreeBuilder();
    assertEquals(newTreeBuilder, result);
  }
  
  @Test
  public void testIsTrackErrors() {
    boolean result = parser.isTrackErrors();
    assertFalse(result);
  }
  
  @Test
  public void testSetTrackErrors() {
    int maxErrors = 5;
    parser.setTrackErrors(maxErrors);
    
    boolean result = parser.isTrackErrors();
    assertTrue(result);
  }
  
  @Test
  public void testGetErrors() {
    List<ParseError> result = parser.getErrors();
    assertNotNull(result);
  }
  
  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "https://www.example.com";
    
    Document document = Parser.parse(html, baseUri);
    
    // Assertions
    assertNotNull(document);
    assertEquals("Test", document.title());
    assertEquals("Hello World", document.body().getElementsByTag("h1").text());
  }
  
  @Test
  public void testParseFragment() {
    String fragmentHtml = "<h1>Hello World</h1>";
    Element context = new Element("div");
    String baseUri = "https://www.example.com";
    
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    
    // Assertions
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    assertTrue(nodes.get(0) instanceof Element);
    assertEquals("h1", ((Element) nodes.get(0)).tagName());
    assertEquals("Hello World", ((Element) nodes.get(0)).text());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "https://www.example.com";
    
    Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    // Assertions
    assertNotNull(document);
    assertEquals("Hello World", document.body().getElementsByTag("h1").first().text());
  }
  
  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "https://www.example.com";
    
    Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    
    // Assertions
    assertNotNull(document);
    assertEquals("Hello World", document.body().getElementsByTag("h1").first().text());
  }
  
  @Test
  public void testHtmlParser() {
    Parser result = Parser.htmlParser();
    
    // Assertions
    assertNotNull(result);
    assertTrue(result.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
  
  @Test
  public void testXmlParser() {
    Parser result = Parser.xmlParser();
    
    // Assertions
    assertNotNull(result);
    assertTrue(result.getTreeBuilder() instanceof XmlTreeBuilder);
  }
}