package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.util.List;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr1=\"value1\" attr2='value2'>content</element></root>";
        ParseSettings settings = new ParseSettings();
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element rootElement = xmlDoc.getRootElement();
        assertNotNull(rootElement);
        assertEquals("root", rootElement.tagName());

        List<Element> rootChildren = rootElement.children();
        assertEquals(1, rootChildren.size());
        Element element = rootChildren.get(0);
        assertEquals("element", element.tagName());

        List<Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());

        Attribute attr1 = attributes.get(0);
        assertEquals("attr1", attr1.getKey());
        assertEquals("value1", attr1.getValue());

        Attribute attr2 = attributes.get(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());

        Text elementContent = (Text) element.childNodes().get(0);
        assertEquals("content", elementContent.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr1=\"value1\" attr2='value2'>content</element>";
        ParseSettings settings = new ParseSettings();

        List<Node> parsedFragment = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), settings);
        Node rootNode = parsedFragment.get(0);
        assertNotNull(rootNode);

        if (rootNode instanceof Element) {
            Element element = (Element) rootNode;
            assertEquals("element", element.tagName());

            List<Attribute> attributes = element.attributes();
            assertEquals(2, attributes.size());

            Attribute attr1 = attributes.get(0);
            assertEquals("attr1", attr1.getKey());
            assertEquals("value1", attr1.getValue());

            Attribute attr2 = attributes.get(1);
            assertEquals("attr2", attr2.getKey());
            assertEquals("value2", attr2.getValue());

            Text elementContent = (Text) element.childNodes().get(0);
            assertEquals("content", elementContent.text());
        }
    }

    @Test
    public void testProcessingDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr1 CDATA #REQUIRED > ]><root><element attr1=\"value1\">content</element></root>";
        ParseSettings settings = new ParseSettings();

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        DocumentType doctype = xmlDoc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.nodeName());

        AttributeList attributes = doctype.attributes();
        assertEquals(1, attributes.size());

        Attribute attlistAttribute = attributes.get(0);
        assertEquals("ATTLIST", attlistAttribute.getKey());
        assertEquals("element", attlistAttribute.getValue());

        Element element = xmlDoc.getRootElement();
        assertEquals("root", element.tagName());

        List<Element> rootChildren = element.children();
        assertEquals(1, rootChildren.size());
        Element parsedElement = rootChildren.get(0);
        assertEquals("element", parsedElement.tagName());

        List<Attribute> parsedAttributes = parsedElement.attributes();
        assertEquals(1, parsedAttributes.size());

        Attribute parsedAttr1 = parsedAttributes.get(0);
        assertEquals("attr1", parsedAttr1.getKey());
        assertNull(parsedAttr1.getValue());
    }

    @Test
    public void testProcessingComment() {
        String xmlString = "<!-- This is a comment -->";
        ParseSettings settings = new ParseSettings();

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        Comment comment = (Comment) xmlDoc.childNodes().get(0);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testProcessingSelfClosingTag() {
        String xmlString = "<selfClosingTag/>";
        ParseSettings settings = new ParseSettings();

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        Element selfClosingTag = (Element) xmlDoc.childNodes().get(0);
        assertNotNull(selfClosingTag);
        assertEquals("selfClosingTag", selfClosingTag.tagName());
        assertFalse(selfClosingTag.childNodes().isEmpty());
    }

    @Test
    public void testProcessingUnknownTag() {
        String xmlString = "<unknownTag/>";
        ParseSettings settings = new ParseSettings();

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        Element unknownTag = (Element) xmlDoc.childNodes().get(0);
        assertNotNull(unknownTag);
        assertTrue(unknownTag.tagName().startsWith("u:"));
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        String xmlString = "<root 123>";
        ParseSettings settings = new ParseSettings();

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");
    }
}