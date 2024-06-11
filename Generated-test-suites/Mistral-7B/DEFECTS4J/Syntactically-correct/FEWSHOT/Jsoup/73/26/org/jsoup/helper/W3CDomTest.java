package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element headElement = w3cDocument.getElementsByTagName("head").item(0);
        Element titleElement = headElement.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        Element h1Element = bodyElement.getElementsByTagName("h1").item(0);
        Assert.assertEquals("Hello World", h1Element.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element headElement = w3cDocument.getElementsByTagName("head").item(0);
        Element titleElement = headElement.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        Element h1Element = bodyElement.getElementsByTagName("h1").item(0);
        Assert.assertEquals("Hello World", h1Element.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>"));
        String w3cDocumentString = new W3CDom().asString(w3cDocument);

        Assert.assertNotNull(w3cDocumentString);
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", w3cDocumentString);
    }

    @Test
    public void testHandleAttributes() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html xmlns:my=\"http://example.com\" xmlns:x=\"http://example.org\"><body><div my:id=\"test\" class=\"myClass x:class\" xml:id=\"otherTest\" my:data=\"testData\"></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Element divElement = w3cDocument.getElementsByTagName("div").item(0);

        HashMap<String, String> expectedNamespaces = new HashMap<>();
        expectedNamespaces.put("my", "http://example.com");
        expectedNamespaces.put("x", "http://example.org");
        Assert.assertEquals(expectedNamespaces, divElement.getAttributes().getNamedItemMap());

        Assert.assertEquals("test", divElement.getAttribute("id"));
        Assert.assertEquals("myClass", divElement.getAttribute("class"));
        Assert.assertEquals("testData", divElement.getAttribute("my:data"));
        Assert.assertEquals("otherTest", divElement.getAttribute("xml:id"));
    }

    @Test
    public void testHandleComments() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><!-- Test Comment --><body></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Comment comment = w3cDocument.getElementsByTagName("!--").item(0);
        Assert.assertEquals("Test Comment", comment.getNodeValue());
    }

    @Test
    public void testHandleTextNodes() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><body><div>Test<div>Hello World</div></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Text testTextNode = w3cDocument.getElementsByTagName("div").item(0).getTextContent().split(" ")[0];
        Assert.assertEquals("Test", testTextNode);

        Element helloWorldDiv = w3cDocument.getElementsByTagName("div").item(1);
        Text helloWorldTextNode = helloWorldDiv.getFirstChild();
        Assert.assertEquals("Hello World", helloWorldTextNode.getNodeValue());
    }
}