import org.jsoup.nodes.Document;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        Document document = parser.parseInput("<html></html>", "");
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        assertNull(parser.getErrors());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testStaticParse() {
        Document document = Parser.parse("<html></html>", "");
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
    }

    @Test
    public void testStaticParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div></div>", null, "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

    @Test
    public void testStaticParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<xml></xml>", "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

    @Test
    public void testStaticParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<div></div>", "");
        assertNotNull(document);
        assertEquals(1, document.body().children().size());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;div&gt;", false);
        assertEquals("<div>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<html></html>", "");
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }

}