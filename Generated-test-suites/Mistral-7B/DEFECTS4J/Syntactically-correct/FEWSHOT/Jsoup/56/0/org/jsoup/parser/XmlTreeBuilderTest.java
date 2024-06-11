package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithRootElement() {
        String xmlString = "<root><element>Hello World</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, null);
        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.tagName());
        List<Element> children = root.children();
        assertEquals(1, children.size());
        Element element = children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<tag/>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, null);
        Element tag = xmlDoc.getElementsByTagName("tag").get(0);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xmlString = "<empty/>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, null);
        Element empty = xmlDoc.getElementsByTagName("empty").get(0);
        assertTrue(empty.content().size() == 0);
    }

    @Test
    public void testParseXmlWithAttributes() {
        String xmlString = "<element attribute1=\"value1\" attribute2='value2'/>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, null);
        Element element = xmlDoc.getElementsByTagName("element").get(0);
        assertEquals("value1", element.attributeValue("attribute1"));
        assertEquals("value2", element.attributeValue("attribute2"));
    }

    @Test
    public void testParseXmlWithComments() {
        String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><!-- This is a comment --><root><element><!-- This is another comment --></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, null);
        List<Node> commentNodes = xmlDoc.getElementsByTag("!--").stream().collect(Collectors.toList());
        assertEquals(2, commentNodes.size());
        Comment comment1 = (Comment) commentNodes.get(0);
        assertEquals("This is a comment", comment1.text());
        Comment comment2 = (Comment) commentNodes.get(1);
        assertEquals("This is another comment", comment2.text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, null);
        DocumentType doctype = xmlDoc.doctype();
        assertEquals("html", doctype.nodeName());
        assertEquals("about:legacy-compat", doctype.publicId());
        assertNull(doctype.systemId());
    }

    @Test
    public void testParseXmlWithFragment() {
        String xmlFragment = "<element><subElement>Hello</subElement></element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, null, null, XmlTreeBuilder.ParseSettings.preserveCase).stream().collect(Collectors.toList());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        List<Node> children = element.children();
        assertEquals(1, children.size());
        Element subElement = (Element) children.get(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals("Hello", subElement.text());
    }

    @Test
    public void testParseXmlWithInvalidInput() {
        String xmlString = "<invalid>";
        try {
            XmlTreeBuilder.parse(xmlString, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected start tag <invalid>", e.getMessage());
        }
    }
}