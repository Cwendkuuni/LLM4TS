package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element1 attr1=\"value1\">Content1</element1><element2 attr2=\"value2\"><subElement attr3=\"value3\"/></element2></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        List<Element> elements = root.children();
        assertTrue(elements.size() == 2);

        Element element1 = elements.get(0);
        assertEquals("element1", element1.tagName());
        assertEquals("value1", element1.attr("attr1"));

        Element element2 = elements.get(1);
        assertEquals("element2", element2.tagName());
        List<Element> subElements = element2.children();
        assertTrue(subElements.size() == 1);
        Element subElement = subElements.get(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals("value3", subElement.attr("attr3"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr1=\"value1\"><subElement attr2=\"value2\"/></element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        List<Element> subElements = element.children();
        assertTrue(subElements.size() == 1);
        Element subElement = subElements.get(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals("value2", subElement.attr("attr2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader(""), "");
    }
}