import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_WithValidInput_ShouldReturnDocumentObject() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }
    
    @Test
    public void testParseInput_WithInvalidInput_ShouldReturnNull() {
        String html = "<html><body><h1>Hello World</h2></body></html>"; // Invalid tag closing
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNull(doc);
    }
    
    @Test
    public void testGetTreeBuilder_ShouldReturnTreeBuilderInstance() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder_ShouldUpdateTreeBuilderInstance() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder2(); // Custom TreeBuilder
        
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors_WhenTrackErrorsEnabled_ShouldReturnTrue() {
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testIsTrackErrors_WhenTrackErrorsDisabled_ShouldReturnFalse() {
        parser.setTrackErrors(0);
        
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors_ShouldUpdateMaxErrors() {
        int newMaxErrors = 5;
        
        parser.setTrackErrors(newMaxErrors);
        
        assertEquals(newMaxErrors, parser.maxErrors);
    }
    
    @Test
    public void testGetErrors_WithNoParseErrors_ShouldReturnEmptyList() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings_WithValidSettings_ShouldUpdateSettings() {
        ParseSettings settings = new ParseSettings(true, false); // Custom settings
        
        parser.settings(settings);
        
        assertEquals(settings, parser.settings);
    }
    
    @Test
    public void testSettings_ShouldReturnSettings() {
        ParseSettings settings = parser.settings();
        
        assertNotNull(settings);
    }
    
    @Test
    public void testParse_WithValidInput_ShouldReturnDocumentObject() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }
    
    @Test
    public void testParseFragment_WithValidInput_ShouldReturnListOfNodes() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("Paragraph 1", nodes.get(0).text());
        assertEquals("Paragraph 2", nodes.get(1).text());
    }
    
    @Test
    public void testParseXmlFragment_WithValidInput_ShouldReturnListOfNodes() {
        String fragmentXml = "<root><item id=\"1\">Item 1</item><item id=\"2\">Item 2</item></root>";
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("Item 1", nodes.get(0).text());
        assertEquals("Item 2", nodes.get(1).text());
    }
    
    @Test
    public void testParseBodyFragment_WithValidInput_ShouldReturnDocumentObject() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(2, doc.select("p").size());
        assertEquals("Paragraph 1", doc.select("p").get(0).text());
        assertEquals("Paragraph 2", doc.select("p").get(1).text());
    }
    
    @Test
    public void testUnescapeEntities_WithEscapedString_ShouldReturnUnescapedString() {
        String escapedString = "&lt;div&gt;Hello World&lt;/div&gt;";
        
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        
        assertEquals("<div>Hello World</div>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_WithValidInput_ShouldReturnDocumentObject() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }
    
    @Test
    public void testHtmlParser_ShouldReturnParserWithHtmlTreeBuilder() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser_ShouldReturnParserWithXmlTreeBuilder() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}