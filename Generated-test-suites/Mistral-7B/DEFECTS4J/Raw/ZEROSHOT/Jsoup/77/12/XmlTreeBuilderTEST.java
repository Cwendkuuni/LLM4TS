package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser.XmlParser;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlString = "<root><child attr=\"value\"><subChild/></child></root>";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        List<Node> childNodes = child.children();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element subChild = (Element) childNodes.get(0);
        assertEquals("subChild", subChild.tagName());
        assertNull(subChild.attributes().get("attr"));
    }

    @Test
    public void testParseXmlWithAttributes() {
        String xmlString = "<root attr1=\"value1\" attr2='value2'><child attr=\"value\"><subChild attr3=\"value3\" attr4='value4'/></child></root>";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        List<Node> childNodes = child.children();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element subChild = (Element) childNodes.get(0);
        assertEquals("subChild", subChild.tagName());
        Map<String, String> attributes = subChild.attributes();
        assertEquals("value3", attributes.get("attr3"));
        assertEquals("value4", attributes.get("attr4"));
        assertNull(attributes.get("attr1"));
        assertNull(attributes.get("attr2"));
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<root><child><img src=\"image.jpg\"/></child></root>";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        List<Node> childNodes = child.children();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element img = (Element) childNodes.get(0);
        assertEquals("img", img.tagName());
        assertTrue(img.isSelfClosing());
        assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testParseXmlWithComments() {
        String xmlString = "<root><!-- This is a comment --><child><!-- Another comment --><subChild/></child></root>";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        List<Node> childNodes = child.children();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element subChild = (Element) childNodes.get(0);
        assertEquals("subChild", subChild.tagName());
        List<Node> subChildNodes = subChild.children();
        assertTrue(subChildNodes.isEmpty());

        List<Node> comments = root.childNodes();
        assertEquals(2, comments.size());
        assertTrue(comments.get(0) instanceof Comment);
        Comment firstComment = (Comment) comments.get(0);
        assertEquals("This is a comment", firstComment.data());
        assertTrue(comments.get(1) instanceof Comment);
        Comment secondComment = (Comment) comments.get(1);
        assertEquals("Another comment", secondComment.data());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root></root>";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        DocumentType doctype = xmlDoc.doctype();
        assertEquals("root", doctype.name());
        assertEquals("root.dtd", doctype.systemIdentifier());
        assertNull(doctype.externalSubset());
        assertEquals("root", xmlDoc.rootElement().tagName());
    }

    @Test
    public void testParseXmlWithDoctypeExternalSubset() {
        String xmlString = "<!DOCTYPE root [ <!ENTITY % general PE \"CDATA[ <!ENTITY eacute E '&Acirc;'>\n<!ENTITY egrave E '&Agrave;'\"]>]\n<root>hello &eacute; &egrave;</root>";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        DocumentType doctype = xmlDoc.doctype();
        assertEquals("root", doctype.name());
        assertEquals("", doctype.systemIdentifier());
        String externalSubset = "PE CDATA[\n<!ENTITY % general PE \"CDATA[\n<!ENTITY eacute E '&Acirc;'\n<!ENTITY egrave E '&Agrave;'\"]>\n";
        Document externalSubsetDoc = Jsoup.parse(externalSubset, baseUri, XmlParser.instance());
        assertArrayEquals(externalSubsetDoc.childNodes().toArray(), doctype.externalSubset().childNodes().toArray());
        assertEquals("root", xmlDoc.rootElement().tagName());

        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();
        assertEquals(2, nodes.size());
        TextNode firstNode = (TextNode) nodes.get(0);
        assertEquals("hello ", firstNode.text());
        TextNode secondNode = (TextNode) nodes.get(1);
        assertEquals(" &eacute; &egrave;", secondNode.text());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<child><subChild/></child>";
        String baseUri = "http://example.com";

        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase).toArray();

        Element child = (Element) nodes[0];
        assertEquals("child", child.tagName());
        List<Node> childNodes = child.children();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element subChild = (Element) childNodes.get(0);
        assertEquals("subChild", subChild.tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXmlInput() {
        String xmlString = "<root><child><img src=\"image.jpg\"";
        String baseUri = "http://example.com";

        Document xmlDoc = Jsoup.parse(xmlString, baseUri, XmlParser.instance());

        Validate.fail("Expected a parse error, but none was thrown.");
    }
}