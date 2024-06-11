package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document document = new XmlTreeBuilder().parse("", "baseUri");
        assertNotNull(document);
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        Document document = new XmlTreeBuilder().parse("<root></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(1, document.childNodes().size());
        assertEquals("root", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithTextNode() {
        Document document = new XmlTreeBuilder().parse("<root><text>Hello World</text></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(1, document.childNodes().size());
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        assertEquals(0, children.get(0).childNodes().size());
        assertEquals("text", children.get(0).tagName());
        TextNode textNode = (TextNode) children.get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseXmlWithElementNode() {
        Document document = new XmlTreeBuilder().parse("<root><child></child></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(1, document.childNodes().size());
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        assertEquals(0, children.get(0).childNodes().size());
        assertEquals("child", children.get(0).tagName());
    }

    @Test
    public void testParseXmlWithMixedNodes() {
        Document document = new XmlTreeBuilder().parse("<root><text>Hello</text><child></child></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(2, document.childNodes().size());
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        TextNode textNode = (TextNode) children.get(0);
        assertEquals("Hello", textNode.text());
        Element childElement = (Element) children.get(1);
        assertEquals("child", childElement.tagName());
        assertEquals(0, childElement.childNodes().size());
    }

    @Test
    public void testParseXmlWithCommentNode() {
        Document document = new XmlTreeBuilder().parse("<root><!--comment--></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(1, document.childNodes().size());
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Comment comment = (Comment) children.get(0);
        assertEquals("comment", comment.data());
    }

    @Test
    public void testParseXmlWithCDataNode() {
        Document document = new XmlTreeBuilder().parse("<root><![CDATA[Hello World]]></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(1, document.childNodes().size());
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        CDataNode cDataNode = (CDataNode) children.get(0);
        assertEquals("Hello World", cDataNode.data());
    }

    @Test
    public void testParseXmlWithDoctypeNode() {
        Document document = new XmlTreeBuilder().parse("<!DOCTYPE root [<!ATTLIST myatt CDATA #REQUIRED>]<root></root>", "baseUri");
        Element root = document.getElementById("root");
        assertNotNull(root);
        assertEquals(1, document.childNodes().size());
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        DocumentType doctype = (DocumentType) children.get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals(1, doctype.attributes().size());
        assertEquals("myatt", doctype.attribute(0).getName());
        assertEquals("CDATA", doctype.attribute(0).getValue());
    }

    @Test
    public void testParseXmlWithInvalidXml() {
        Document document = new XmlTreeBuilder().parse("<root><text><![CDATA[Hello World]]></text></root>", "baseUri");
        assertNull(document);
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = new XmlTreeBuilder().parseFragment("<root><text>Hello</text><child></child></root>", "baseUri");
        assertEquals(3, nodes.size());
        Node root = nodes.get(0);
        assertEquals("root", root.tagName());
        Element textElement = (Element) nodes.get(1);
        assertEquals("text", textElement.tagName());
        TextNode textNode = (TextNode) textElement.childNodes().get(0);
        assertEquals("Hello", textNode.text());
        Element childElement = (Element) nodes.get(2);
        assertEquals("child", childElement.tagName());
    }

    @Test
    public void testParseXmlFragmentWithContext() {
        List<Node> nodes = new XmlTreeBuilder().parseFragment("<root><text>Hello</text><child></child></root>", new Element("outer"), "baseUri");
        assertEquals(4, nodes.size());
        Node outer = nodes.get(0);
        assertEquals("outer", outer.tagName());
        Node root = nodes.get(1);
        assertEquals("root", root.tagName());
        Element textElement = (Element) nodes.get(2);
        assertEquals("text", textElement.tagName());
        TextNode textNode = (TextNode) textElement.childNodes().get(0);
        assertEquals("Hello", textNode.text());
        Element childElement = (Element) nodes.get(3);
        assertEquals("child", childElement.tagName());
    }
}