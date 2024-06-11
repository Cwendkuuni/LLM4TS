package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

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
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<root><element>content</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        
        Element rootElement = document.child(0);
        assertNotNull(rootElement);
        assertEquals("element", rootElement.nodeName());
        
        TextNode contentNode = (TextNode) rootElement.childNode(0);
        assertNotNull(contentNode);
        assertEquals("content", contentNode.getWholeText());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>content</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(input, xmlTreeBuilder.getBuffer());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(settings, xmlTreeBuilder.getSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDocument().outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag("element");
        xmlTreeBuilder.process(startTag);
        Element element = xmlTreeBuilder.getCurrentElement();
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        
        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.process(endTag);
        assertNull(xmlTreeBuilder.getCurrentElement());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.setCurrentElement(element);
        TextNode textNode = new TextNode("content");
        xmlTreeBuilder.insertNode(textNode);
        assertEquals(1, element.childNodeSize());
        assertEquals(textNode, element.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.setSelfClosing(false);
        xmlTreeBuilder.insert(startTag);
        Element element = xmlTreeBuilder.getCurrentElement();
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        
        startTag.setSelfClosing(true);
        xmlTreeBuilder.insert(startTag);
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("comment", ((Comment) node).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("content");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("content", ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.insert(doctype);
        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType docType = (DocumentType) node;
        assertEquals("html", docType.nodeName());
        assertEquals("", docType.getPublicIdentifier());
        assertEquals("", docType.getSystemIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.getBaseUri());
    }

    @Test
    public void testPopStackToClose() {
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element element1 = new Element(Tag.valueOf("element1"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("element2"), "http://example.com");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>content</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.nodeName());
    }
}