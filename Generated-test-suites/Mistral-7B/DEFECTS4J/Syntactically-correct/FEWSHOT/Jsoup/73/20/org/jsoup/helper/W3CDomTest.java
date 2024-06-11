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
import javax.xml.xpath.XPathFactory;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head></head><body><div id='container'>Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        String xmlString = new W3CDom().asString(w3cDocument);
        Assert.assertNotNull(xmlString);

        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.StringReader(xmlString));
        String actualId = xmlDocument.getElementsByTagName("div").item(0).getAttribute("id");
        Assert.assertEquals("container", actualId);
    }

    @Test
    public void testConvert() {
        String html = "<html><head><title>Test</title></head><body><div id='container'>Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        String xmlString = new W3CDom().asString(w3cDocument);
        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.StringReader(xmlString));

        // Test title tag
        Assert.assertEquals(1, xmlDocument.getElementsByTagName("title").getLength());
        Element titleElement = (Element) xmlDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", titleElement.getTextContent());

        // Test div tag
        Assert.assertEquals(1, xmlDocument.getElementsByTagName("div").getLength());
        Element divElement = (Element) xmlDocument.getElementsByTagName("div").item(0);
        Assert.assertEquals("Hello World", divElement.getTextContent());

        // Test id attribute
        Assert.assertEquals("container", divElement.getAttribute("id"));

        // Test comment
        Comment comment = jsoupDocument.body().child(1);
        Element commentElement = (Element) xmlDocument.getElementsByTagName("comment").item(0);
        Assert.assertEquals(comment.data(), commentElement.getTextContent());

        // Test data node
        org.jsoup.nodes.DataNode dataNode = jsoupDocument.body().child(2);
        Element dataElement = (Element) xmlDocument.getElementsByTagName("text").item(2);
        Assert.assertEquals(dataNode.data(), dataElement.getTextContent());
    }

    @Test
    public void testAsString() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild(document.createComment("This is a comment"));
        document.appendChild(document.createElement("div"));
        document.appendChild(document.createTextNode("Hello World"));

        String xmlString = new W3CDom().asString(document);
        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.StringReader(xmlString));

        Assert.assertEquals("This is a comment", xmlDocument.getElementsByTagName("comment").item(0).getTextContent());
        Assert.assertEquals("Hello World", xmlDocument.getElementsByTagName("text").item(2).getTextContent());
    }
}