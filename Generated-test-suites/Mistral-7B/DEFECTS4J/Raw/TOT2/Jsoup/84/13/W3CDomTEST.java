package org.example.junit4;

import org.example.W3CDom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        // Perform assertions on w3cDocument
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Perform assertions on w3cDocument
    }

    @Test
    public void testAsString() {
        Document document = DocumentBuilderFactory.newInstance().newDocument();
        document.appendChild(document.createElement("html"));
        document.getDocumentElement().appendChild(document.createElement("body"));
        document.getDocumentElement().getElementsByTagName("body").getItem(0).appendChild(document.createTextNode("Test"));

        String expectedResult = "<html><body>Test</body></html>";
        String w3cString = new W3CDom().asString(document);

        assertEquals(expectedResult, w3cString);
    }

    @Test
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertNullInDocument() {
        Document w3cDocument = new Document();
        new W3CDom().convert(null, w3cDocument);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertNullOutDocument() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        new W3CDom().convert(jsoupDocument, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAsStringNullInput() {
        new W3CDom().asString(null);
    }
}