import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testInitialiseParse() throws IOException {
        String input = "<html><body></body></html>";
        String baseUri = "https://example.com";
        String expectedOutput = "<html><body></body></html>";

        Reader reader = new StringReader(input);
        xmlTreeBuilder.initialiseParse(reader, baseUri, new Parser(xmlTreeBuilder));

        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertEquals(expectedOutput, document.outerHtml());
    }

    @Test
    public void testParseReader() throws IOException {
        String input = "<html><body></body></html>";
        String baseUri = "https://example.com";
        String expectedOutput = "<html><body></body></html>";

        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertEquals(expectedOutput, document.outerHtml());
    }

    @Test
    public void testParseString() throws IOException {
        String input = "<html><body></body></html>";
        String baseUri = "https://example.com";
        String expectedOutput = "<html><body></body></html>";

        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expectedOutput, document.outerHtml());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.add("class", "container");
        startTag.attributes.add("id", "main");

        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("container", element.attr("class"));
        assertEquals("main", element.attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element divElement = xmlTreeBuilder.insert(startTag);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = xmlTreeBuilder.insert(commentToken);

        assertNotNull(comment);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        assertEquals("data", ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/html4/loose.dtd");

        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        assertEquals("html",
                ((DocumentType) childNodes.get(0)).name());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN",
                ((DocumentType) childNodes.get(0)).publicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd",
                ((DocumentType) childNodes.get(0)).systemIdentifier());
    }

    @Test
    public void testParseFragment() {
        String input = "<div><p>Hello, World!</p></div>";
        String baseUri = "https://example.com";
        String expectedOutput = "<div><p>Hello, World!</p></div>";

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, new Parser(xmlTreeBuilder));
        Document document = new Document("");

        for (Node node : nodes) {
            document.appendChild(node);
        }

        assertEquals(expectedOutput, document.outerHtml());
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<p>Hello, World!</p>";
        Element context = new Element("div");

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, "https://example.com", new Parser(xmlTreeBuilder));
        assertEquals(1, nodes.size());

        Element div = new Element("div");
        div.appendChild(nodes.get(0));

        assertEquals("<div><p>Hello, World!</p></div>", div.outerHtml());
    }

}