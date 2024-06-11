package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.jsoup.parse(html);
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        w3cDoc.setDocumentURI("file:test.html");
        Element htmlEl = w3cDoc.createElement("html");
        Element headEl = w3cDoc.createElement("head");
        Element titleEl = w3cDoc.createElement("title");
        titleEl.setTextContent("Test");
        headEl.appendChild(titleEl);
        htmlEl.appendChild(headEl);
        Element bodyEl = w3cDoc.createElement("body");
        Element h1El = w3cDoc.createElement("h1");
        h1El.setTextContent("Hello World");
        bodyEl.appendChild(h1El);
        htmlEl.appendChild(bodyEl);
        w3cDoc.appendChild(htmlEl);

        String w3cDocString = new W3CDom().asString(w3cDoc);
        String expected = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";

        Assert.assertEquals(expected, w3cDocString);
    }

    @Test
    public void testConvertWithNamespaces() {
        String xml = "<ns:root xmlns:ns=\"http://example.com/\"><ns:child></ns:child></ns:root>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.jsoup.parse(xml);
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element rootEl = w3cDoc.getDocumentElement();
        Assert.assertEquals("http://example.com/", rootEl.getNamespaceURI());
        Assert.assertEquals("ns", rootEl.getPrefix());
        Assert.assertEquals("root", rootEl.getTagName());
        Assert.assertEquals(1, rootEl.getChildNodes().getLength());

        Element childEl = (Element) rootEl.getChildNodes().item(0);
        Assert.assertEquals("http://example.com/", childEl.getNamespaceURI());
        Assert.assertEquals("ns", childEl.getPrefix());
        Assert.assertEquals("child", childEl.getTagName());
    }
}