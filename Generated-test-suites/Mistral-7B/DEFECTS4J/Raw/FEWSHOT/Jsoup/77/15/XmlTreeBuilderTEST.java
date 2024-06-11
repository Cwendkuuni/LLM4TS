package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlDocument() {
        String xmlString = "<root><element attr='value'><subElement></subElement></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        assertNotNull(document);
        Element root = document.element("root");
        assertNotNull(root);
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element element = (Element) children.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        Attribute attr = element.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
        List<Node> subElementChildren = element.childNodes();
        assertEquals(1, subElementChildren.size());
        Element subElement = (Element) subElementChildren.get(0);
        assertNotNull(subElement);
        assertEquals("subElement", subElement.tagName());
    }

    @Test
    public void testParseEmptyXmlDocument() {
        Document document = XmlTreeBuilder.parse(new StringReader(""), "");
        assertNotNull(document);
        assertEquals(0, document.numChildren());
    }

    @Test
    public void testParseXmlDocumentWithComment() {
        String xmlString = "<root><!-- This is a comment --><element attr='value'><subElement></subElement></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        assertNotNull(document);
        Element root = document.element("root");
        assertNotNull(root);
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element element = (Element) children.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        List<Node> subElementChildren = element.childNodes();
        assertEquals(1, subElementChildren.size());
        Element subElement = (Element) subElementChildren.get(0);
        assertNotNull(subElement);
        assertEquals("subElement", subElement.tagName());
        Comment commentNode = (Comment) document.select("--[*]--").first(); // selects the first comment in the document
        assertNotNull(commentNode);
        assertEquals("This is a comment", commentNode.data());
    }

    @Test
    public void testParseXmlDocumentWithCDataSection() {
        String xmlString = "<root><![CDATA[<script>alert('Hello World');</script>]]><element attr='value'><subElement></subElement></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        assertNotNull(document);
        Element root = document.element("root");
        assertNotNull(root);
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element element = (Element) children.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        List<Node> subElementChildren = element.childNodes();
        assertEquals(1, subElementChildren.size());
        Element subElement = (Element) subElementChildren.get(0);
        assertNotNull(subElement);
        assertEquals("subElement", subElement.tagName());
        CDataNode cdataNode = (CDataNode) document.select("/*[CDATA[*]]]*/").first(); // selects the first CDATA section in the document
        assertNotNull(cdataNode);
        assertEquals("<script>alert('Hello World');</script>", cdataNode.data());
    }

    @Test
    public void testParseXmlDocumentWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM 'about:config'> <root><element attr='value'><subElement></subElement></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        assertNotNull(document);
        Element root = document.element("root");
        assertNotNull(root);
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element element = (Element) children.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        List<Node> subElementChildren = element.childNodes();
        assertEquals(1, subElementChildren.size());
        Element subElement = (Element) subElementChildren.get(0);
        assertNotNull(subElement);
        assertEquals("subElement", subElement.tagName());
        Doctype doctype = document.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.name());
        assertEquals("about:config", doctype.publicData());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<element attr='value'><subElement></subElement></element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element element = (Element) nodes.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        List<Node> subElementChildren = element.childNodes();
        assertEquals(1, subElementChildren.size());
        Element subElement = (Element) subElementChildren.get(0);
        assertNotNull(subElement);
        assertEquals("subElement", subElement.tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        XmlTreeBuilder.parse(new StringReader(""), "");
    }
}