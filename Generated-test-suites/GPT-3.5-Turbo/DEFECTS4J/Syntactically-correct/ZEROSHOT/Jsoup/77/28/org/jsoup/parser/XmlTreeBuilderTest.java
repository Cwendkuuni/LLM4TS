package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

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
    public void testParseReaderBaseUri() {
        StringReader input = new StringReader("<root></root>");
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<root></root>", document.html());
    }

    @Test
    public void testParseStringBaseUri() {
        String input = "<root></root>";
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<root></root>", document.html());
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("<root></root>");
        xmlTreeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        Document doc = xmlTreeBuilder.getDoc();
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("p", "attr1=val1", true);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("val1", element.attr("attr1"));
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("p"), "http://example.com"));
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("div"), "http://example.com"));
        xmlTreeBuilder.popStackToClose(endTag);
        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals("div", stack.get(0).tagName());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("test character");
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("test character", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        xmlTreeBuilder.insert(doctype);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNodes.get(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("public", documentType.getPublicIdentifier());
        assertEquals("system", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("div"), "http://example.com"));
        xmlTreeBuilder.popStackToClose(endTag);
        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals("div", stack.get(0).tagName());
    }

    @Test
    public void testPopStackToCloseFound() {
        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("div"), "http://example.com"));
        Element pElement = new Element(Tag.valueOf("p"), "http://example.com");
        xmlTreeBuilder.getStack().add(pElement);
        xmlTreeBuilder.popStackToClose(endTag);
        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals("div", stack.get(0).tagName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>test</p>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element pElement = (Element) nodes.get(0);
        assertEquals("p", pElement.tagName());
        assertTrue(nodes.get(1) instanceof TextNode);
        TextNode textNode = (TextNode) nodes.get(1);
        assertEquals("test", textNode.getWholeText());
        assertTrue(nodes.get(2) instanceof Element);
        Element rootElement = (Element) nodes.get(2);
        assertEquals("root", rootElement.tagName());
    }
}