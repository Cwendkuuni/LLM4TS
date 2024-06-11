package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        Element root = document.getRootElement();
        assertTrue(root.tagName().equals("root"));
        List<Node> children = root.children();
        Element element = (Element) children.get(0);
        assertTrue(element.tagName().equals("element"));
        List<Node> attributes = element.attributes();
        Node attrNode = attributes.get("attr");
        assertTrue(attrNode.toString().equals("value"));
        assertTrue(element.text().toString().equals("Content"));
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xmlString = "<root><element></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        Element root = document.getRootElement();
        assertTrue(root.tagName().equals("root"));
        List<Node> children = root.children();
        Element element = (Element) children.get(0);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.text().toString().isEmpty());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<root><element /></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        Element root = document.getRootElement();
        assertTrue(root.tagName().equals("root"));
        List<Node> children = root.children();
        Element element = (Element) children.get(0);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.isClosed());
        assertTrue(element.text().toString().isEmpty());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"http://example.com/dtd.dtd\"><root><element></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        DocumentType doctype = document.doctype();
        assertNotNull(doctype);
        assertTrue(doctype.name().equals("root"));
        assertTrue(doctype.system().toString().equals("http://example.com/dtd.dtd"));
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xmlString = "<root><![CDATA[This is some CDATA content]]><element></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        Element root = document.getRootElement();
        List<Node> children = root.children();
        Node node = children.get(0);
        assertTrue(node instanceof CDataNode);
        assertTrue(((CDataNode) node).getData().toString().equals("This is some CDATA content"));
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlString = "<root><!-- This is a comment --><element></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        Element root = document.getRootElement();
        List<Node> children = root.children();
        Node node = children.get(0);
        assertTrue(node instanceof Comment);
        assertTrue(((Comment) node).data().toString().equals("This is a comment"));
    }

    @Test
    public void testParseXmlWithMixedContent() {
        String xmlString = "<root><![CDATA[This is some CDATA content]]><element attr=\"value\">Content<!-- This is a comment--></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
        assertNotNull(document);
        Element root = document.getRootElement();
        List<Node> children = root.children();
        Node cdataNode = children.get(0);
        assertTrue(cdataNode instanceof CDataNode);
        assertTrue(((CDataNode) cdataNode).getData().toString().equals("This is some CDATA content"));

        Element element = (Element) children.get(1);
        assertTrue(element.tagName().equals("element"));
        List<Node> attributes = element.attributes();
        Node attrNode = attributes.get("attr");
        assertTrue(attrNode.toString().equals("value"));
        assertTrue(element.text().toString().isEmpty());

        List<Node> comments = element.children();
        Node commentNode = comments.get(0);
        assertTrue(commentNode instanceof Comment);
        assertTrue(((Comment) commentNode).data().toString().equals("This is a comment"));
    }
}