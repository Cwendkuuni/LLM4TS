
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class W3CDomTest {

    private static final String TEST_HTML = "<html><body><div>Hello, World!</div></body></html>";

    private org.jsoup.nodes.Document jsoupDoc;

    @Test
    public void testFromJsoup() {
        jsoupDoc = Jsoup.parse(TEST_HTML);
        W3CDom domConverter = new W3CDom();
        Document w3cDoc = domConverter.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", rootElement.getTagName());
        Element bodyElement = (Element) rootElement.getChildNodes().item(0);
        Assert.assertEquals("body", bodyElement.getTagName());
        Element divElement = (Element) bodyElement.getChildNodes().item(0);
        Assert.assertEquals("div", divElement.getTagName());
        Assert.assertEquals("Hello, World!", divElement.getTextContent());
    }

    @Test
    public void testConvert() {
        jsoupDoc = Jsoup.parse(TEST_HTML);
        W3CDom domConverter = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            domConverter.convert(jsoupDoc, w3cDoc);
            Assert.assertNotNull(w3cDoc);
            Element rootElement = w3cDoc.getDocumentElement();
            Assert.assertEquals("html", rootElement.getTagName());
            Element bodyElement = (Element) rootElement.getChildNodes().item(0);
            Assert.assertEquals("body", bodyElement.getTagName());
            Element divElement = (Element) bodyElement.getChildNodes().item(0);
            Assert.assertEquals("div", divElement.getTagName());
            Assert.assertEquals("Hello, World!", divElement.getTextContent());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Assert.fail("ParserConfigurationException occurred");
        }
    }

    @Test
    public element testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupElement.attr("class", "container");
        jsoupElement.attr("id", "main");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            Element w3cElement = w3cDoc.createElement("div");
            w3cBuilder.copyAttributes(jsoupElement, w3cElement);

            NamedNodeMap attributes = w3cElement.getAttributes();
            Assert.assertEquals(2, attributes.getLength());

            Node classAttribute = attributes.getNamedItem("class");
            Assert.assertNotNull(classAttribute);
            Assert.assertEquals("container", classAttribute.getTextContent());

            Node idAttribute = attributes.getNamedItem("id");
            Assert.assertNotNull(idAttribute);
            Assert.assertEquals("main", idAttribute.getTextContent());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Assert.fail("ParserConfigurationException occurred");
        }
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupElement.attr("xmlns:test", "http://example.com");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            w3cBuilder.updateNamespaces(jsoupElement);
            Assert.assertEquals(1, w3cBuilder.namespaces.size());
            Assert.assertTrue(w3cBuilder.namespaces.containsKey("test"));
            Assert.assertEquals("http://example.com", w3cBuilder.namespaces.get("test"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Assert.fail("ParserConfigurationException occurred");
        }
    }

    @Test
    public void testAsString() {
        jsoupDoc = Jsoup.parse(TEST_HTML);
        W3CDom domConverter = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            domConverter.convert(jsoupDoc, w3cDoc);
            String w3cDocString = domConverter.asString(w3cDoc);
            Assert.assertNotNull(w3cDocString);
            Assert.assertFalse(w3cDocString.isEmpty());
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head></head><body><div>Hello, World!</div></body></html>", w3cDocString);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Assert.fail("ParserConfigurationException occurred");
        }
    }
}
