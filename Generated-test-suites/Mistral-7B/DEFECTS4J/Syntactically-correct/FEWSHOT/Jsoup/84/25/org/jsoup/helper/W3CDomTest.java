package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello world", pElement.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello world", pElement.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>"));
        String w3cDocumentString = new W3CDom().asString(w3cDocument);

        Assert.assertNotNull(w3cDocumentString);
        Assert.assertTrue(w3cDocumentString.contains("<html>"));
        Assert.assertTrue(w3cDocumentString.contains("Hello world"));
        Assert.assertTrue(w3cDocumentString.contains("Test"));
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDocument = new Document();
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>");
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello world", pElement.getTextContent());

        // Test W3CBuilder
        new W3CDom().convert(jsoupDocument, w3cDocument, new W3CDom.W3CBuilder() {
            @Override
            public void head(org.jsoup.nodes.Node source, int depth) {
                super.head(source, depth);
                if (source instanceof org.jsoup.nodes.Element) {
                    Element el = (Element) source;
                    el.setAttribute("data-test", "test");
                }
            }
        });

        titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());
        Assert.assertEquals("test", titleElement.getAttribute("data-test"));

        pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello world", pElement.getTextContent());
        Assert.assertEquals("test", pElement.getAttribute("data-test"));
    }
}