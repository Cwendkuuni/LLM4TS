package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoupNotNull() {
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNull() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvertLocationNotBlank() {
        jsoupDoc.location("http://www.example.com/");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("http://www.example.com/", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertLocationBlank() {
        jsoupDoc.location("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertRootElement() {
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("html"), "");
        jsoupDoc.appendChild(rootElement);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvertTextNode() {
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Hello World!", "");
        jsoupDoc.appendChild(textNode);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("Hello World!", w3cDoc.getTextContent());
    }

    @Test
    public void testConvertComment() {
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("This is a comment", "");
        jsoupDoc.appendChild(comment);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("This is a comment", w3cDoc.getFirstChild().getNodeValue());
    }

    @Test
    public void testConvertDataNode() {
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("<!--CDATA-->", "");
        jsoupDoc.appendChild(dataNode);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("<!--CDATA-->", w3cDoc.getFirstChild().getNodeValue());
    }

    @Test
    public void testConvertAttribute() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        element.attr("id", "mydiv");
        jsoupDoc.appendChild(element);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("mydiv", w3cDoc.getDocumentElement().getAttribute("id"));
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("p"), "");
        jsoupDoc.appendChild(element);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String output = w3CDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><p></p>", output);
    }
}