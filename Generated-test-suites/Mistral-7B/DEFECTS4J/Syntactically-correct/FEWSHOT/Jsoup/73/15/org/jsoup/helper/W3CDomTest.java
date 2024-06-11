package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;

public class W3CDomTest {
    private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
    private static final String XMLNS_PREFIX = "xmlns:";

    @Test
    public void testFromJsoup() {
        TextDocument jsoupDoc = new TextDocument("Test document");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.title(), w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvertTextNodes() {
        String html = "<html><body><p>Test document</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        Text textNode = (Text) jsoupDoc.selectFirst("p text()");
        Text w3cTextNode = (Text) w3cDoc.getElementsByTagName("p").item(0).getFirstChild();
        assertEquals(textNode.text(), w3cTextNode.getData());
    }

    @Test
    public void testConvertEmptyTextNodes() {
        String html = "<html><body><p></p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        Text textNode = (Text) jsoupDoc.selectFirst("p text()");
        Text w3cTextNode = (Text) w3cDoc.getElementsByTagName("p").item(0).getFirstChild();
        assertNull(textNode.text());
        assertNull(w3cTextNode.getData());
    }

    @Test
    public void testConvertCommentNodes() {
        String html = "<html><!--test comment--></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        Comment commentNode = (Comment) jsoupDoc.selectFirst("html comment()");
        Comment w3cCommentNode = (Comment) w3cDoc.getElementsByTagName("html").item(0).getFirstChild();
        assertEquals(commentNode.data(), w3cCommentNode.getData());
    }

    @Test
    public void testConvertElementNodes() {
        String html = "<html><body><p><span xmlns:foo='bar'>Test</span></p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        Element pElement = (Element) jsoupDoc.selectFirst("p");
        Element w3cPElement = (Element) w3cDoc.getElementsByTagName("p").item(0);
        assertEquals(pElement.tagName(), w3cPElement.getTagName());

        Element spanElement = (Element) pElement.getElementsByTagName("span").item(0);
        Element w3cSpanElement = (Element) w3cPElement.getElementsByTagNameNS(XML_NS, "span").item(0);
        assertEquals(spanElement.tagName(), w3cSpanElement.getTagName());
        assertEquals(spanElement.namespaceInterface(), w3cSpanElement.getNamespaceURI());

        HashMap<String, String> namespaces = new HashMap<>();
        namespaces.put("foo", "bar");
        Element w3cSpanElementWithNamespaces = w3cDoc.createElementNS("bar", "span");
        w3cPElement.appendChild(w3cSpanElementWithNamespaces);
        assertEquals("span", w3cSpanElementWithNamespaces.getTagName());
        assertEquals("bar", w3cSpanElementWithNamespaces.getNamespaceURI());
    }

    @Test
    public void testConvertRootElement() {
        String html = "<html><body><p>Test document</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        String xml = W3CDom.asString(w3cDoc);
        assertTrue(xml.startsWith("<?xml version='1.0'?>"));
        assertTrue(xml.contains("<html"));
        assertTrue(xml.contains("<body"));
        assertTrue(xml.contains("<p>Test document</p>"));
    }
}