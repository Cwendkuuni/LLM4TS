package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    W3CDom w3CDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document in = null;
        w3CDom.fromJsoup(in);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);

        // Check elements and text nodes are converted correctly
        Element htmlElement = (Element) w3cDocument.getElementsByTagName("html").item(0);
        Element headElement = (Element) htmlElement.getElementsByTagName("head").item(0);
        Element titleElement = (Element) headElement.getElementsByTagName("title").item(0);
        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Element h1Element = (Element) bodyElement.getElementsByTagName("h1").item(0);

        Assert.assertEquals("html", htmlElement.getTagName());
        Assert.assertEquals("head", headElement.getTagName());
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("body", bodyElement.getTagName());
        Assert.assertEquals("h1", h1Element.getTagName());
        Assert.assertEquals("Hello World", h1Element.getTextContent());

        // Check attributes are converted correctly
        Attribute titleAttr = titleElement.getAttributeNode("title");
        Assert.assertNotNull(titleAttr);
        Assert.assertEquals("Test", titleAttr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInput() {
        Document w3cDocument = null;
        w3CDom.convert(null, w3cDocument);
    }

    @Test
    public void testConvertEmptyInput() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.nodes.Document.newDocument();
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);

        Element rootElement = (Element) w3cDocument.getDocumentElement();
        Assert.assertEquals("", rootElement.getNodeValue());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);

        String w3cString = w3CDom.asString(w3cDocument);
        String expected = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Assert.assertEquals(expected, w3cString);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringNullInput() {
        Document w3cDocument = null;
        w3CDom.asString(w3cDocument);
    }
}