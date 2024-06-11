package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='header'>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        assertNotNull(w3cDocument.getDocumentElement().getElementsByTagName("body").item(0));
        assertNotNull(w3cDocument.getDocumentElement().getElementsByTagName("h1").item(0));
        assertEquals("Hello World", w3cDocument.getDocumentElement().getElementsByTagName("h1").item(0).getTextContent());
        assertEquals("header", w3cDocument.getDocumentElement().getElementsByTagName("h1").item(0).getId());
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='header'>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Document newW3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, newW3cDocument);
        assertEquals(w3cDocument.getDocumentElement(), newW3cDocument.getDocumentElement());
    }

    @Test
    public void testAsString() {
        Document document = W3CDomHelper.createDocument();
        Element root = document.createElement("root");
        root.setTextContent("Hello World");
        document.appendChild(root);
        String expected = "<root>Hello World</root>";
        String actual = new W3CDom().asString(document);
        assertEquals(expected, actual);
    }

    @Test
    public void testW3CBuilder() {
        Document document = W3CDomHelper.createDocument();
        Element root = document.createElement("root");
        root.setTextContent("Hello World");
        Element div = document.createElement("div");
        div.setTextContent("Div Content");
        root.appendChild(div);
        NodeVisitor visitor = new W3CDom.W3CBuilder(document);
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='header'>Hello World</h1><div>Div Content</div></body></html>");
        visitor.head(jsoupDocument.body(), 0);
        assertEquals("html", document.getDocumentElement().getTagName());
        assertEquals("root", document.getDocumentElement().getElementsByTagName("root").item(0).getTagName());
        assertEquals("div", document.getDocumentElement().getElementsByTagName("div").item(0).getTagName());
        assertEquals("Hello World", document.getDocumentElement().getElementsByTagName("h1").item(0).getTextContent());
        assertEquals("Div Content", document.getDocumentElement().getElementsByTagName("div").item(0).getTextContent());
    }
}