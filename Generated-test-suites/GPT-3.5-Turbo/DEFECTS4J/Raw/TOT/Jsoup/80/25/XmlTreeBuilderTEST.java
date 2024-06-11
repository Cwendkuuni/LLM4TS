import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseReader() {
        String xml = "<root><element1>value1</element1><element2>value2</element2></root>";
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
        assertEquals("root", ((Element) document.childNode(0)).tagName());
        assertEquals(2, document.childNode(0).childNodeSize());
        assertTrue(document.childNode(0).childNode(0) instanceof Element);
        assertTrue(document.childNode(0).childNode(1) instanceof Element);
        assertEquals("element1", ((Element) document.childNode(0).childNode(0)).tagName());
        assertEquals("value1", ((Element) document.childNode(0).childNode(0)).text());
        assertEquals("element2", ((Element) document.childNode(0).childNode(1)).tagName());
        assertEquals("value2", ((Element) document.childNode(0).childNode(1)).text());
    }

    @Test
    public void testParseString() {
        String xml = "<root><element1>value1</element1><element2>value2</element2></root>";
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
        assertEquals("root", ((Element) document.childNode(0)).tagName());
        assertEquals(2, document.childNode(0).childNodeSize());
        assertTrue(document.childNode(0).childNode(0) instanceof Element);
        assertTrue(document.childNode(0).childNode(1) instanceof Element);
        assertEquals("element1", ((Element) document.childNode(0).childNode(0)).tagName());
        assertEquals("value1", ((Element) document.childNode(0).childNode(0)).text());
        assertEquals("element2", ((Element) document.childNode(0).childNode(1)).tagName());
        assertEquals("value2", ((Element) document.childNode(0).childNode(1)).text());
    }

    @Test
    public void testInitialiseParse() {
        Element element = new Element("element");
        xmlTreeBuilder.initialiseParse(null, "http://example.com", null, ParseSettings.preserveCase);
        assertEquals(element, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcessStartTag() {
        String startTag = "<tag>";
        Token.StartTag token = new Token.StartTag(startTag);
        Element element = xmlTreeBuilder.insert(token);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessDoctype() {
        String doctype = "<!DOCTYPE html>";
        Token.Doctype token = new Token.Doctype(doctype);
        xmlTreeBuilder.insert(token);

        Document document = xmlTreeBuilder.currentElement().ownerDocument();
        assertNotNull(document);
        assertEquals(doctype, document.toString());
    }

    @Test
    public void testPopStackToClose() {
        String endTag = "</element>";
        Token.EndTag token = new Token.EndTag(endTag);
        xmlTreeBuilder.popStackToClose(token);

        List<Node> stack = xmlTreeBuilder.stack;
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("element");
        xmlTreeBuilder.insertNode(element);

        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertComment() {
        String commentData = "<!-- comment -->";
        Token.Comment token = new Token.Comment(commentData);
        xmlTreeBuilder.insert(token);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals(commentData, ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testInsertCharacter() {
        String characterData = "Some character data";
        Token.Character token = new Token.Character(characterData);
        xmlTreeBuilder.insert(token);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Node);
        assertEquals(characterData, ((Node) xmlTreeBuilder.currentElement().childNode(0)).outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        Token.Doctype token = new Token.Doctype(String.format("<!DOCTYPE %s PUBLIC \"%s\" \"%s\">", doctypeName, publicIdentifier, systemIdentifier));
        xmlTreeBuilder.insert(token);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof org.jsoup.nodes.DocumentType);
        assertEquals(doctypeName, ((org.jsoup.nodes.DocumentType) xmlTreeBuilder.currentElement().childNode(0)).name());
        assertEquals(publicIdentifier, ((org.jsoup.nodes.DocumentType) xmlTreeBuilder.currentElement().childNode(0)).publicId());
        assertEquals(systemIdentifier, ((org.jsoup.nodes.DocumentType) xmlTreeBuilder.currentElement().childNode(0)).systemId());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>text</element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", null, ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("element", ((Element) nodes.get(0)).tagName());
        assertEquals("text", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testInsertSelfClosingTag() {
        String startTag = "<tag />";
        Token.StartTag token = new Token.StartTag(startTag);
        Element element = xmlTreeBuilder.insert(token);

        assertNotNull(element);
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

}