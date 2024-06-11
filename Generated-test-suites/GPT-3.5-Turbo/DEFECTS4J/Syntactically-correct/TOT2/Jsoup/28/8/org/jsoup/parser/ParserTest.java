import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.HtmlTreeBuilder;
import java.util.List;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Test
    public void testParseInput() {
        parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><p>Hello, world!</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testStaticParse() {
        String html = "<div>Test Static Parse</div>";
        String baseUri = "https://www.test.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.test.com", doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Test Fragment</p>";
        Element context = new Element("div");
        String baseUri = "https://www.testfragment.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Body Fragment</p>";
        String baseUri = "https://www.testbodyfragment.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.testbodyfragment.com", doc.baseUri());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

}