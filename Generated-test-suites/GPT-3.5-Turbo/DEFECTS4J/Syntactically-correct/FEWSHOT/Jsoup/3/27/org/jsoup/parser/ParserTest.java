import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com/";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals(baseUri, document.baseUri());

        Element h1Element = document.selectFirst("h1");
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com/";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);

        Element h1Element = document.selectFirst("h1");
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com/";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com/";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testParseWithNullHtml() {
        String baseUri = "https://www.example.com/";
        Document document = Parser.parse(null, baseUri);

        assertNull(document);
    }

    @Test
    public void testParseBodyFragmentWithNullHtml() {
        String baseUri = "https://www.example.com/";
        Document document = Parser.parseBodyFragment(null, baseUri);

        assertNull(document);
    }

    @Test
    public void testParseWithNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parse(html, null);

        assertNotNull(document);
        assertEquals("", document.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Parser.parseBodyFragment(bodyHtml, null);

        assertNotNull(document);
        assertEquals("", document.baseUri());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Node node = document.childNode(0);
        assertNotNull(node);
        assertEquals("#comment", node.nodeName());
        assertEquals(" This is a comment ", ((Comment) node).getData());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Node node = document.childNode(0);
        assertNotNull(node);
        assertEquals("#cdata-section", node.nodeName());
        assertEquals("This is CDATA", ((DataNode) node).getWholeData());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Node node = document.childNode(0);
        assertNotNull(node);
        assertEquals("#xml-declaration", node.nodeName());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", ((XmlDeclaration) node).getWholeData());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Element h1Element = document.selectFirst("h1");
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());

        document.body().html(""); // Remove the content of the body

        h1Element = document.selectFirst("h1");
        assertNull(h1Element);
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Element bodyElement = document.body();
        assertNotNull(bodyElement);

        Element h1Element = bodyElement.selectFirst("h1");
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());

        h1Element.remove();

        h1Element = bodyElement.selectFirst("h1");
        assertNull(h1Element);
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><head></head><body><h1>Hello <em>World</em></h1></body></html>";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Element h1Element = document.selectFirst("h1");
        assertNotNull(h1Element);

        TextNode textNode = (TextNode) h1Element.childNode(0);
        assertNotNull(textNode);
        assertEquals("Hello ", textNode.getWholeText());

        Element emElement = h1Element.selectFirst("em");
        assertNotNull(emElement);

        textNode = (TextNode) h1Element.childNode(1);
        assertNotNull(textNode);
        assertEquals("World", textNode.getWholeText());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"myDiv\" class=\"myClass\"></div>";
        Document document = Parser.parse(html, "https://www.example.com/");

        assertNotNull(document);

        Element divElement = document.selectFirst("div");
        assertNotNull(divElement);
        assertNotNull(divElement.attr("id"));
        assertNotNull(divElement.attr("class"));
        assertEquals("myDiv", divElement.attr("id"));
        assertEquals("myClass", divElement.attr("class"));
    }
}