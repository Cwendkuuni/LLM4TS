package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
                "<root>\n" +
                "<element attr1=\"value1\" attr2=\"value2\">Content</element>\n" +
                "</root>";

        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());

        Element root = xmlDoc.rootElement();
        Element element = root.child(0);

        assertEquals("xml", xmlDoc.documentElement().nodeName());
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithDoctype() {
        String xmlString = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">\n" +
                "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
                "<root>\n" +
                "<element attr1=\"value1\" attr2=\"value2\">Content</element>\n" +
                "</root>";

        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());

        DocumentType doctype = xmlDoc.doctype();
        assertEquals("xml", doctype.nodeName());
        assertEquals("-//W3C//DTD XML 1.0//EN", doctype.getPublicIdentifier());
        assertEquals("xml.dtd", doctype.getSystemIdentifier());

        Element root = xmlDoc.rootElement();
        Element element = root.child(0);

        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root>\n" +
                "<element attr1=\"value1\" attr2=\"value2\">Content</element>\n" +
                "</root>";

        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();

        Element root = (Element) nodes.get(0);
        Element element = (Element) nodes.get(1);

        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
        assertEquals("Content", element.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInvalidToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(-1, "Invalid Token");
        xmlTreeBuilder.process(token);
    }
}