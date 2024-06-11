
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document jsoupDocument = Jsoup.parse("<html><head></head><body><div>Test</div></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        Document jsoupDocument = Jsoup.parse("<html><head></head><body><div>Test</div></body></html>");
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDocument, w3cDocument);

        assertNotNull(w3cDocument);
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        assertEquals("div", w3cDocument.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("Test", w3cDocument.getDocumentElement().getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderHead() {
        Document jsoupDocument = Jsoup.parse("<html><head></head><body><div>Test</div></body></html>");
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.head(jsoupDocument, 0);

        assertNotNull(w3cDocument);
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        assertEquals("div", w3cDocument.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("Test", w3cDocument.getDocumentElement().getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderTail() {
        Document jsoupDocument = Jsoup.parse("<html><head></head><body><div>Test</div></body></html>");
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.tail(jsoupDocument, 0);

        assertNotNull(w3cDocument);
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        Document jsoupDocument = Jsoup.parse("<html><head></head><body><div>Test</div></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        String w3cString = w3cDom.asString(w3cDocument);

        assertNotNull(w3cString);
        assertTrue(w3cString.contains("<html>"));
        assertTrue(w3cString.contains("<div>Test</div>"));
    }
}
