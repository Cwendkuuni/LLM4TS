package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        String jsoupHtml = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(jsoupHtml);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("http://localhost/Test", w3cDocument.getDocumentURI().toString());

        Element html = w3cDocument.getDocumentElement();
        assertEquals("html", html.getTagName());

        Element head = getChildElement(html, "head");
        assertEquals("head", head.getTagName());

        Element title = getChildElement(head, "title");
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());

        Element body = getChildElement(html, "body");
        assertEquals("body", body.getTagName());

        Element p = getChildElement(body, "p");
        assertEquals("p", p.getTagName());
        assertEquals("Hello World!", p.getTextContent());
    }

    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        String jsoupHtml = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(jsoupHtml);
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertEquals("http://localhost/Test", w3cDocument.getDocumentURI().toString());

        Element html = w3cDocument.getDocumentElement();
        assertEquals("html", html.getTagName());

        Element head = getChildElement(html, "head");
        assertEquals("head", head.getTagName());

        Element title = getChildElement(head, "title");
        assertEquals("title", title.getTagName());
        assertEquals("Test", title.getTextContent());

        Element body = getChildElement(html, "body");
        assertEquals("body", body.getTagName());

        Element p = getChildElement(body, "p");
        assertEquals("p", p.getTagName());
        assertEquals("Hello World!", p.getTextContent());
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        String jsoupHtml = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(jsoupHtml);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cString = new W3CDom().asString(w3cDocument);

        String expected = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        assertEquals(expected, w3cString);
    }

    private Element getChildElement(Element parent, String tagName) {
        NodeList nodeList = parent.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getTagName().equals(tagName)) {
                return (Element) node;
            }
        }
        return null;
    }
}