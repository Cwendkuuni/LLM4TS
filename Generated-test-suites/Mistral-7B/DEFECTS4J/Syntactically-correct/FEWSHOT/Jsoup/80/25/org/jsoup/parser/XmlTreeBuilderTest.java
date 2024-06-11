package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithAttributes() {
        String xmlString = "<root attr1=\"value1\" attr2=\"value2\"><element attr=\"value\" attr3=\"value3\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        List<Attribute> attrs = root.attributes();
        assertEquals(2, attrs.size());
        assertEquals("value1", attrs.get(0).getValue());
        assertEquals("value2", attrs.get(1).getValue());

        Element element = root.child(0);
        assertEquals("element", element.tagName());
        attrs = element.attributes();
        assertEquals(3, attrs.size());
        assertEquals("value", attrs.get(0).getValue());
        assertEquals("value3", attrs.get(2).getValue());
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseEmptyDocument() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(""), "");
        assertTrue(xmlDoc.isEmpty());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xmlString = "<root><img src=\"image.jpg\" /> </root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertTrue(img.isSelfClosing());
    }

    @Test
    public void testParseWithUnknownTag() {
        String xmlString = "<customTag />";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element customTag = xmlDoc.rootElement();
        assertEquals("customTag", customTag.tagName());
        assertTrue(customTag.isSelfClosing());
    }

    @Test
    public void testParseWithComment() {
        String xmlString = "<root><!-- This is a comment --><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        List<Node> nodes = xmlDoc.childNodes();
        Node comment = nodes.get(0);
        assertEquals(Node.COMMENT_NODE, comment.nodeType());
        assertEquals("This is a comment", comment.text());

        Element element = (Element) nodes.get(1);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithCDATA() {
        String xmlString = "<root><![CDATA[This is CDATA]]></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();
        Node cdata = nodes.get(0);
        assertEquals(Node.CDATA_SECTION_NODE, cdata.nodeType());
        assertEquals("This is CDATA", cdata.text());
    }

    @Test
    public void testParseWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"document.dtd\">";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        DocumentType doctype = (DocumentType) xmlDoc.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("document.dtd", doctype.systemId());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(n -> n.cloneNode(true)).collect(Collectors.toList());

        Element root = (Element) nodes.get(0);
        assertEquals("element", root.tagName());
        assertEquals("value", root.attr("attr"));
        assertEquals("Content", root.text());
    }

    @Test
    public void testParseFragmentWithEmptyDocument() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertTrue(nodes.isEmpty());
    }
}