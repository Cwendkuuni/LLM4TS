import org.jsoup.parser.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    private String html = "<html><body><p>Hello, World!</p></body></html>";
    private String baseUri = "https://www.example.com";
    
    @Test
    public void testParseInput() {
        parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testGetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testSetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
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
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Example</p>", context, baseUri);
        assertEquals(1, nodes.size());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Example</p>", baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Example</p>", baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testHtmlParser() {
        parser = Parser.htmlParser();
        assertNotNull(parser);
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        parser = Parser.xmlParser();
        assertNotNull(parser);
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}