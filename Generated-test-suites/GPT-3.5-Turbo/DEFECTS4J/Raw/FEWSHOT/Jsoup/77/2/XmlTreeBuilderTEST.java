import org.jsoup.Jsoup;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<root><child></child></root>");
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><child></child></root>";
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root><child></child></root>");
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag token = new Token.StartTag("div");
        token.attributes.add(new Token.Attribute("id", "example"));
        Element element = xmlTreeBuilder.insert(token);
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("example", element.attr("id"));
        assertTrue(element.childNodes().isEmpty());
        assertFalse(element.tag().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);
        Token.EndTag token = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(token);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment token = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(token);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character token = new Token.Character("This is character data");
        xmlTreeBuilder.insert(token);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("This is character data", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype token = new Token.Doctype("html");
        token.setPubSysKey("PUBLIC");
        token.setPublicIdentifier("-//W3C//DTD HTML 4.01//EN");
        token.setSystemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.insert(token);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) childNodes.get(0);
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.insertNode(new TextNode("This is a text node"));
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("This is a text node", textNode.getWholeText());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child></child>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element element = (Element) childNodes.get(0);
        assertEquals("child", element.tagName());
    }
}