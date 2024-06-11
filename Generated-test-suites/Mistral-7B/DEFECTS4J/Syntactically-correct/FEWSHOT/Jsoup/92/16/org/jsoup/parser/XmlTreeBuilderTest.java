package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXMLWithNoContent() {
        String xml = "<root></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("root", doc.childNodes().get(0).nodeName());
    }

    @Test
    public void testParseXMLWithTextContent() {
        String xml = "<root>Hello World</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals("Hello World", root.childNodes().get(0).text());
    }

    @Test
    public void testParseXMLWithEmptyTextContent() {
        String xml = "<root></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertNull(root.childNodes().get(0));
    }

    @Test
    public void testParseXMLWithComment() {
        String xml = "<root><!-- This is a comment --></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        List<Node> commentNodes = root.childNodes();
        assertEquals(2, commentNodes.size());
        Node comment = commentNodes.get(1);
        assertEquals(Node.COMMENT_NODE, comment.nodeType());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXMLWithCDataSection() {
        String xml = "<root><![CDATA[ <p>This is a CDATA section</p> ]]></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        List<Node> nodes = root.childNodes();
        assertEquals(1, nodes.size());
        Node cdataNode = nodes.get(0);
        assertEquals(Node.CDATA_SECTION_NODE, cdataNode.nodeType());
        StringBuilder expectedCDATA = new StringBuilder();
        expectedCDATA.append("<p>This is a CDATA section</p>");
        assertEquals(expectedCDATA.toString(), cdataNode.text());
    }

    @Test
    public void testParseXMLWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST myattr CDATA #REQUIRED > ]><root></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        DocumentType doctype = (DocumentType) doc.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("[ <!ATTLIST myattr CDATA #REQUIRED > ]", doctype.getSystemIdentifier());
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testParseXMLWithSelfClosingTag() {
        String xml = "<root><img src=\"image.png\" /></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        List<Node> nodes = root.childNodes();
        assertEquals(1, nodes.size());
        Element img = (Element) nodes.get(0);
        assertEquals("img", img.nodeName());
        assertTrue(img.hasAttr("src"));
        assertEquals("image.png", img.attr("src"));
    }

    @Test
    public void testParseXMLWithNestedTags() {
        String xml = "<root><tag1><tag2>Hello World</tag2></tag1></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        List<Node> nodes = root.childNodes();
        assertEquals(1, nodes.size());
        Element tag1 = (Element) nodes.get(0);
        assertEquals("tag1", tag1.nodeName());
        List<Node> tag1Nodes = tag1.childNodes();
        assertEquals(1, tag1Nodes.size());
        Element tag2 = (Element) tag1Nodes.get(0);
        assertEquals("tag2", tag2.nodeName());
        assertEquals("Hello World", tag2.childNodes().get(0).text());
    }

    @Test(expected = AssertionError.class)
    public void testParseXMLWithInvalidToken() {
        String xml = "<root><![CDATA[ <p>This is a CDATA section<tag></tag></p> ]]></root>";
        new XmlTreeBuilder().parse(new StringReader(xml), null);
    }
}