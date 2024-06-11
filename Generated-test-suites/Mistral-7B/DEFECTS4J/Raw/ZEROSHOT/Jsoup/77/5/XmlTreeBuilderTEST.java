package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<xml version='1.0'>\n" +
                "<root>\n" +
                "<element attr='value'>\n" +
                "<subElement attr2='value2'/>\n" +
                "</element>\n" +
                "<anotherElement>\n" +
                "<anotherSubElement/>\n" +
                "</anotherElement>\n" +
                "</root>\n";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());
        List<Node> rootChildren = rootElement.children();

        Element rootElementChild1 = (Element) rootChildren.get(0);
        assertEquals("element", rootElementChild1.tagName());
        assertEquals("value", rootElementChild1.attribute("attr"));
        List<Node> elementChildren = rootElementChild1.children();
        Element subElement = (Element) elementChildren.get(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals("value2", subElement.attribute("attr2"));

        Element rootElementChild2 = (Element) rootChildren.get(1);
        assertEquals("anotherElement", rootElementChild2.tagName());
        List<Node> anotherElementChildren = rootElementChild2.children();
        Element anotherSubElement = (Element) anotherElementChildren.get(0);
        assertEquals("anotherSubElement", anotherSubElement.tagName());

        // Test parsing XML declaration
        String xmlDeclarationString = "<?xml version='1.0' encoding='UTF-8'?>";
        Document xmlDeclarationDoc = Jsoup.parse(xmlDeclarationString, "http://test.com", new XmlTreeBuilder().defaultSettings());
        DocumentType documentType = xmlDeclarationDoc.docType();
        assertEquals("xml", documentType.nodeName());
        assertEquals("1.0", documentType.publicId());
        assertEquals("UTF-8", documentType.systemId());
        assertNull(documentType.getSystemIdentifier());
    }

    @Test
    public void testParseFragment() {
        String xmlFragmentString = "<element attr='value'>\n" +
                "<subElement attr2='value2'/>\n" +
                "</element>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document fragmentDoc = xmlTreeBuilder.parse(new StringReader(xmlFragmentString), "http://test.com");
        List<Node> fragmentRootChildren = fragmentDoc.rootElement().children();
        Element fragmentElement = (Element) fragmentRootChildren.get(0);

        Element rootElement = Jsoup.parse(xmlTreeBuilder.parseFragment(xmlFragmentString, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).toString(), "http://test.com").rootElement();
        assertEquals(fragmentElement, rootElement);
    }
}