import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.Before;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(XmlTreeBuilder.class, newParser.getTreeBuilder().getClass());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(10);
        assertTrue(newParser.isTrackErrors());
        
        newParser = parser.setTrackErrors(0);
        assertFalse(newParser.isTrackErrors());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body>Hello, World!</body></html>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("body").text());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        Parser newParser = parser.settings(settings);
        assertEquals(settings, newParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body>Hello, World!</body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("body").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><span>Hello, World!</span></div>";
        Element context = new Element("body");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals(1, nodes.get(0).childNodeSize());
        assertEquals("span", nodes.get(0).childNode(0).nodeName());
        assertEquals("Hello, World!", nodes.get(0).childNode(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Hello, World!</child></root>";
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals(1, nodes.get(0).childNodeSize());
        assertEquals("child", nodes.get(0).childNode(0).nodeName());
        assertEquals("Hello, World!", nodes.get(0).childNode(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<span>Hello, World!</span>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("span").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello, &lt;World&gt;!";
        String unescapedString = Parser.unescapeEntities(escapedString, false);

        assertEquals("Hello, <World>!", unescapedString);
    }
}