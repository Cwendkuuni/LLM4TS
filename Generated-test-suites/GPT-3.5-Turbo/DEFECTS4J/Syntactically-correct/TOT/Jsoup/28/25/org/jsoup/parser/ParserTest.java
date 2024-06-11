
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
   private Parser parser;
   
   @Before
   public void setUp() {
      parser = new Parser(new HtmlTreeBuilder());
   }
   
   // Testing parseInput method
   
   @Test
   public void testParseInput_typicalCase() {
      String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
      String baseUri = "https://example.com";
      
      Document doc = parser.parseInput(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>", doc.html());
   }
   
   @Test
   public void testParseInput_maxErrorsEnabled() {
      String html = "<html><body><h1>Title</h1><p>Paragraph";
      String baseUri = "https://example.com";
      
      parser.setTrackErrors(3);
      Document doc = parser.parseInput(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>", doc.html());
      assertEquals(1, parser.getErrors().size());
   }
   
   // Testing getTreeBuilder and setTreeBuilder methods
   
   @Test
   public void testGetTreeBuilder_typicalCase() {
      TreeBuilder treeBuilder = parser.getTreeBuilder();
      
      assertNotNull(treeBuilder);
      assertTrue(treeBuilder instanceof HtmlTreeBuilder);
   }
   
   @Test
   public void testSetTreeBuilder_typicalCase() {
      TreeBuilder treeBuilder = new XmlTreeBuilder();
      parser.setTreeBuilder(treeBuilder);
      
      assertEquals(treeBuilder, parser.getTreeBuilder());
   }
   
   // Testing isTrackErrors and setTrackErrors methods
   
   @Test
   public void testIsTrackErrors_typicalCase() {
      assertFalse(parser.isTrackErrors());
   }
   
   @Test
   public void testSetTrackErrors_typicalCase() {
      int maxErrors = 5;
      parser.setTrackErrors(maxErrors);
      
      assertTrue(parser.isTrackErrors());
      assertEquals(maxErrors, parser.getErrors().size());
   }
   
   // Testing getErrors method
   
   @Test
   public void testGetErrors_typicalCase() {
      assertEquals(0, parser.getErrors().size());
   }
   
   // Testing static parse methods
   
   @Test
   public void testParse_typicalCase() {
      String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
      String baseUri = "https://example.com";
      
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>", doc.html());
   }
   
   @Test
   public void testParseFragment_typicalCase() {
      String fragmentHtml = "<h1>Title</h1><p>Paragraph</p>";
      Element context = new Element("div");
      String baseUri = "https://example.com";
      
      List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
      
      assertNotNull(nodeList);
      assertEquals(2, nodeList.size());
   }
   
   @Test
   public void testParseBodyFragment_typicalCase() {
      String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
      String baseUri = "https://example.com";
      
      Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
      
      assertNotNull(doc);
      assertEquals("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>", doc.html());
   }
   
   // Testing parseBodyFragmentRelaxed method
   
   @Test
   public void testParseBodyFragmentRelaxed_typicalCase() {
      String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
      String baseUri = "https://example.com";
      
      Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
      
      assertNotNull(doc);
      assertEquals("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>", doc.html());
   }

   // Testing builder methods
   
   @Test
   public void testHtmlParser_typicalCase() {
      Parser htmlParser = Parser.htmlParser();
      
      assertNotNull(htmlParser);
      assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
   }
   
   @Test
   public void testXmlParser_typicalCase() {
      Parser xmlParser = Parser.xmlParser();
      
      assertNotNull(xmlParser);
      assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
   }
}
