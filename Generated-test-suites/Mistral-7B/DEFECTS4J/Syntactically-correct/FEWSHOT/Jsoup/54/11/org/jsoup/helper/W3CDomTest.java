package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");

        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        Element head = w3cDocument.getDocumentElement().getElementsByTagName("head").item(0);
        assertEquals("head", head.getTagName());
        Element title = head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());

        Element body = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());
        Element p = body.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");

        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        Element head = w3cDocument.getDocumentElement().getElementsByTagName("head").item(0);
        assertEquals("head", head.getTagName());
        Element title = head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());

        Element body = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());
        Element p = body.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element root = w3cDocument.createElement("html");
        root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);

        Element head = w3cDocument.createElement("head");
        root.appendChild(head);

        Element title = w3cDocument.createElement("title");
        title.setTextContent("Test");
        head.appendChild(title);

        Element body = w3cDocument.createElement("body");
        root.appendChild(body);

        Element p = w3cDocument.createElement("p");
        p.setTextContent("Hello World");
        body.appendChild(p);

        String w3cString = new W3CDom().asString(w3cDocument);

        String expected = "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><title>Test</title></head>" +
                "<body><p>Hello World</p></body>" +
                "</html>";

        assertEquals(expected, w3cString);
    }
}