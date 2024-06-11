
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.assertEquals;
 
public class ParserTest {
    
    // Test parseInput(String html, String baseUri) method
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Document document = parser.parseInput(html, baseUri);
        
        assertEquals("<h1>Test</h1>", document.select("h1").toString());
    }

    // Test getTreeBuilder() method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    // Test setTreeBuilder(TreeBuilder treeBuilder) method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTreeBuilder(new XmlTreeBuilder());
        
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    // Test isTrackErrors() method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        boolean trackErrors = parser.isTrackErrors();
        
        assertEquals(false, trackErrors);
    }

    // Test setTrackErrors(int maxErrors) method
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(10);
        
        assertEquals(true, parser.isTrackErrors());
    }

    // Test getErrors() method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        List<ParseError> errors = parser.getErrors();
        
        assertEquals(0, errors.size());
    }

    // Test settings(ParseSettings settings) method
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(true, false);
        
        parser.settings(settings);
        
        assertEquals(settings, parser.settings());
    }

    // Test parse(String html, String baseUri) method
    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertEquals("<h1>Test</h1>", document.select("h1").toString());
    }

    // Test parseFragment(String fragmentHtml, Element context, String baseUri) method
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Test</h1>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals("<h1>Test</h1>", nodes.toString());
    }

    // Test parseXmlFragment(String fragmentXml, String baseUri) method
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Test</h1>";
        String baseUri = "https://example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertEquals("<h1>Test</h1>", nodes.toString());
    }

    // Test parseBodyFragment(String bodyHtml, String baseUri) method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("<h1>Test</h1>", document.select("h1").toString());
    }

    // Test unescapeEntities(String string, boolean inAttribute) method
    @Test
    public void testUnescapeEntities() {
        String html = "&lt;h1&gt;Test&lt;/h1&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(html, inAttribute);
        
        assertEquals("<h1>Test</h1>", unescapedString);
    }

    // Test parseBodyFragmentRelaxed(String bodyHtml, String baseUri) method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("<h1>Test</h1>", document.select("h1").toString());
    }

    // Test htmlParser() method
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    // Test xmlParser() method
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
}
