package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "http://localhost/");
        assertEquals("root", xmlDoc.tagName());

        Element rootElement = xmlDoc.getElementsByTag("root").first();
        List<Node> children = rootElement.children();
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        Attribute attr = attributes.get("attr");
        assertEquals("value", attr.getValue());
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseEmptyXml() {
        String xmlString = "";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "http://localhost/");
        assertNull(xmlDoc);
    }

    @Test
    public void testParseComment() {
        String xmlString = "<!-- This is a comment -->";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "http://localhost/");
        Comment comment = (Comment) xmlDoc.child(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseCData() {
        String xmlString = "<![CDATA[This is CDATA content]]>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "http://localhost/");
        CDataNode cdataNode = (CDataNode) xmlDoc.child(0);
        assertEquals("This is CDATA content", cdataNode.text());
    }

    @Test
    public void testParseDocumentType() {
        String xmlString = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "http://localhost/");
        DocumentType docTypeNode = (DocumentType) xmlDoc.child(0);
        assertEquals("root", docTypeNode.nodeName());
        assertEquals("http://example.com/root.dtd", docTypeNode.systemIdentifier());
    }

    @Test
    public void testParseSelfClosingTag() {
        String xmlString = "<img src=\"image.jpg\" />";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "http://localhost/");
        Element imgElement = (Element) xmlDoc.child(0);
        assertTrue(imgElement.isSelfClosing());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://localhost/", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(node -> node).toList();
        Document xmlDoc = (Document) nodes.get(0);
        assertEquals("root", xmlDoc.tagName());

        Element rootElement = xmlDoc.getElementsByTag("root").first();
        List<Node> children = rootElement.children();
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        Attribute attr = attributes.get("attr");
        assertEquals("value", attr.getValue());
        assertEquals("Content", element.text());
    }
}