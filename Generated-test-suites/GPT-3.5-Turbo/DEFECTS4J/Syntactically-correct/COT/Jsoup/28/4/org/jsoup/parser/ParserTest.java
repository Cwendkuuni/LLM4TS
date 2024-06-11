import org.junit.Test;
import org.junit.Assert;

public class ParserTest {
  
  @Test
  public void testParseInput() {
    // Test basic functionality
    String html = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(new HtmlTreeBuilder());
    Document doc = parser.parseInput(html, baseUri);
    Assert.assertNotNull(doc);
    Assert.assertEquals(1, doc.body().getElementsByTag("h1").size());
  }
  
  @Test
  public void testGetTreeBuilder() {
    // Test basic functionality
    Parser parser = new Parser(new HtmlTreeBuilder());
    Assert.assertNotNull(parser.getTreeBuilder());
  }
  
  @Test
  public void testSetTreeBuilder() {
    // Test basic functionality
    Parser parser = new Parser(new HtmlTreeBuilder());
    TreeBuilder treeBuilder = new XmlTreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
  }
  
  @Test
  public void testIsTrackErrors() {
    // Test basic functionality
    Parser parser = new Parser(new HtmlTreeBuilder());
    Assert.assertFalse(parser.isTrackErrors());
  }
  
  @Test
  public void testSetTrackErrors() {
    // Test basic functionality
    Parser parser = new Parser(new HtmlTreeBuilder());
    parser.setTrackErrors(5);
    Assert.assertTrue(parser.isTrackErrors());
    Assert.assertEquals(5, parser.getTrackErrors());
  }
  
  @Test
  public void testGetErrors() {
    // Test basic functionality
    Parser parser = new Parser(new HtmlTreeBuilder());
    List<ParseError> errors = parser.getErrors();
    Assert.assertNotNull(errors);
    Assert.assertEquals(0, errors.size());
  }
  
  @Test
  public void testParse() {
    // Test basic functionality
    String html = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    Assert.assertNotNull(doc);
    Assert.assertEquals(1, doc.body().getElementsByTag("h1").size());
  }
  
  @Test
  public void testParseFragment() {
    // Test basic functionality
    String fragmentHtml = "<h1>Hello World</h1>";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
    Assert.assertNotNull(nodes);
    Assert.assertEquals(1, nodes.size());
    Assert.assertEquals("h1", nodes.get(0).nodeName());
  }
  
  @Test
  public void testParseBodyFragment() {
    // Test basic functionality
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    Assert.assertNotNull(doc);
    Assert.assertEquals(1, doc.body().getElementsByTag("h1").size());
  }
  
  @Test
  public void testParseBodyFragmentRelaxed() {
    // Test basic functionality
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    Assert.assertNotNull(doc);
    Assert.assertEquals(1, doc.body().getElementsByTag("h1").size());
  }
  
  @Test
  public void testHtmlParser() {
    // Test basic functionality
    Parser parser = Parser.htmlParser();
    Assert.assertNotNull(parser);
    Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
  }
  
  @Test
  public void testXmlParser() {
    // Test basic functionality
    Parser parser = Parser.xmlParser();
    Assert.assertNotNull(parser);
    Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
  }
}