package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertEquals(" root ", doc.tagName());
        Element root = doc.child(0);
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attribute(0).getValue());
        assertEquals("Content", root.child(0).text());
    }

    @Test
    public void testParseWithEmptyDocument() {
        Document doc = XmlTreeBuilder.parse(new StringReader(""), "baseUri");
        assertNotNull(doc);
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testParseWithComment() {
        String xml = "<root><!-- This is a comment --><element></element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Element root = doc.child(0);
        Element element = root.child(0);
        assertEquals(Comment.class, element.get(0).getClass());
        assertEquals("This is a comment", element.get(0).text());
    }

    @Test
    public void testParseWithCData() {
        String xml = "<root><![CDATA[This is CData]]></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Element root = doc.child(0);
        assertEquals(CDataNode.class, root.child(0).getClass());
        assertEquals("This is CData", root.child(0).text());
    }

    @Test
    public void testParseWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\"/></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.getName());
        assertEquals("root", doc.tagName());
        Element root = doc.child(0);
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute(0).getValue());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xml = "<root><img src=\"image.jpg\" /> </root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Element root = doc.child(0);
        Element img = root.child(0);
        assertTrue(img.isSelfClosing());
        assertEquals("img", img.tagName());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(Node::get).collect(Collectors.toList());
        assertEquals(2, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute(0).getValue());
        assertEquals("Content", element.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        XmlTreeBuilder.parse(new StringReader(""), "baseUri");
    }
}