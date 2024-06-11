import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html></html>", "http://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(0, parser.getErrors().size());
    }
    
    @Test
    public void testParseStatic() {
        Document doc = Parser.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testParseFragmentStatic() {
        Node node = new Node("test");
        List<Node> nodeList = Parser.parseFragment("<div></div>", (Element) node, "http://example.com");
        assertEquals(1, nodeList.size());
    }
    
    @Test
    public void testParseBodyFragmentStatic() {
        Document doc = Parser.parseBodyFragment("<div></div>", "http://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}