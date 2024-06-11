
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1><p>Paragraph</p></body></html>");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1><p>Paragraph</p></body></html>");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);

        assertEquals("html", rootElement.getTagName());
        assertEquals(1, rootElement.getChildNodes().getLength());

        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);

        assertEquals("body", bodyElement.getTagName());
        assertEquals(2, bodyElement.getChildNodes().getLength());

        Element h1Element = (Element) bodyElement.getElementsByTagName("h1").item(0);
        assertNotNull(h1Element);

        assertEquals("h1", h1Element.getTagName());
        assertEquals("Title", h1Element.getTextContent());

        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        assertNotNull(pElement);

        assertEquals("p", pElement.getTagName());
        assertEquals("Paragraph", pElement.getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1><p>Paragraph</p></body></html>");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        String xmlString = w3cDom.asString(w3cDoc);

        assertNotNull(xmlString);

        assertTrue(xmlString.contains("<html>"));
        assertTrue(xmlString.contains("<body>"));
        assertTrue(xmlString.contains("<h1>Title</h1>"));
        assertTrue(xmlString.contains("<p>Paragraph</p>"));
    }

    @Test
    public void testConvertEmptyJsoupDocument() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);

        assertEquals("html", rootElement.getTagName());
        assertEquals(0, rootElement.getChildNodes().getLength());
    }

    @Test
    public void testConvertSingleNodeJsoupDocument() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Title</h1></body></html>");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);

        assertEquals("html", rootElement.getTagName());
        assertEquals(1, rootElement.getChildNodes().getLength());

        Element headElement = (Element) rootElement.getElementsByTagName("head").item(0);
        assertNotNull(headElement);

        assertEquals("head", headElement.getTagName());
        assertEquals(0, headElement.getChildNodes().getLength());

        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);

        assertEquals("body", bodyElement.getTagName());
        assertEquals(1, bodyElement.getChildNodes().getLength());

        Element h1Element = (Element) bodyElement.getElementsByTagName("h1").item(0);
        assertNotNull(h1Element);

        assertEquals("h1", h1Element.getTagName());
        assertEquals("Title", h1Element.getTextContent());
    }
}

