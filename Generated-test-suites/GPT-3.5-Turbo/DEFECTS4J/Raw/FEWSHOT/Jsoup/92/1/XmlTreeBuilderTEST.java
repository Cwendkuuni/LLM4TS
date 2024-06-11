package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        // Test if the document is placed onto the stack
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        StringReader inputReader = new StringReader(input);
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(inputReader, baseUri, parser);
        List<Node> stack = xmlTreeBuilder.parseFragment(input, baseUri, parser);
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Document);
        Document document = (Document) stack.get(0);
        assertEquals("https://www.example.com", document.baseUri());

        // Test if the syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void testParseString() {
        String input = "<root></root>";
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals("root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("element", xmlTreeBuilder.defaultSettings());
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("element", element.nodeName());
        assertFalse(element.isBlock());
        assertFalse(element.isInline());
        assertFalse(element.isData());
        assertFalse(element.isFormListed());
        assertFalse(element.hasText());
        assertFalse(element.isBlock());
        assertEquals("element", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.stack.add(new Element("element"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("body"));
        xmlTreeBuilder.stack.add(new Element("html"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("html", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(Comment.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("data");
        xmlTreeBuilder.insert(character);
        assertEquals(TextNode.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        assertEquals(DocumentType.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("element"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("body"));
        xmlTreeBuilder.stack.add(new Element("html"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(3, xmlTreeBuilder.stack.size());
        assertEquals("html", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("body", xmlTreeBuilder.stack.get(1).nodeName());
        assertEquals("element", xmlTreeBuilder.stack.get(2).nodeName());
    }

    @Test
    public void testParseFragmentWithString() {
        String input = "<div><p></p></div>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element divElement = (Element) nodes.get(0);
        assertEquals("div", divElement.nodeName());
        assertEquals("https://www.example.com", divElement.baseUri());
        assertEquals(1, divElement.childNodeSize());
        assertTrue(divElement.childNode(0) instanceof Element);
        Element pElement = (Element) divElement.childNode(0);
        assertEquals("p", pElement.nodeName());
    }

    @Test
    public void testParseFragmentWithElement() {
        String input = "<p></p>";
        String baseUri = "https://www.example.com";
        Element context = new Element("div");
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, context, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element pElement = (Element) nodes.get(0);
        assertEquals("p", pElement.nodeName());
        assertEquals("https://www.example.com", pElement.baseUri());
    }
}