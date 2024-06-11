package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String jsoupHtml = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(jsoupHtml);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);

        Element root = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());

        Node titleNode = root.getElementsByTagName("title").item(0);
        Assert.assertNotNull(titleNode);
        Assert.assertEquals("Test", titleNode.getTextContent());

        Node bodyNode = root.getElementsByTagName("body").item(0);
        Assert.assertNotNull(bodyNode);

        NodeList pNodes = bodyNode.getElementsByTagName("p");
        Assert.assertEquals(1, pNodes.getLength());
        Node pNode = pNodes.item(0);
        Assert.assertEquals("Hello World", pNode.getTextContent());
    }

    @Test
    public void testConvert() {
        String jsoupHtml = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(jsoupHtml);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Document convertedDoc = new W3CDom();
        convertedDoc.convert(jsoupDoc, w3cDoc);

        Assert.assertSame(w3cDoc, convertedDoc);
    }

    @Test
    public void testAsString() {
        String jsoupHtml = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(jsoupHtml);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        String w3cString = new W3CDom().asString(w3cDoc);

        String expected = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Assert.assertEquals(expected, w3cString);
    }

    @Test
    public void testNamespaces() {
        String xmlnsPrefix = "xmlns";
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String htmlWithXmlns = "<html xmlns:%s=\"%s\"> <body><p xmlns:x=\"http://example.com/\">Hello World</p></body></html>";
        String html = String.format(htmlWithXmlns, xmlnsPrefix, xmlns);

        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.convert(jsoupDoc.child(0), w3cDoc.getDocumentElement());

        Map<String, String> expectedNamespaces = new HashMap<>();
        expectedNamespaces.put(xmlnsPrefix, xmlns);
        expectedNamespaces.put("x", "http://example.com/");
        Map<String, String> actualNamespaces = builder.namespaces;

        Assert.assertEquals(expectedNamespaces, actualNamespaces);
    }
}