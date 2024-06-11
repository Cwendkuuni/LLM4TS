package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
import javax.xml.transform.stream.StreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head><title>Test</title></head><body><div id=\"test\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());

        Element title = (Element) w3cDoc.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());

        Element div = (Element) w3cDoc.getElementsByTagName("div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
    }

    @Test
    public void testConvert() {
        String html = "<html><head><title>Test</title></head><body><div id=\"test\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        Document w3cDocCopy = W3CDom.newInstance();
        W3CDom.convert(jsoupDoc, w3cDocCopy);

        assertNotNull(w3cDocCopy);
        assertEquals(w3cDoc, w3cDocCopy);
    }

    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head><body><div id=\"test\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        String xmlString = W3CDom.asString(w3cDoc);

        assertNotNull(xmlString);
        assertEquals(html, xmlString);
    }

    @Test
    public void testNamespaceAware() {
        String html = "<html xmlns:my=\"http://example.com/\"><head><title>Test</title></head><body><div id=\"test\" xmlns:x=\"http://example.com/\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertEquals("http://example.com/", w3cDoc.getDocumentElement().getNamespaceURI());
        Element div = (Element) w3cDoc.getElementsByTagNameNS("http://example.com/", "div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
    }

    @Test
    public void testEmptyDocument() {
        Document w3cDoc = W3CDom.newInstance();
        String xmlString = W3CDom.asString(w3cDoc);

        assertEquals("", xmlString);
    }

    @Test
    public void testAttributes() {
        String html = "<html><head><title attr=\"test\">Test</title></head><body><div id=\"test\" class=\"test\" tabindex=\"123\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        Element title = (Element) w3cDoc.getElementsByTagName("title").item(0);
        assertNotNull(title);
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());
        assertEquals("test", title.getAttribute("attr"));

        Element div = (Element) w3cDoc.getElementsByTagName("div").item(0);
        assertNotNull(div);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
        assertEquals("test", div.getAttribute("class"));
        assertEquals("123", div.getAttribute("tabindex"));
    }

    @Test
    public void testEmptyElement() {
        Document w3cDoc = W3CDom.newInstance();
        Element emptyElement = w3cDoc.createElement("empty");
        w3cDoc.appendChild(emptyElement);

        assertEquals("empty", emptyElement.getTagName());
        assertEquals(0, emptyElement.getChildNodes().getLength());
    }

    @Test
    public void testTextNodes() {
        String html = "<html><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        Element p = (Element) w3cDoc.getElementsByTagName("p").item(0);
        Text textNode = (Text) p.getFirstChild();
        assertEquals("Hello World", textNode.getData());
    }

    @Test
    public void testComment() {
        String html = "<html><body><p>Hello World<!-- Comment --></p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        Element p = (Element) w3cDoc.getElementsByTagName("p").item(0);
        Comment comment = (Comment) p.getFirstChild();
        assertEquals("Comment", comment.getData());
    }

    @Test
    public void testDataNodes() {
        String html = "<html><body><p data-test=\"Hello World\"></p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        Element p = (Element) w3cDoc.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        Attribute dataAttr = p.getAttributeNodes().item(0);
        assertEquals("data-test", dataAttr.getName());
        assertEquals("Hello World", dataAttr.getValue());
    }

    @Test
    public void testNamespaces() {
        String html = "<html xmlns:m=\"http://example.com/\"><body><m:div id=\"test\">Hello World</m:div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertEquals("http://example.com/", w3cDoc.getDocumentElement().getNamespaceURI() + "/");
        Element div = (Element) w3cDoc.getElementsByTagNameNS("http://example.com/", "div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
    }

    @Test
    public void testNamespacePrefix() {
        String html = "<html xmlns:m=\"http://example.com/\"><body><m:div xmlns:x=\"http://example2.com/\" id=\"test\">Hello World</m:div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertEquals("http://example.com/", w3cDoc.getDocumentElement().getNamespaceURI() + "/");
        assertEquals("http://example2.com/", w3cDoc.getElementsByTagNameNS("*", "x").item(0).getNamespaceURI() + "/");
        Element div = (Element) w3cDoc.getElementsByTagNameNS("http://example.com/", "div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
    }

    @Test
    public void testConversionOfComplexDocument() {
        String html = "<html xmlns:m=\"http://example.com/\"><head><title>Test</title></head><body><m:div xmlns:x=\"http://example2.com/\" id=\"test\">Hello World</m:div><m:script src=\"script.js\"></m:script></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertEquals("http://example.com/", w3cDoc.getDocumentElement().getNamespaceURI() + "/");
        assertEquals("http://example2.com/", w3cDoc.getElementsByTagNameNS("*", "x").item(0).getNamespaceURI() + "/");

        Element div = (Element) w3cDoc.getElementsByTagNameNS("http://example.com/", "div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));

        Element script = (Element) w3cDoc.getElementsByTagNameNS("http://example.com/", "script").item(0);
        assertEquals("script", script.getTagName());
        assertEquals("script.js", script.getAttribute("src"));
    }

    @Test
    public void testSerializedDocument() throws ParserConfigurationException, TransformerException, IOException {
        String html = "<html xmlns:m=\"http://example.com/\"><head><title>Test</title></head><body><m:div xmlns:x=\"http://example2.com/\" id=\"test\">Hello World</m:div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        String w3cString = W3CDom.asString(w3cDoc);
        Document newW3cDoc = W3CDom.fromString(w3cString);

        assertEquals(w3cDoc, newW3cDoc);
    }

    @Test
    public void testEmptyDocumentSerialized() throws ParserConfigurationException, TransformerException, IOException {
        Document w3cDoc = W3CDom.newInstance();
        String w3cString = W3CDom.asString(w3cDoc);
        Document newW3cDoc = W3CDom.fromString(w3cString);

        assertEquals(w3cDoc, newW3cDoc);
    }

    @Test
    public void testDomSerializationAndParsing() throws ParserConfigurationException, TransformerException, IOException {
        String xmlString = "<html xmlns:m=\"http://example.com/\"><head><title>Test</title></head><body><m:div id=\"test\">Hello World</m:div></body></html>";
        Document w3cDoc = W3CDom.fromString(xmlString);
        String w3cString = W3CDom.asString(w3cDoc);

        Document newW3cDoc = W3CDom.fromString(w3cString);

        assertEquals(w3cDoc, newW3cDoc);
    }
}