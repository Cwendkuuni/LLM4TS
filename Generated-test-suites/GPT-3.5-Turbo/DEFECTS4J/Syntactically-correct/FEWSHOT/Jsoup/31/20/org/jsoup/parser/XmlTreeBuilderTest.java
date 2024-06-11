package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert stack has the document node
        assertEquals("html", xmlTreeBuilder.stack.pop().nodeName());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.process(startTag);

        // Assert stack has a new element with tag name "div"
        Element element = xmlTreeBuilder.stack.peek();
        assertEquals("div", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
        assertNull(element.attributes().asList());
    }

    @Test
    public void testProcessEndTag() {
        Element div = new Element("div", "http://www.example.com");
        xmlTreeBuilder.stack.push(div);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);

        // Assert stack is empty (div has been popped)
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(comment);

        // Assert the document node has a child with comment data "This is a comment"
        Node child = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(NodeType.COMMENT, child.nodeType());
        assertEquals("This is a comment", ((Comment) child).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Some text");
        xmlTreeBuilder.process(character);

        // Assert the document node has a child with text "Some text"
        Node child = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(NodeType.TEXT, child.nodeType());
        assertEquals("Some text", ((TextNode) child).text());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.process(doctype);

        // Assert the document node has a child with doctype attributes
        Node child = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(NodeType.DOCTYPE, child.nodeType());
        DocumentType doctypeNode = (DocumentType) child;
        assertEquals("html", doctypeNode.getName());
        assertNull(doctypeNode.getPublicIdentifier());
        assertNull(doctypeNode.getSystemIdentifier());
        assertEquals("http://www.example.com", doctypeNode.baseUri());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div", "http://www.example.com");
        xmlTreeBuilder.stack.push(element);

        Element child = new Element("span", "http://www.example.com");
        xmlTreeBuilder.insertNode(child);

        // Assert the top element in the stack has a child with tag name "span"
        assertEquals("span", element.childNode(0).nodeName());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.baseUri = "http://www.example.com";
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert element has tag name "div", proper base URI and no attributes
        assertEquals("div", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
        assertNull(element.attributes().asList());
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("img");
        xmlTreeBuilder.baseUri = "http://www.example.com";
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert element has tag name "img", proper base URI, no attributes and is self-closing
        assertEquals("img", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
        assertNull(element.attributes().asList());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        // Assert the document node has a child with comment data "This is a comment"
        Node child = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(NodeType.COMMENT, child.nodeType());
        assertEquals("This is a comment", ((Comment) child).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Some text");
        xmlTreeBuilder.insert(characterToken);

        // Assert the document node has a child with text "Some text"
        Node child = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(NodeType.TEXT, child.nodeType());
        assertEquals("Some text", ((TextNode) child).text());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        // Assert the document node has a child with doctype attributes
        Node child = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(NodeType.DOCTYPE, child.nodeType());
        DocumentType doctypeNode = (DocumentType) child;
        assertEquals("html", doctypeNode.getName());
        assertNull(doctypeNode.getPublicIdentifier());
        assertNull(doctypeNode.getSystemIdentifier());
        assertEquals("http://www.example.com", doctypeNode.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Element div1 = new Element("div", "http://www.example.com");
        Element div2 = new Element("div", "http://www.example.com");
        Element p = new Element("p", "http://www.example.com");
        xmlTreeBuilder.stack.push(div1);
        xmlTreeBuilder.stack.push(div2);
        xmlTreeBuilder.stack.push(p);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert the stack is empty (div2 and p have been popped)
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}