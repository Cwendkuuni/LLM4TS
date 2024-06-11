
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
    
    private Parser parser;
    
    @BeforeEach
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput_WithHtmlAndBaseUri_ReturnsParsedDocument() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertEquals("Title", doc.select("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder_WithInitializedParser_ReturnsTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertEquals(TreeBuilder.class, treeBuilder.getClass());
    }
    
    @Test
    public void testSetTreeBuilder_WithNewTreeBuilder_ReturnsUpdatedParser() {
        TreeBuilder newTreeBuilder = new TreeBuilder();
        
        Parser updatedParser = parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, updatedParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors_WithDefaultParser_ReturnsFalse() {
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors_WithMaxErrors_ReturnsUpdatedParser() {
        int maxErrors = 10;
        
        Parser updatedParser = parser.setTrackErrors(maxErrors);
        
        assertTrue(updatedParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors_WithParsedInput_ReturnsEmptyList() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testParser_Parse_WithHtmlAndBaseUri_ReturnsParsedDocument() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("Title", doc.select("h1").text());
    }
    
    @Test
    public void testParseFragment_WithFragmentHtmlAndContextAndBaseUri_ReturnsListOfNodes() {
        String fragmentHtml = "<h1>Title</h1><p>Paragraph</p>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(2, nodes.size());
        assertEquals("Title", nodes.get(0).text());
        assertEquals("Paragraph", nodes.get(1).text());
    }
    
    @Test
    public void testParseBodyFragment_WithBodyHtmlAndBaseUri_ReturnsParsedDocumentWithBody() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("Title", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_WithBodyHtmlAndBaseUri_ReturnsParsedDocumentWithBody() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("Title", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }
    
    @Test
    public void testHtmlParser_WithoutArguments_ReturnsHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser_WithoutArguments_ReturnsXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}
