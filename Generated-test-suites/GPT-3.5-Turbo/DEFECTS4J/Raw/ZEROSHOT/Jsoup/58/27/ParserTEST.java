import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

   @Test
   public void testParseInput() {
       Parser parser = new Parser(new HtmlTreeBuilder());
       String html = "<html><body><h1>Hello, World!</h1></body></html>";
       String baseUri = "https://www.example.com";
       assertNotNull(parser.parseInput(html, baseUri));
   }

   @Test
   public void testGetTreeBuilder() {
       Parser parser = new Parser(new HtmlTreeBuilder());
       assertNotNull(parser.getTreeBuilder());
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
   }

   @Test
   public void testSetTrackErrors() {
       Parser parser = new Parser(new HtmlTreeBuilder());
       parser.setTrackErrors(10);
       assertTrue(parser.isTrackErrors());
   }

   @Test
   public void testGetErrors() {
       Parser parser = new Parser(new HtmlTreeBuilder());
       assertEquals(0, parser.getErrors().size());
   }

   @Test
   public void testSettings() {
       Parser parser = new Parser(new HtmlTreeBuilder());
       ParseSettings settings = parser.settings();
       assertNotNull(settings);
       parser.settings(new ParseSettings());
       assertNotEquals(settings, parser.settings());
   }

   @Test
   public void testParse() {
       String html = "<html><body><h1>Hello, World!</h1></body></html>";
       String baseUri = "https://www.example.com";
       assertNotNull(Parser.parse(html, baseUri));
   }

   @Test
   public void testParseFragment() {
       String fragmentHtml = "<h1>Hello, World!</h1>";
       Element context = new Element("div");
       String baseUri = "https://www.example.com";
       assertNotNull(Parser.parseFragment(fragmentHtml, context, baseUri));
   }

   @Test
   public void testParseXmlFragment() {
       String fragmentXml = "<h1>Hello, World!</h1>";
       String baseUri = "https://www.example.com";
       assertNotNull(Parser.parseXmlFragment(fragmentXml, baseUri));
   }

   @Test
   public void testParseBodyFragment() {
       String bodyHtml = "<h1>Hello, World!</h1>";
       String baseUri = "https://www.example.com";
       assertNotNull(Parser.parseBodyFragment(bodyHtml, baseUri));
   }

   @Test
   public void testUnescapeEntities() {
       String string = "&lt;hello&gt;";
       boolean inAttribute = false;
       assertEquals("<hello>", Parser.unescapeEntities(string, inAttribute));
   }

   @Test
   public void testParseBodyFragmentRelaxed() {
       String bodyHtml = "<h1>Hello, World!</h1>";
       String baseUri = "https://www.example.com";
       assertNotNull(Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri));
   }

   @Test
   public void testHtmlParser() {
       Parser parser = Parser.htmlParser();
       assertNotNull(parser);
       assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
   }

   @Test
   public void testXmlParser() {
       Parser parser = Parser.xmlParser();
       assertNotNull(parser);
       assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
   }
}