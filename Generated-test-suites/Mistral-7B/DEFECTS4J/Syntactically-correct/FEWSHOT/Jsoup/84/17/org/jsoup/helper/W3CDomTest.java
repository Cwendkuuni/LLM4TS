package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Node headNode = w3cDocument.getElementsByTagName("head").item(0);
        Assert.assertEquals(1, headNode.getChildNodes().getLength());

        Node bodyNode = w3cDocument.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, bodyNode.getChildNodes().getLength());

        Element h1Element = (Element) bodyNode.getChildNodes().item(0);
        Assert.assertEquals("h1", h1Element.getTagName());
        Assert.assertEquals("Test", h1Element.getTextContent());

        String w3cXml = W3CDom.asString(w3cDocument);
        Assert.assertNotNull(w3cXml);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        W3CDom.convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element headElement = (Element) w3cDocument.getElementsByTagName("head").item(0);
        Assert.assertEquals(1, headElement.getChildNodes().getLength());
        Element titleElement = (Element) headElement.getChildNodes().item(0);
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, bodyElement.getChildNodes().getLength());
        Element h1Element = (Element) bodyElement.getChildNodes().item(0);
        Assert.assertEquals("h1", h1Element.getTagName());
        Assert.assertEquals("Test", h1Element.getTextContent());

        String w3cXml = W3CDom.asString(w3cDocument);
        Assert.assertNotNull(w3cXml);
    }

    @Test
    public void testNamespaces() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><html:div xmlns:html=\"http://www.w3.org/1999/xhtml\">Test</html:div></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        W3CDom.convert(jsoupDocument, w3cDocument);

        Element rootElement = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", rootElement.getTagName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", rootElement.getNamespaceURI());

        Element divElement = (Element) rootElement.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "div").item(0);
        Assert.assertEquals("div", divElement.getTagName());
        Assert.assertEquals("Test", divElement.getTextContent());

        String w3cXml = W3CDom.asString(w3cDocument);
        Assert.assertNotNull(w3cXml);
    }
}