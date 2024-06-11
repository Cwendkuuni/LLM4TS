
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private static final String HTML = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    private static final String BASE_URI = "https://www.example.com";

    // Test the parseInput() method
    @Test
    public void testParseInput() {
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput(HTML, BASE_URI);

        assertNotNull("Parsed document should not be null", document);
        assertEquals("Title should match", "Test", document.title());
        assertEquals("Body should contain one h1 element", 1, document.getElementsByTag("h1").size());
        assertEquals("Text of h1 element should be 'Hello World!'", "Hello World!", document.getElementsByTag("h1").first().text());
    }

    // Test the getTreeBuilder() method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        assertTrue("Default tree builder should be HtmlTreeBuilder", parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    // Test the setTreeBuilder() method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertTrue("Tree builder should be XmlTreeBuilder", parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    // Test the isTrackErrors() method
    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertFalse("Error tracking should be disabled by default", parser.isTrackErrors());
    }

    // Test the setTrackErrors() method
    @Test
    public void testSetTrackErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(10);
        assertTrue("Error tracking should be enabled with a maximum of 10 errors", parser.isTrackErrors());
    }

    // Test the getErrors() method
    @Test
    public void testGetErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(5);
        Document document = parser.parseInput("<p>This is an invalid HTML</div>", BASE_URI);
        List<ParseError> errors = parser.getErrors();
        assertEquals("There should be 1 parse error", 1, errors.size());
        assertEquals("Error position should match", 25, errors.get(0).getPosition());
    }

    // Test the parse() method
    @Test
    public void testParse() {
        Document document = Parser.parse(HTML, BASE_URI);
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Title should match", "Test", document.title());
        assertEquals("Body should contain one h1 element", 1, document.getElementsByTag("h1").size());
        assertEquals("Text of h1 element should be 'Hello World!'", "Hello World!", document.getElementsByTag("h1").first().text());
    }

    // Test the parseFragment() method
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        context.append("<p>Paragraph 1</p>");
        List<Node> nodes = Parser.parseFragment("<p>Paragraph 2</p>", context, BASE_URI);
        assertEquals("There should be 2 nodes", 2, nodes.size());
        assertEquals("Text of first node should be 'Paragraph 1'", "Paragraph 1", nodes.get(0).toString());
        assertEquals("Text of second node should be 'Paragraph 2'", "Paragraph 2", nodes.get(1).toString());
    }

    // Test the parseXmlFragment() method
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<tag1>Text 1</tag1><tag2>Text 2</tag2>", BASE_URI);
        assertEquals("There should be 2 nodes", 2, nodes.size());
        assertEquals("Text of first node should be 'Text 1'", "Text 1", nodes.get(0).toString());
        assertEquals("Text of second node should be 'Text 2'", "Text 2", nodes.get(1).toString());
    }

    // Test the parseBodyFragment() method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>This is a heading</h2><p>This is a paragraph</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, BASE_URI);
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Body should contain one h2 element", 1, document.getElementsByTag("h2").size());
        assertEquals("Text of h2 element should be 'This is a heading'", "This is a heading", document.getElementsByTag("h2").first().text());
        assertEquals("Body should contain one p element", 1, document.getElementsByTag("p").size());
        assertEquals("Text of p element should be 'This is a paragraph'", "This is a paragraph", document.getElementsByTag("p").first().text());
    }

    // Test the unescapeEntities() method
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;This is a paragraph&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        assertEquals("Unescaped string should be '<p>This is a paragraph</p>'", "<p>This is a paragraph</p>", unescapedString);
    }

    // Test the deprecated parseBodyFragmentRelaxed() method
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<h2>This is a heading</h2><p>This is a paragraph</p>", BASE_URI);
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Body should contain one h2 element", 1, document.getElementsByTag("h2").size());
        assertEquals("Text of h2 element should be 'This is a heading'", "This is a heading", document.getElementsByTag("h2").first().text());
        assertEquals("Body should contain one p element", 1, document.getElementsByTag("p").size());
        assertEquals("Text of p element should be 'This is a paragraph'", "This is a paragraph", document.getElementsByTag("p").first().text());
    }

    // Test the htmlParser() method
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue("Tree builder should be HtmlTreeBuilder", parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    // Test the xmlParser() method
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue("Tree builder should be XmlTreeBuilder", parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}
