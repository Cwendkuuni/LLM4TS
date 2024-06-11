
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");

        assertNotNull(document);
        assertEquals("<html>", document.children().get(0).toString());
        assertEquals("<body>", document.children().get(0).children().get(0).toString());
        assertEquals("<h1>", document.children().get(0).children().get(0).children().get(0).toString());
        assertEquals("Hello World", document.children().get(0).children().get(0).children().get(0).text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
      
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
      
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        boolean trackErrors = parser.isTrackErrors();
      
        assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(5);
      
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
      
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings();
        parser.settings(settings);
      
        assertEquals(settings, parser.settings());}

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
      
        assertNotNull(document);
        assertEquals("<html>", document.children().get(0).toString());
        assertEquals("<body>", document.children().get(0).children().get(0).toString());
        assertEquals("<h1>", document.children().get(0).children().get(0).children().get(0).toString());
        assertEquals("Hello World", document.children().get(0).children().get(0).children().get(0).text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");  
        List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", context, "http://example.com");
      
        assertEquals(1, nodes.size());
        assertEquals("<p>", nodes.get(0).toString());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Hello World</p>", "http://example.com");

        assertEquals(1, nodes.size());
        assertEquals("<p>", nodes.get(0).toString());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Hello World</p>", "http://example.com");

        assertNotNull(document);
        assertEquals("<html>", document.toString());
        assertEquals("<head>", document.head().toString());
        assertEquals("<body>", document.body().toString());
        assertEquals("<p>", document.body().children().get(0).toString());
        assertEquals("Hello World", document.body().children().get(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;p&gt;Hello World&lt;/p&gt;", true);

        assertEquals("<p>Hello World</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", "http://example.com");

        assertNotNull(document);
        assertEquals("<html>", document.toString());
        assertEquals("<head>", document.head().toString());
        assertEquals("<body>", document.body().toString());
        assertEquals("<p>", document.body().children().get(0).toString());
        assertEquals("Hello World", document.body().children().get(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertNotNull(parser);
        assertEquals(TreeBuilder.HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
      
        assertNotNull(parser);
        assertEquals(TreeBuilder.XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}
