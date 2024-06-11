package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getNodeName());

        Element titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Assert.assertEquals(1, bodyElement.getChildNodes().getLength());

        Element pElement = (Element) bodyElement.getChildNodes().item(0);
        Assert.assertEquals("Hello World", pElement.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Document convertedDocument = new W3CDom();
        convertedDocument.convert(jsoupDocument, w3cDocument);

        Assert.assertEquals(w3cDocument, convertedDocument.getDocument());
    }

    @Test
    public void testAsString() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("root");
        root.setTextContent("Hello World");
        document.appendChild(root);

        Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.jsoup.parse(new W3CDom().asString(document)));

        String result = new W3CDom().asString(w3cDocument);

        Assert.assertEquals("<root>Hello World</root>", result);
    }
}