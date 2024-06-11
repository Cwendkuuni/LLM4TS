package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Node headNode = w3cDocument.getElementsByTagName("head").item(0);
        Assert.assertEquals(1, headNode.getChildNodes().getLength());
        Element titleElement = (Element) headNode.getFirstChild();
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("Test", titleElement.getTextContent());

        Node bodyNode = w3cDocument.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, bodyNode.getChildNodes().getLength());
        Element pElement = (Element) bodyNode.getFirstChild();
        Assert.assertEquals("p", pElement.getTagName());
        Assert.assertEquals("Hello World", pElement.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Node headNode = w3cDocument.getElementsByTagName("head").item(0);
        Assert.assertEquals(1, headNode.getChildNodes().getLength());
        Element titleElement = (Element) headNode.getFirstChild();
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("Test", titleElement.getTextContent());

        Node bodyNode = w3cDocument.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, bodyNode.getChildNodes().getLength());
        Element pElement = (Element) bodyNode.getFirstChild();
        Assert.assertEquals("p", pElement.getTagName());
        Assert.assertEquals("Hello World", pElement.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        w3cDocument.appendChild(w3cDocument.createElement("html"));
        Element htmlElement = w3cDocument.getDocumentElement();
        htmlElement.appendChild(htmlElement.createElement("head"));
        htmlElement.appendChild(htmlElement.createElement("body"));

        Element titleElement = w3cDocument.createElement("title");
        titleElement.setTextContent("Test");
        htmlElement.getElementsByTagName("head").item(0).appendChild(titleElement);

        Element pElement = w3cDocument.createElement("p");
        pElement.setTextContent("Hello World");
        htmlElement.getElementsByTagName("body").item(0).appendChild(pElement);

        String w3cDocumentString = new W3CDom().asString(w3cDocument);

        Assert.assertNotNull(w3cDocumentString);
        Assert.assertEquals("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", w3cDocumentString);
    }
}