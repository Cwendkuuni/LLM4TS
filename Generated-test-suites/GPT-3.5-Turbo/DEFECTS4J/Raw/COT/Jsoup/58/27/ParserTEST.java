import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";
        parser = new Parser(new HtmlTreeBuilder());
        
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Title", document.select("h1").text());
        assertEquals("Paragraph", document.select("p").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
    
    @Test
    public void testIsTrackErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(true);
        
        parser.settings(settings);
        ParseSettings returnedSettings = parser.settings();
        
        assertNotNull(returnedSettings);
        assertTrue(returnedSettings.isTrackErrors());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Title", document.select("h1").text());
        assertEquals("Paragraph", document.select("p").text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><span>Fragment</span></div>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Fragment", ((Element) nodes.get(0)).select("span").text());
    }
    
    @Test
    public void testParseFragment_withErrorList() {
        String fragmentHtml = "<p>This is some invalid HTML";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "https://www.example.com";
        List<ParseError> errorList = new ArrayList<>();
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri, errorList);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("This is some invalid HTML", nodes.get(0).toString());
        assertEquals(1, errorList.size());
        assertEquals(ErrorType.UnescapedCharacterError, errorList.get(0).getErrorType());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Fragment</child></root>";
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Fragment", ((Element) nodes.get(0)).select("child").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("Title", document.select("h1").text());
        assertEquals("Paragraph", document.select("p").text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String html = "&lt;div&gt;Escaped&lt;/div&gt;";
        boolean inAttribute = false;
        
        String unescapedHtml = Parser.unescapeEntities(html, inAttribute);
        
        assertNotNull(unescapedHtml);
        assertEquals("<div>Escaped</div>", unescapedHtml);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("Title", document.select("h1").text());
        assertEquals("Paragraph", document.select("p").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}