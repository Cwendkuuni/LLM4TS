
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDocument;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
        jsoupDocument = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello world</h1></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        Element rootElement = w3cDocument.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3CDom.convert(jsoupDocument, w3cDocument);
        Element rootElement = w3cDocument.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        String documentString = w3CDom.asString(w3cDocument);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><h1>Hello world</h1></body></html>", documentString);
    }
}