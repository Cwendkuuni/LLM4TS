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
    public void setup() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput_normalInput_returnDocument() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseInput_emptyInput_returnDocument() {
        String html = "";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder_defaultTreeBuilder_returnTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof TreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder_validTreeBuilder_returnThis() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser modifiedParser = parser.setTreeBuilder(treeBuilder);
        
        assertNotNull(modifiedParser);
        assertEquals(treeBuilder, modifiedParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors_enabledTracking_returnTrue() {
        parser.setTrackErrors(10);
        boolean trackErrors = parser.isTrackErrors();
        
        assertTrue(trackErrors);
    }
    
    @Test
    public void testIsTrackErrors_disabledTracking_returnFalse() {
        parser.setTrackErrors(0);
        boolean trackErrors = parser.isTrackErrors();
        
        assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors_validMaxErrors_returnThis() {
        Parser modifiedParser = parser.setTrackErrors(5);
        
        assertNotNull(modifiedParser);
        assertTrue(modifiedParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors_errorsPresent_returnListErrors() {
        parser.setTrackErrors(3);
        parser.parseInput("<html><body>Invalid HTML", "https://example.com");
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertEquals(3, errors.size());
    }
    
    @Test
    public void testParse_validHtml_returnDocument() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseFragment_validHtml_returnListOfNodes() {
        String fragmentHtml = "<div><p>Test</p><p>Test2</p></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(3, nodeList.size());
    }
    
    @Test
    public void testParseBodyFragment_validHtml_returnDocument() {
        String bodyHtml = "<p>Test</p><p>Test2</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Test</p>\n  <p>Test2</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_validHtml_returnDocument() {
        String bodyHtml = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testHtmlParser_validParser_returnParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser_validParser_returnParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}