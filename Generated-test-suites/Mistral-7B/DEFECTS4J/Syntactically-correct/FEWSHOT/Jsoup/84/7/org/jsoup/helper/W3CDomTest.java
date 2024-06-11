package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.processing.NSResolver;
import org.w3c.dom.processing.Processor;

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
import java.util.Map;

public class W3CDomTest {
    @Test
    public void testFromJsoup() throws Exception {
        String html = "<html xmlns:foo=\"http://example.com\" xmlns:bar=\"http://example.org\"><head><title>Test</title></head><body><div id=\"foo\">Hello World!</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertEquals("http://example.com", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("foo", w3cDoc.getDocumentElement().getPrefix("http://example.com"));
        assertEquals("http://example.org", w3cDoc.getDocumentElement().getNamespaceURI("http://example.org"));
        assertEquals("bar", w3cDoc.getDocumentElement().getPrefix("http://example.org"));

        assertNotNull(w3cDoc.getDocumentURI());
        assertEquals("file:/test.html", w3cDoc.getDocumentURI().toString());

        assertSame(w3cDoc.getDocumentElement(), w3cDoc.getDocument().getDocumentElement());
    }

    @Test
    public void testConvert() throws Exception {
        String html = "<html xmlns:foo=\"http://example.com\" xmlns:bar=\"http://example.org\"><head><title>Test</title></head><body><div id=\"foo\">Hello World!</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element root = w3cDoc.getDocumentElement();
        assertEquals(6, root.getChildNodes().getLength());

        Element htmlEl = (Element) root.getChildNodes().item(0);
        assertEquals("html", htmlEl.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getNamespaceURI());
        assertEquals("", htmlEl.getPrefix());
        assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getLocalName());

        Element headEl = (Element) root.getChildNodes().item(1);
        assertEquals("head", headEl.getTagName());
        assertEquals("", headEl.getPrefix());
        assertEquals("head", headEl.getLocalName());

        Element titleEl = (Element) headEl.getChildNodes().item(0);
        assertEquals("title", titleEl.getTagName());
        assertEquals("", titleEl.getPrefix());
        assertEquals("title", titleEl.getLocalName());
        assertEquals("Test", titleEl.getTextContent());

        Element bodyEl = (Element) root.getChildNodes().item(2);
        assertEquals("body", bodyEl.getTagName());
        assertEquals("", bodyEl.getPrefix());
        assertEquals("body", bodyEl.getLocalName());

        Element divEl = (Element) bodyEl.getChildNodes().item(0);
        assertEquals("div", divEl.getTagName());
        assertEquals("foo", divEl.getPrefix());
        assertEquals("div", divEl.getLocalName());
        assertEquals("Hello World!", divEl.getTextContent());

        assertEquals(1, divEl.getAttributeCount());
        assertEquals("foo", divEl.getAttributeNode("id").getPrefix());
        assertEquals("id", divEl.getAttributeNode("id").getLocalName());
        assertEquals("foo", divEl.getAttributeNode("id").getNodeValue());
    }

    @Test
    public void testAsString() throws Exception {
        String html = "<html xmlns:foo=\"http://example.com\" xmlns:bar=\"http://example.org\"><head><title>Test</title></head><body><div id=\"foo\">Hello World!</div></body></html>";
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.StringReader(html));
        W3CDom w3cdom = new W3CDom();
        w3cdom.convert(w3cDoc, w3cDoc);
        String result = w3cdom.asString(w3cDoc);

        Processor processor = new Processor();
        Document parsed = processor.parseFromString(result);
        assertEquals(w3cDoc.getDocumentElement(), parsed.getDocumentElement());
    }
}