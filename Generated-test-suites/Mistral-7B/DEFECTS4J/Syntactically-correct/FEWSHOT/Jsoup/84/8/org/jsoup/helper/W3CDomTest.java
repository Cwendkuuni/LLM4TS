package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.junit.Test;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertNotNull(w3cDocument.getDocumentElement());
        assertEquals("Test", w3cDocument.getDocumentElement().getTagName());
        Element titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        assertEquals("Test", titleElement.getTextContent());
        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        assertEquals("Hello World", bodyElement.getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cString = new W3CDom().asString(w3cDocument);

        String expected = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        assertEquals(expected, w3cString);
    }
}