package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParsingSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse("<root><element>Hello World</element></root>", "http://test.com/");

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());

        TextNode textNode = (TextNode) element.child(0);
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParsingEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse("<root/>", "http://test.com/");

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        assertEquals(0, root.childSize());
    }

    @Test
    public void testParsingXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse("<root><element /></root>", "http://test.com/");

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals(0, element.childSize());
    }

    @Test
    public void testParsingXmlWithCdataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse("<root><![CDATA[Hello World]]></root>", "http://test.com/");

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        TextNode textNode = (TextNode) root.child(0);
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<root><![CDATA[Hello World]]></root><![CDATA[Hello Again]>", "http://test.com/");
    }

    @Test
    public void testParsingXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse("<!DOCTYPE root SYSTEM \"http://test.com/root.dtd\">" +
                "<root><element>Hello World</element></root>", "http://test.com/");

        DocumentType doctypeNode = xmlDoc.doctype();
        assertNotNull(doctypeNode);
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("http://test.com/root.dtd", doctypeNode.systemIdentifier());

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());

        TextNode textNode = (TextNode) element.child(0);
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParsingXmlWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse("<root><!-- Test Comment --></root>", "http://test.com/");

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Comment comment = root.child(0);
        assertNotNull(comment);
        assertEquals("Test Comment", comment.text());
    }
}