package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element root = w3cDocument.getDocumentElement();
        assertEquals("html", root.getTagName());
        Element head = root.getElementsByTagName("head").item(0);
        Element title = head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());
        Element body = root.getElementsByTagName("body").item(0);
        Element h1 = body.getElementsByTagName("h1").item(0);
        assertEquals("h1", h1.getTagName());
        assertEquals("Test", h1.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element root = w3cDocument.createElement("html");
        Element head = w3cDocument.createElement("head");
        Element title = w3cDocument.createElement("title");
        title.setTextContent("Test");
        head.appendChild(title);
        root.appendChild(head);
        Element body = w3cDocument.createElement("body");
        Element h1 = w3cDocument.createElement("h1");
        h1.setTextContent("Test");
        body.appendChild(h1);
        root.appendChild(body);
        w3cDocument.appendChild(root);

        String jsoupString = new W3CDom().asString(w3cDocument);
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(jsoupString);

        Element jsoupRoot = jsoupDocument.rootElement();
        assertEquals("html", jsoupRoot.tagName());
        Element jsoupHead = jsoupRoot.child(0);
        Element jsoupTitle = jsoupHead.child(0);
        assertEquals("title", jsoupTitle.tagName());
        assertEquals("Test", jsoupTitle.text());
        Element jsoupBody = jsoupRoot.child(1);
        Element jsoupH1 = jsoupBody.child(0);
        assertEquals("h1", jsoupH1.tagName());
        assertEquals("Test", jsoupH1.text());
    }
}