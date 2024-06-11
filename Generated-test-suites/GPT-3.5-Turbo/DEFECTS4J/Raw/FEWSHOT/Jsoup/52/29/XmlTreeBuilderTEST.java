package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><foo>bar</foo></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertFalse(errors.hasErrors());
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.doc.outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("div");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.stack.size());
        Element element = xmlTreeBuilder.stack.get(0);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        Token.EndTag endTag = new Token.EndTag("div");
        assertTrue(xmlTreeBuilder.process(endTag));
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("Sample comment");
        assertTrue(xmlTreeBuilder.process(commentToken));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node comment = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(comment instanceof Comment);
        assertEquals("Sample comment", comment.outerHtml());
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character("Sample text");
        assertTrue(xmlTreeBuilder.process(characterToken));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node textNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Sample text", textNode.outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        assertTrue(xmlTreeBuilder.process(doctype));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node doctypeNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("<!doctype html>", doctypeNode.outerHtml());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "myDiv");
        startTag.attributes.put("class", "container");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.nodeName());
        assertEquals("myDiv", element.attr("id"));
        assertEquals("container", element.attr("class"));
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("Sample comment");
        xmlTreeBuilder.currentElement().appendChild(new Element("div"));
        xmlTreeBuilder.insert(commentToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        Node comment = xmlTreeBuilder.currentElement().childNodes().get(1);
        assertTrue(comment instanceof Comment);
        assertEquals("Sample comment", comment.outerHtml());
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("Sample text");
        xmlTreeBuilder.currentElement().appendChild(new Element("div"));
        xmlTreeBuilder.insert(characterToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        Node textNode = xmlTreeBuilder.currentElement().childNodes().get(1);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Sample text", textNode.outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.currentElement().appendChild(new Element("div"));
        xmlTreeBuilder.insert(doctype);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        Node doctypeNode = xmlTreeBuilder.currentElement().childNodes().get(1);
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("<!doctype html>", doctypeNode.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("span"));
        xmlTreeBuilder.stack.add(new Element("div"));

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("span", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(2, nodes.size());

        Node node1 = nodes.get(0);
        assertTrue(node1 instanceof Element);
        assertEquals("p", node1.nodeName());
        assertEquals("http://example.com", ((Element) node1).baseUri());
        assertEquals("<p>Paragraph 1</p>", node1.outerHtml());

        Node node2 = nodes.get(1);
        assertTrue(node2 instanceof Element);
        assertEquals("p", node2.nodeName());
        assertEquals("http://example.com", ((Element) node2).baseUri());
        assertEquals("<p>Paragraph 2</p>", node2.outerHtml());
    }
}