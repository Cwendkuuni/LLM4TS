package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Sample Document</title></head><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Sample Document</title></head><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            Assert.assertNotNull(w3cDoc);
        } catch (Exception e) {
            Assert.fail("Exception occurred while converting jsoup document to W3C document: " + e.getMessage());
        }
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        org.jsoup.nodes.Node source = sourceEl;
        int depth = 0;
        w3cBuilder.head(source, depth);
        Assert.assertNotNull(w3cBuilder);
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        org.jsoup.nodes.Node source = sourceEl;
        int depth = 0;
        w3cBuilder.tail(source, depth);
        Assert.assertNotNull(w3cBuilder);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("div");
        Element el = w3cBuilder.doc.createElement("div");
        w3cBuilder.copyAttributes(source, el);
        Assert.assertEquals(0, el.getAttributes().getLength());
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        String prefix = w3cBuilder.updateNamespaces(el);
        Assert.assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            String w3cString = w3cDom.asString(w3cDoc);
            Assert.assertNotNull(w3cString);
        } catch (Exception e) {
            Assert.fail("Exception occurred while converting W3C document to string: " + e.getMessage());
        }
    }
}