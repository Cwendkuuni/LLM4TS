package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderParse() {
        String xmlString = "<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        assertEquals("root", xmlDocument.tagName());
        assertFalse(xmlDocument.outputSettings().isSyntheticRoot());

        List<Node> rootChildren = xmlDocument.childNodes();
        assertEquals(1, rootChildren.size());
        Element rootElement = (Element) rootChildren.get(0);
        assertEquals("element", rootElement.tagName());

        List<Node> elementChildren = rootElement.childNodes();
        assertEquals(1, elementChildren.size());
        Element subElement = (Element) elementChildren.get(0);
        assertEquals("subElement", subElement.tagName());
        assertNull(subElement.attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderParseWithNullBaseUri() {
        String xmlString = "<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.parse(new StringReader(xmlString), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderParseWithEmptyBaseUri() {
        String xmlString = "<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.parse(new StringReader(xmlString), "");
    }

    @Test
    public void testXmlTreeBuilderParseFragment() {
        String xmlFragment = "<element attr='value'><subElement/></element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

        Element rootElement = (Element) parsedNodes.get(0);
        assertEquals("element", rootElement.tagName());

        List<Node> elementChildren = rootElement.childNodes();
        assertEquals(1, elementChildren.size());
        Element subElement = (Element) elementChildren.get(0);
        assertEquals("subElement", subElement.tagName());
        assertNull(subElement.attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderParseFragmentWithNullBaseUri() {
        String xmlFragment = "<element attr='value'><subElement/></element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.parseFragment(xmlFragment, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderParseFragmentWithEmptyBaseUri() {
        String xmlFragment = "<element attr='value'><subElement/></element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.parseFragment(xmlFragment, "");
    }
}