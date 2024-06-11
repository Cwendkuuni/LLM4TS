package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("http://localhost/test", w3cDocument.getDocumentURI().toString());

        Element html = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", html.getTagName());

        Element head = (Element) html.getElementsByTagName("head").item(0);
        Assert.assertEquals(1, head.getChildNodes().getLength());
        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) html.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, body.getChildNodes().getLength());
        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello world!", p.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("http://localhost/test", w3cDocument.getDocumentURI().toString());

        Element html = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", html.getTagName());

        Element head = (Element) html.getElementsByTagName("head").item(0);
        Assert.assertEquals(1, head.getChildNodes().getLength());
        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) html.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, body.getChildNodes().getLength());
        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello world!", p.getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cString = new W3CDom().asString(w3cDocument);

        Assert.assertNotNull(w3cString);
        Assert.assertFalse(w3cString.isEmpty());

        org.jsoup.nodes.Document jsoupDocumentFromString = org.jsoup.Jsoup.parse(w3cString);
        Document w3cDocumentFromString = new W3CDom().fromJsoup(jsoupDocumentFromString);
        String w3cStringFromJsoup = new W3CDom().asString(w3cDocumentFromString);

        Assert.assertEquals(w3cString, w3cStringFromJsoup);
    }
}