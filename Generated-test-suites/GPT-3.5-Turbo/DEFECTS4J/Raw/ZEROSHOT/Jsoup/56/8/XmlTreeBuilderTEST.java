package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList parseErrorList;
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseErrorList = new ParseErrorList();
        parseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<xml></xml>";
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "https://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList, parseSettings);
        assertNotNull(xmlTreeBuilder.getDoc());
        assertEquals("https://example.com", xmlTreeBuilder.getBaseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setName("xml");
        xmlTreeBuilder.process(startTag);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("xml", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.setName("xml");
        xmlTreeBuilder.process(endTag);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNull(node);
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment();
        comment.setData("Test Comment");
        xmlTreeBuilder.process(comment);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        Comment commentNode = (Comment) node;
        assertEquals("Test Comment", commentNode.getData());
        assertEquals("https://example.com", commentNode.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();
        character.setData("Test Character");
        xmlTreeBuilder.process(character);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals("Test Character", textNode.getWholeText());
        assertEquals("https://example.com", textNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        xmlTreeBuilder.process(doctype);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("html", doctypeNode.name());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.publicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctypeNode.systemIdentifier());
        assertEquals("https://example.com", doctypeNode.baseUri());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "https://example.com");
        xmlTreeBuilder.setStack(new Elements(element));
        Node node = new Element(Tag.valueOf("span"), "https://example.com");
        xmlTreeBuilder.insertNode(node);
        Element parent = xmlTreeBuilder.currentElement();
        assertNotNull(parent);
        assertSame(element, parent);
        assertEquals(1, parent.childNodeSize());
        assertSame(node, parent.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setName("div");
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        startTag.setAttributes(attributes);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
        assertNotNull(element.attributes());
        assertEquals(1, element.attributes().size());
        assertEquals("test", element.attr("id"));
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment();
        comment.setData("Test Comment");
        xmlTreeBuilder.insert(comment);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        Comment commentNode = (Comment) node;
        assertEquals("Test Comment", commentNode.getData());
        assertEquals("https://example.com", commentNode.baseUri());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character();
        character.setData("Test Character");
        xmlTreeBuilder.insert(character);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals("Test Character", textNode.getWholeText());
        assertEquals("https://example.com", textNode.baseUri());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        xmlTreeBuilder.insert(doctype);
        Node node = xmlTreeBuilder.getDoc().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("html", doctypeNode.name());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.publicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctypeNode.systemIdentifier());
        assertEquals("https://example.com", doctypeNode.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("div"), "https://example.com");
        Element element2 = new Element(Tag.valueOf("span"), "https://example.com");
        Element element3 = new Element(Tag.valueOf("a"), "https://example.com");
        xmlTreeBuilder.setStack(new Elements(element1, element2, element3));
        Token.EndTag endTag = new Token.EndTag();
        endTag.setName("span");
        xmlTreeBuilder.popStackToClose(endTag);
        Elements stack = xmlTreeBuilder.getStack();
        assertNotNull(stack);
        assertEquals(1, stack.size());
        assertSame(element1, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String input = "<div><span></span></div>";
        String baseUri = "https://example.com";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, parseErrorList, parseSettings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("div", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
        assertEquals(1, element.childNodeSize());
        Node childNode = element.childNode(0);
        assertNotNull(childNode);
        assertTrue(childNode instanceof Element);
        Element childElement = (Element) childNode;
        assertEquals("span", childElement.nodeName());
        assertEquals("https://example.com", childElement.baseUri());
    }
}