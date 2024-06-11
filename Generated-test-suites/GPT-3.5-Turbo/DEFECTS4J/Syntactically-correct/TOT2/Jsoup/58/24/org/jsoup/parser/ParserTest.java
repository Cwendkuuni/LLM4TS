import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        ParseSettings settings = new ParseSettings(true, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        String fragmentHtml = "<p>Hello, World!</p>";
        String baseUri = "http://www.example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<note><to>Tove</to><from>Jani</from></note>";
        String baseUri = "http://www.example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        assertEquals("<p>Hello, World!</p>", unescapedString);
    }
}