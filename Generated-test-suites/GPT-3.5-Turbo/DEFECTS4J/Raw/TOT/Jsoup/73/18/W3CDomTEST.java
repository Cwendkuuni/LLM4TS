import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class W3CDomTest {
    @Test
    public void testFromJsoup_typicalUseCase() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
        assertEquals("Test Page", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_typicalUseCase() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.w3c.dom.Document convertedW3CDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(convertedW3CDoc);
        Element rootElement = convertedW3CDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
        assertEquals("Test Page", convertedW3CDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", convertedW3CDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert_blankLocation() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.setBaseUri("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.w3c.dom.Document convertedW3CDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(convertedW3CDoc);
        assertEquals("", convertedW3CDoc.getDocumentURI());
    }

    @Test
    public void testConvert_typicalElement() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.w3c.dom.Element convertedElement = (org.w3c.dom.Element) w3cDoc.getElementsByTagName("h1").item(0);
        assertEquals("Hello World!", convertedElement.getTextContent());
    }

    @Test
    public void testConvert_emptyElement() {
        String html = "<html><head><title>Test Page</title></head><body></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.w3c.dom.Element convertedElement = (org.w3c.dom.Element) w3cDoc.getElementsByTagName("body").item(0);
        assertEquals("", convertedElement.getTextContent());
    }

    @Test
    public void testAsString_typicalUseCase() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String w3cDocStr = w3cDom.asString(w3cDoc);

        assertNotNull(w3cDocStr);
        assertTrue(w3cDocStr.contains("<html>"));
        assertTrue(w3cDocStr.contains("<title>Test Page</title>"));
        assertTrue(w3cDocStr.contains("<h1>Hello World!</h1>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(null);
    }
}