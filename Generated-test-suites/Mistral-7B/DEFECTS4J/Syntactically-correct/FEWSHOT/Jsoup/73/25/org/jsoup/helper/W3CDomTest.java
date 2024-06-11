package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element htmlElement = (Element) w3cDocument.getDocumentElement();
        Element headElement = htmlElement.getElementsByTagName("head").item(0).getValue();
        Element bodyElement = htmlElement.getElementsByTagName("body").item(0).getValue();
        Element pElement = bodyElement.getElementsByTagName("p").item(0).getValue();

        Assert.assertEquals("p", pElement.getTagName());
        Assert.assertEquals("Test", pElement.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element htmlElement = w3cDocument.createElement("html");
        Element headElement = w3cDocument.createElement("head");
        Element titleElement = w3cDocument.createElement("title");
        titleElement.setTextContent("Test");
        headElement.appendChild(titleElement);
        htmlElement.appendChild(headElement);
        Element bodyElement = w3cDocument.createElement("body");
        Element pElement = w3cDocument.createElement("p");
        pElement.setTextContent("Test");
        bodyElement.appendChild(pElement);
        htmlElement.appendChild(bodyElement);
        w3cDocument.appendChild(htmlElement);

        String w3cString = new W3CDom().asString(w3cDocument);
        String jsoupString = org.jsoup.Jsoup.parse(w3cString).body().html();

        Assert.assertEquals(jsoupString, "<p>Test</p>");
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element htmlElement = w3cDocument.createElement("html");
        Element headElement = w3cDocument.createElement("head");
        Element titleElement = w3cDocument.createElement("title");
        titleElement.setTextContent("Test");
        headElement.appendChild(titleElement);
        htmlElement.appendChild(headElement);
        Element bodyElement = w3cDocument.createElement("body");
        Element pElement = w3cDocument.createElement("p");
        pElement.setTextContent("Test");
        bodyElement.appendChild(pElement);
        htmlElement.appendChild(bodyElement);
        w3cDocument.appendChild(htmlElement);

        new W3CDom().convert(w3cDocument, new Document());

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        NodeTraversor.traverse(builder, jsoupDocument.child(0));

        assertEquals("html", builder.dest.getTagName());
        assertEquals("head", builder.dest.getElementsByTagName("head").item(0).getTagName());
        assertEquals("title", builder.dest.getElementsByTagName("title").item(0).getTagName());
        assertEquals("body", builder.dest.getElementsByTagName("body").item(0).getTagName());
        assertEquals("p", builder.dest.getElementsByTagName("p").item(0).getTagName());
    }

    @Test
    public void testComment() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element htmlElement = w3cDocument.createElement("html");
        Comment comment = w3cDocument.createComment("Test");
        htmlElement.appendChild(comment);
        w3cDocument.appendChild(htmlElement);

        new W3CDom().convert(w3cDocument, new Document());

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><!--Test--></html>");
        NodeTraversor.traverse(builder, jsoupDocument.child(0));

        assertEquals("comment", builder.dest.getNodeName());
        assertEquals("Test", builder.dest.getNodeValue());
    }
}