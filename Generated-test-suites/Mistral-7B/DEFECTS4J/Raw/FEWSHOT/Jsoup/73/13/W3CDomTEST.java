package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("<html><body><p>Hello World</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html xmlns:html='http://www.w3.org/1999/xhtml'><html:head><title>Test</title></html:head><html:body><p>Hello World</p></html:body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        Assert.assertEquals("<html xmlns:html='http://www.w3.org/1999/xhtml'><html:head><title>Test</title></html:head><html:body><p>Hello World</p></html:body></html>", asString(w3cDocument));
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html xmlns:html='http://www.w3.org/1999/xhtml'><html:head><title>Test</title></html:head><html:body><p>Hello World <span xmlns:test='http://www.test.com'>Test</span></p></html:body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element html = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", html.getTagName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", html.getNamespaceURI());

        Element head = (Element) html.getElementsByTagNameNS("*", "head").item(0);
        Assert.assertEquals("head", head.getTagName());
        Assert.assertNull(head.getNamespaceURI());

        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("title", title.getTagName());
        Assert.assertNull(title.getNamespaceURI());
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) html.getElementsByTagName("body").item(0);
        Assert.assertEquals("body", body.getTagName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", body.getNamespaceURI());

        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertEquals("p", p.getTagName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", p.getNamespaceURI());
        Assert.assertEquals("Hello World ", p.getTextContent());

        Element span = (Element) p.getElementsByTagNameNS("*", "test").item(0);
        Assert.assertEquals("test", span.getTagName());
        Assert.assertEquals("http://www.test.com", span.getNamespaceURI());
        Assert.assertEquals("Test", span.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new Document();
        org.w3c.dom.Element html = w3cDocument.createElement("html");
        html.setAttribute("xmlns:html", "http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(html);

        org.w3c.dom.Element head = w3cDocument.createElement("head");
        html.appendChild(head);
        org.w3c.dom.Element title = w3cDocument.createElement("title");
        title.setTextContent("Test");
        head.appendChild(title);

        org.w3c.dom.Element body = w3cDocument.createElement("body");
        html.appendChild(body);
        org.w3c.dom.Element p = w3cDocument.createElement("p");
        body.appendChild(p);
        p.setTextContent("Hello World ");

        org.w3c.dom.Element span = w3cDocument.createElementNS("http://www.test.com", "test");
        span.setTextContent("Test");
        p.appendChild(span);

        org.w3c.dom.Comment comment = w3cDocument.createComment("This is a comment");
        body.appendChild(comment);

        Text text = w3cDocument.createTextNode("Some text");
        body.appendChild(text);

        Document w3cDocumentFromString = new W3CDom().fromJsoup(org.jsoup.jsoup.parse(new W3CDom().asString(w3cDocument)));
        Assert.assertEquals(asString(w3cDocumentFromString), asString(w3cDocument));
    }

    private static String asString(Document document) {
        return new W3CDom().asString(document);
    }
}