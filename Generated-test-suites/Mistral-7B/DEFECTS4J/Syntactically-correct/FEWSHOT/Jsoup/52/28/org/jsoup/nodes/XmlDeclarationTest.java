package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;
    private Document document;

    @Before
    public void setUp() {
        this.document = new Document();
        this.xmlDeclaration = new XmlDeclaration("xml", null, false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = attributes;

        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("standalone", null, false);
        assertEquals("<!DOCTYPE standalone [ ]>", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("html", null, false);
        assertNull(xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        Appendable appendable = new StringBuilder();
        xmlDeclaration.outerHtmlHead(appendable, 0, null);

        assertEquals("<?xml?>", appendable.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Appendable appendable = new StringBuilder();
        xmlDeclaration.outerHtmlTail(appendable, 0, null);

        assertEquals("", appendable.toString());
    }

    @Test
    public void testToString() throws IOException {
        Element element = new Element("root");
        element.appendChild(xmlDeclaration);
        TextNode textNode = new TextNode("Hello World!");
        element.appendChild(textNode);

        StringBuilder expected = new StringBuilder();
        expected.append("<root>");
        expected.append(xmlDeclaration.outerHtml());
        expected.append(textNode.text());
        expected.append("</root>");

        assertEquals(expected.toString(), document.body().outerHtml());
    }
}