package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        Assert.assertEquals(jsoupDocument.childNodes().item(0).getNodeName(), w3cDocument.getDocumentElement().getTagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInputs() {
        new W3CDom().convert(null, null);
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:test=\"http://test.com\"><body><h1 xmlns:test=\"http://test.com\" xmlns:another=\"http://another.com\">Test</h1></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        Element root = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());
        Assert.assertNotNull(root.getElementsByTagNameNS("http://test.com", "test").item(0));
        Assert.assertNotNull(root.getElementsByTagNameNS("http://another.com", "another").item(0));
    }

    @Test
    public void testAsStringNullInput() {
        Document w3cDocument = Document.newDocument();
        String result = new W3CDom().asString(w3cDocument);
        Assert.assertNull(result);
    }

    @Test
    public void testAsStringEmptyInput() {
        Document w3cDocument = Document.newDocument();
        w3cDocument.appendChild(w3cDocument.createComment(""));
        String result = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("", result);
    }

    @Test
    public void testAsStringNonEmptyInput() {
        Document w3cDocument = Document.newDocument();
        Element element = w3cDocument.createElement("Test");
        element.setTextContent("Test Content");
        w3cDocument.appendChild(element);
        String result = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("<Test>Test Content</Test>", result);
    }

}