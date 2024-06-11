package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

/**
 * Tests for XmlTreeBuilder class
 *
 * @author John Doe
 */
public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderDefaultParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><element attr='value'><subElement/><anotherElement attr2='value2'/></element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        Element root = (Element) document.body();
        assertEquals("root", root.tagName());
        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());
        Attribute attr = attributes.get(0);
        assertEquals("attr", attr.getKey());
        assertEquals("value", attr.getValue());
        Attribute attr2 = attributes.get(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
        List<Node> children = element.children();
        assertEquals(2, children.size());
        Element subElement = (Element) children.get(0);
        assertNull(subElement.attributes());
        assertNull(subElement.children());
        Element anotherElement = (Element) children.get(1);
        assertNull(anotherElement.attributes());
        assertNull(anotherElement.children());
    }

    @Test
    public void testXmlTreeBuilderParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<element attr='value'><subElement/><anotherElement attr2='value2'/></element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) nodes.get(0);
        assertEquals("element", root.tagName());
        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(1, attributes.size());
        Attribute attr = attributes.get(0);
        assertEquals("attr", attr.getKey());
        assertEquals("value", attr.getValue());
        List<Node> children = element.children();
        assertEquals(2, children.size());
        Element subElement = (Element) children.get(0);
        assertNull(subElement.attributes());
        assertNull(subElement.children());
        Element anotherElement = (Element) children.get(1);
        assertNull(anotherElement.attributes());
        assertNull(anotherElement.children());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader(""), "");
    }

    @Test
    public void testXmlTreeBuilderParseInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String invalidXml = "<root><element<tag name='value'/><anotherElement attr2='value2'/></element>";
        ParseErrorList errors = new ParseErrorList();
        Document document = xmlTreeBuilder.parse(new StringReader(invalidXml), "http://test.com", errors, ParseSettings.preserveCase);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("<root><element<tag name='value'/><anotherElement attr2='value2'/></element>", error.toString());
        assertNull(document);
    }

    @Test
    public void testXmlTreeBuilderParseWithErrors() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlStringWithErrors = "<root><element><subElement><tag name='value'/></subElement></element>";
        ParseErrorList errors = new ParseErrorList();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlStringWithErrors), "http://test.com", errors, ParseSettings.preserveCase);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("<subElement><tag name='value'/>", error.toString());
        Element root = (Element) document.body();
        assertEquals("root", root.tagName());
        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        List<Node> children = element.children();
        assertEquals(1, children.size());
        Element subElement = (Element) children.get(0);
        assertEquals("subElement", subElement.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(0, attributes.size());
        List<Node> subElementChildren = subElement.children();
        assertEquals(1, subElementChildren.size());
        Element tagElement = (Element) subElementChildren.get(0);
        assertEquals("tag", tagElement.tagName());
        List<Attribute> tagElementAttributes = tagElement.attributes();
        assertEquals(1, tagElementAttributes.size());
        Attribute tagAttribute = tagElementAttributes.get(0);
        assertEquals("name", tagAttribute.getKey());
        assertEquals("value", tagAttribute.getValue());
    }

    @Test
    public void testXmlTreeBuilderParseWithUnknownTags() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlStringWithUnknownTags = "<root><element><unknownTag attr='value'/><anotherElement attr2='value2'/></element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xmlStringWithUnknownTags), "http://test.com");
        Element root = (Element) document.body();
        assertEquals("root", root.tagName());
        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        List<Node> children = element.children();
        assertEquals(2, children.size());
        Element unknownElement = (Element) children.get(0);
        assertEquals("unknownTag", unknownElement.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(1, attributes.size());
        Attribute attr = attributes.get(0);
        assertEquals("attr", attr.getKey());
        assertEquals("value", attr.getValue());
        List<Node> subElementChildren = unknownElement.children();
        assertTrue(subElementChildren.isEmpty());
        Element anotherElement = (Element) children.get(1);
        assertEquals("anotherElement", anotherElement.tagName());
        List<Attribute> anotherElementAttributes = anotherElement.attributes();
        assertEquals(1, anotherElementAttributes.size());
        Attribute attr2 = anotherElementAttributes.get(0);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
        List<Node> anotherElementChildren = anotherElement.children();
        assertTrue(anotherElementChildren.isEmpty());
    }

    @Test
    public void testXmlTreeBuilderParseWithCdata() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlStringWithCdata = "<root><element><![CDATA[This is some CDATA]]><anotherElement attr2='value2'/></element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xmlStringWithCdata), "http://test.com");
        Element root = (Element) document.body();
        assertEquals("root", root.tagName());
        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        List<Node> children = element.children();
        assertEquals(2, children.size());
        CDataNode cdataNode = (CDataNode) children.get(0);
        assertEquals("This is some CDATA", cdataNode.getData());
        Element anotherElement = (Element) children.get(1);
        assertEquals("anotherElement", anotherElement.tagName());
        List<Attribute> anotherElementAttributes = anotherElement.attributes();
        assertEquals(1, anotherElementAttributes.size());
        Attribute attr2 = anotherElementAttributes.get(0);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
        List<Node> anotherElementChildren = anotherElement.children();
        assertTrue(anotherElementChildren.isEmpty());
    }

    @Test
    public void testXmlTreeBuilderParseWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlStringWithDoctype = "<!DOCTYPE root [ <!ENTITY ent SYSTEM \"file:///path/to/file\"]]><root></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xmlStringWithDoctype), "http://test.com");
        DocumentType doctypeNode = document.doctype();
        assertEquals("root", doctypeNode.getName());
        assertEquals("file:///path/to/file", doctypeNode.getSystemIdentifier());
        Element root = (Element) document.body();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertTrue(children.isEmpty());
    }
}