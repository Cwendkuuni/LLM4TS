import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
  
  private Parser parser;
  
  @Before
  public void setUp() {
    parser = new Parser(new HtmlTreeBuilder());
  }
  
  @Test
  public void testParseInput() {
    String html = "<html><head><title>Test Page</title></head><body><p>Hello, world!</p></body></html>";
    String baseUri = "http://www.example.com";
    Document doc = parser.parseInput(html, baseUri);
    
    assertNotNull(doc);
    assertEquals("Test Page", doc.title());
    assertEquals("<html> <head> <title> Test Page </title> </head> <body> <p> Hello, world! </p> </body> </html>",
                doc.html());
  }
  
  @Test
  public void testGetTreeBuilder() {
    TreeBuilder treeBuilder = parser.getTreeBuilder();
    
    assertNotNull(treeBuilder);
    assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
  }
  
  @Test
  public void testSetTreeBuilder() {
    TreeBuilder treeBuilder = new XmlTreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    
    assertEquals(treeBuilder, parser.getTreeBuilder());
  }
  
  @Test
  public void testIsTrackErrors() {
    assertFalse(parser.isTrackErrors());
  }
  
  @Test
  public void testSetTrackErrors() {
    parser.setTrackErrors(10);
    
    assertTrue(parser.isTrackErrors());
  }
  
  @Test
  public void testGetErrors() {
    List<ParseError> errors = parser.getErrors();
    
    assertNotNull(errors);
    assertEquals(0, errors.size());
  }
  
  @Test
  public void testParse() {
    String html = "<p>Hello, world!</p>";
    String baseUri = "http://www.example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertEquals("<p> Hello, world! </p>", doc.html());
  }
  
  @Test
  public void testParseFragment() {
    String fragmentHtml = "<div><p>Fragment</p></div>";
    Element context = new Element("div");
    String baseUri = "http://www.example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    assertEquals("<p> Fragment </p>", nodes.get(0).outerHtml());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<p>Hello, world!</p>";
    String baseUri = "http://www.example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertEquals("<body> <p> Hello, world! </p> </body>", doc.body().outerHtml());
  }
  
  @Test
  public void testHtmlParser() {
    Parser htmlParser = Parser.htmlParser();
    
    assertNotNull(htmlParser);
    assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
  }
  
  @Test
  public void testXmlParser() {
    Parser xmlParser = Parser.xmlParser();
    
    assertNotNull(xmlParser);
    assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
  }
}