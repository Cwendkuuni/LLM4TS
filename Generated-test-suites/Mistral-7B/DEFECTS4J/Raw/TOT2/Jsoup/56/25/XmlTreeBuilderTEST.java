package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        String xmlInput = "<root><element attr=\"value\"></element></root>";
        Document xmlDoc = Jsoup.parse(xmlInput, ParseSettings.preserveCase);
        assertNotNull(xmlDoc.rootElement());
    }

    @Test
    public void testParseMethod() {
        String xmlInput = "<root><element attr=\"value\"></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
        assertNotNull(xmlDoc.rootElement());
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><element attr=\"value\"></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        assertNotNull(element);
        assertNotNull(element.attribute("attr"));
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnknownTag() {
        String xmlInput = "<unknownTag />";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<!-- This is a comment -->";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
        List<Node> comments = xmlDoc.select("comment()");
        assertNotNull(comments);
        assertNotNull(comments.get(0).nodeName());
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<root>&#x21;<element attr=\"value\"></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
        Element root = xmlDoc.rootElement();
        List<Node> characters = xmlDoc.select("text()");
        assertNotNull(characters);
        assertNotNull(characters.get(0).nodeValue());
    }

    @Test
    public void testProcessDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\"></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
        DocumentType doctypeNode = xmlDoc.doctype();
        assertNotNull(doctypeNode);
        assertNotNull(doctypeNode.getName());
    }

    @Test
    public void testPopStackToClose() {
        String xmlInput = "<root><element><element></element></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "baseUrl");
        Element root = xmlDoc.rootElement();
        Element element1 = root.child(0);
        Element element2 = element1.child(0);
        XmlTreeBuilder.popStackToClose(new Token.EndTag("element"));
        assertNotNull(root.child(0)); // element1 should not be removed
        assertNull(element1.child(0)); // element2 should be removed
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\"></element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element element = (Element) nodes.get(0);
        assertNotNull(element);
        assertNotNull(element.attribute("attr"));
    }
}