
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void fromJsoup_validDocument_returnsW3CDocument() {
        // Arrange
        Document jsoupDoc = Jsoup.parseBodyFragment("<h1>Hello, world!</h1>");

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsoup_nullDocument_throwsIllegalArgumentException() {
        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(null);
    }

    @Test
    public void convert_validJsoupDocument_convertToW3CDocument() {
        // Arrange
        Document jsoupDoc = Jsoup.parseBodyFragment("<h1>Hello, world!</h1>");
        org.w3c.dom.Document w3cDoc = getNewW3cDocument();

        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("h1", rootElement.getTagName());
        assertEquals("Hello, world!", rootElement.getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void convert_nullJsoupDocument_throwsIllegalArgumentException() {
        // Arrange
        org.w3c.dom.Document w3cDoc = getNewW3cDocument();

        // Act
        w3cDom.convert(null, w3cDoc);
    }

    private org.w3c.dom.Document getNewW3cDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void asString_validW3CDocument_returnsDocumentAsString() {
        // Arrange
        org.w3c.dom.Document w3cDoc = getNewW3cDocument();
        Element rootElement = w3cDoc.createElement("h1");
        w3cDoc.appendChild(rootElement);
        rootElement.setTextContent("Hello, world!");

        // Act
        String result = w3cDom.asString(w3cDoc);

        // Assert
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><h1>Hello, world!</h1>", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void asString_nullW3CDocument_throwsIllegalArgumentException() {
        // Act
        String result = w3cDom.asString(null);
    }
}
