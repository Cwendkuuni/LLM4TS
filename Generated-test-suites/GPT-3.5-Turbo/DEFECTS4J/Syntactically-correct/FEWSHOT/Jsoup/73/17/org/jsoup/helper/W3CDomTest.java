
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() throws ParserConfigurationException, SAXException, IOException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvert() throws ParserConfigurationException, SAXException, IOException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        Document w3cDoc = createEmptyW3CDocument();

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Element bodyElement = (Element) w3cDoc.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);
        Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
        assertNotNull(divElement);
        Element pElement = (Element) divElement.getElementsByTagName("p").item(0);
        assertNotNull(pElement);

        Text textNode = (Text) pElement.getFirstChild();
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.getNodeValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_NullInput_W3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        w3CDom.convert(jsoupDoc, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_NullInput_JsoupDocument() throws ParserConfigurationException {
        Document w3cDoc = createEmptyW3CDocument();
        w3CDom.convert(null, w3cDoc);
    }

    @Test
    public void testConvert_EmptyInput_JsoupDocument() throws ParserConfigurationException {
        Document w3cDoc = createEmptyW3CDocument();
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("");
        w3CDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
        assertNull(w3cDoc.getDocumentElement());
    }

    @Test
    public void testConvert_EmptyInput_W3CDocument() throws ParserConfigurationException, SAXException, IOException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Element bodyElement = (Element) w3cDoc.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);
        Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
        assertNotNull(divElement);
        Element pElement = (Element) divElement.getElementsByTagName("p").item(0);
        assertNotNull(pElement);

        Text textNode = (Text) pElement.getFirstChild();
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.getNodeValue());
    }

    @Test
    public void testConvert_InvalidTagNames() throws ParserConfigurationException, SAXException, IOException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div><div@attr>Invalid Tag</p></div></body></html>");
        Document w3cDoc = createEmptyW3CDocument();

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Element bodyElement = (Element) w3cDoc.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);
        Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
        assertNotNull(divElement);
        assertNull(divElement.getAttribute("div@attr"));
        Element pElement = (Element) divElement.getElementsByTagName("p").item(0);
        assertNull(pElement);
    }

    @Test
    public void testAsString() throws ParserConfigurationException, SAXException, IOException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        Document w3cDoc = createEmptyW3CDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);

        String w3cString = w3CDom.asString(w3cDoc);

        assertNotNull(w3cString);
        assertTrue(w3cString.contains("<body>"));
        assertTrue(w3cString.contains("<p>Hello World</p>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_NullInput() {
        w3CDom.asString(null);
    }

    private Document createEmptyW3CDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
}
