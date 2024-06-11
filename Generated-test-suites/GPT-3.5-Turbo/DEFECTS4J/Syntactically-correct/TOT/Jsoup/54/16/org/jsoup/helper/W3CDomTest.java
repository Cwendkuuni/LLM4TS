package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
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
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_ValidJsoupDoc_ReturnsW3CDoc() throws ParserConfigurationException {
        // Arrange
        Document jsoupDoc = new Document("https://www.example.com");
        Element root = new Element("html");
        Element body = new Element("body");
        TextNode textNode = new TextNode("Hello World!");
        body.appendChild(textNode);
        root.appendChild(body);
        jsoupDoc.appendChild(root);

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);
        Element w3cRoot = w3cDoc.getDocumentElement();
        assertNotNull(w3cRoot);
        assertEquals("html", w3cRoot.getTagName());
        Element w3cBody = (Element) w3cRoot.getElementsByTagName("body").item(0);
        assertNotNull(w3cBody);
        Text w3cText = (Text) w3cBody.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("Hello World!", w3cText.getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_NullJsoupDoc_ThrowsIllegalStateException() throws ParserConfigurationException {
        // Act
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_ValidJsoupDoc_ConvertsToW3CDoc() throws ParserConfigurationException {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("https://www.example.com");
        org.jsoup.nodes.Element root = new org.jsoup.nodes.Element("html");
        org.jsoup.nodes.Element body = new org.jsoup.nodes.Element("body");
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Hello World!");
        body.appendChild(textNode);
        root.appendChild(body);
        jsoupDoc.appendChild(root);

        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            // Handle exception
        }

        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(w3cDoc);
        org.w3c.dom.Element w3cRoot = w3cDoc.getDocumentElement();
        assertNotNull(w3cRoot);
        assertEquals("html", w3cRoot.getTagName());
        org.w3c.dom.Element w3cBody = (org.w3c.dom.Element) w3cRoot.getElementsByTagName("body").item(0);
        assertNotNull(w3cBody);
        Text w3cText = (Text) w3cBody.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("Hello World!", w3cText.getData());
    }

    @Test
    public void testConvert_EmptyJsoupDoc_ConvertsToEmptyW3CDoc() throws ParserConfigurationException {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("https://www.example.com");
        jsoupDoc.appendChild(new org.jsoup.nodes.Element("html"));

        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            // Handle exception
        }

        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(w3cDoc);
        org.w3c.dom.Element w3cRoot = w3cDoc.getDocumentElement();
        assertNotNull(w3cRoot);
        assertEquals("html", w3cRoot.getTagName());
    }

    @Test
    public void testConvert_NullJsoupDoc_DoesNotConvertToW3CDoc() throws ParserConfigurationException {
        // Arrange
        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            // Handle exception
        }

        // Act
        w3cDom.convert(null, w3cDoc);

        // Assert
        assertNull(w3cDoc.getDocumentElement());
    }

    @Test
    public void testAsString_ValidW3CDoc_ReturnsStringRepresentation() throws ParserConfigurationException {
        // Arrange
        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            // Handle exception
        }

        org.w3c.dom.Element rootElement = w3cDoc.createElement("html");
        org.w3c.dom.Element bodyElement = w3cDoc.createElement("body");
        org.w3c.dom.Text textNode = w3cDoc.createTextNode("Hello World!");
        bodyElement.appendChild(textNode);
        rootElement.appendChild(bodyElement);
        w3cDoc.appendChild(rootElement);

        // Act
        String result = w3cDom.asString(w3cDoc);

        // Assert
        assertNotNull(result);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><body>Hello World!</body></html>", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_NullW3CDoc_ThrowsIllegalStateException() {
        // Act
        w3cDom.asString(null);
    }
}