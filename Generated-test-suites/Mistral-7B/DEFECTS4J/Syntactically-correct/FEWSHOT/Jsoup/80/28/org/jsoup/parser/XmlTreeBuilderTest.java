package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><child>Hello</child></root>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.nodeName());
        assertEquals("Hello", child.childNodes().get(0).text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><tag/></root>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element tag = (Element) root.childNodes().get(0);
        assertEquals("tag", tag.nodeName());
        assertTrue(tag.hasAttrs());
        assertTrue(tag.tagName().isSelfClosing());
        assertEquals(0, tag.childNodes().size());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<!-- This is a comment -->";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertInstanceOf(Comment.class, doc.childNodes().get(0));
    }

    @Test
    public void testParseXmlWithCDATA() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><![CDATA[This is CDATA]]></root>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Node cdataNode = root.childNodes().get(0);
        assertTrue(cdataNode instanceof CDataNode);
        assertEquals("This is CDATA", cdataNode.text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE root [ <!ATTLIST mytag attr CDATA #REQUIRED> ]><root></root>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
        Node doctypeNode = doc.head();
        assertTrue(doctYPENode instanceof DocumentType);
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("MyTag", ((DocumentType) doctypeNode).getName());
    }

    @Test
    public void testParseXmlWithNestedTags() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><child>< grandchild>Hello Grandchild</grandchild></child></root>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.nodeName());
        assertEquals(1, child.childNodes().size());
        Element grandchild = (Element) child.childNodes().get(0);
        assertEquals("grandchild", grandchild.nodeName());
        assertEquals("Hello Grandchild", grandchild.childNodes().get(0).text());
    }

    @Test
    public void testParseXmlWithMixedContent() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><child>Text</child><child><grandchild><![CDATA[CDATA Text]]></grandchild></child></root>";
        Document doc = builder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(2, root.childNodes().size());
        Element textChild = (Element) root.childNodes().get(0);
        assertEquals("child", textChild.nodeName());
        assertEquals("Text", textChild.childNodes().get(0).text());
        Element cdataChild = (Element) root.childNodes().get(1);
        assertEquals("child", cdataChild.nodeName());
        Node grandchild = cdataChild.childNodes().get(0);
        assertTrue(grandchild instanceof CDataNode);
        assertEquals("CDATA Text", grandchild.text());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><child>Text</child><child><grandchild><![CDATA[CDATA Text]]></grandchild></child></root>";
        List<Node> nodes = builder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.defaultSettings());
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(2, root.childNodes().size());
        Element textChild = (Element) root.childNodes().get(0);
        assertEquals("child", textChild.nodeName());
        assertEquals("Text", textChild.childNodes().get(0).text());
        Element cdataChild = (Element) root.childNodes().get(1);
        assertEquals("child", cdataChild.nodeName());
        Node grandchild = cdataChild.childNodes().get(0);
        assertTrue(grandchild instanceof CDataNode);
        assertEquals("CDATA Text", grandchild.text());
    }
}