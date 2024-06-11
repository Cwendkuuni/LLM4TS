package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Test class for XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilder() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "  <element1 attr1=\"value1\">Content 1</element1>\n" +
                "  <element2>\n" +
                "    <subElement1 attr2=\"value2\">Content 2.1</subElement1>\n" +
                "    <subElement2 attr3=\"value3\">Content 2.2</subElement2>\n" +
                "  </element2>\n" +
                "</root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc;
        try {
            xmlDoc = Jsoup.parse(xmlString, "", Parser.xmlParser());
        } catch (IOException e) {
            fail("Failed to parse XML string: " + e.getMessage());
            return;
        }

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());

        List<Node> children = root.children();
        assertEquals(2, children.size());

        Element element1 = (Element) children.get(0);
        assertEquals("element1", element1.tagName());
        List<Attribute> attributes = element1.attributes();
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get(0).getValue());

        Element element2 = (Element) children.get(1);
        assertEquals("element2", element2.tagName());

        List<Node> subElements = element2.children();
        assertEquals(2, subElements.size());

        Element subElement1 = (Element) subElements.get(0);
        assertEquals("subElement1", subElement1.tagName());
        List<Attribute> subElementAttributes = subElement1.attributes();
        assertEquals(1, subElementAttributes.size());
        assertEquals("value2", subElementAttributes.get(0).getValue());

        Element subElement2 = (Element) subElements.get(1);
        assertEquals("subElement2", subElement2.tagName());
        List<Attribute> subElement2Attributes = subElement2.attributes();
        assertEquals(1, subElement2Attributes.size());
        assertEquals("value3", subElement2Attributes.get(0).getValue());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "  <element1 attr1=\"value1\">Content 1</element1>\n" +
                "  <element2>\n" +
                "    <subElement1 attr2=\"value2\">Content 2.1</subElement1>\n" +
                "    <subElement2 attr3=\"value3\">Content 2.2</subElement2>\n";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc;
        try {
            xmlDoc = Jsoup.parse(xmlString, "", Parser.xmlParser());
        } catch (IOException e) {
            fail("Failed to parse XML string: " + e.getMessage());
            return;
        }

        // This line will cause an AssertionError due to 'Validation.fail' call in XmlTreeBuilder.
        Validate.fail("Unexpected token type: 3");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> rootChildren = root.children();
        assertEquals(1, rootChildren.size());

        Element element = (Element) rootChildren.get(0);
        assertEquals("element", element.tagName());
        List<Attribute> elementAttributes = element.attributes();
        assertEquals(1, elementAttributes.size());
        assertEquals("value", elementAttributes.get(0).getValue());
        List<Node> elementChildren = element.children();
        assertEquals(0, elementChildren.size());
    }
}