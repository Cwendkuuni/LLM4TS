package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup_NullJsoupDocument_ThrowsException() {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.fromJsoup(null);
            Assert.fail("Expected IllegalStateException but no exception was thrown");
        } catch (IllegalStateException e) {
            assertEquals("jsoup doc must not be null", e.getMessage());
        }
    }

    @Test
    public void testFromJsoup_ValidJsoupDocument_ReturnsW3CDocument() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert_ValidJsoupDocumentAndW3CDocument_ConvertsCorrectly() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.appendText("Some text");
        jsoupDoc.appendChild(jsoupElement);

        w3CDom.convert(jsoupDoc, w3cDoc);

        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("tag", w3cElement.getTagName());

        TextNode w3cTextNode = (TextNode) w3cElement.getFirstChild();
        assertNotNull(w3cTextNode);
        assertEquals("Some text", w3cTextNode.getWholeText());
    }

    @Test
    public void testConvert_BlankLocation_SetsDocumentURIAsBlank() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_NonBlankLocation_SetsDocumentURIAsLocation() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_NullSource_DoesNothing() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        w3CBuilder.head(null, 0);

        assertNull(w3cDoc.getFirstChild());
    }

    @Test
    public void testConvert_ElementSource_CreatesW3cElement() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.attr("attribute", "value");
        jsoupElement.appendChild(new org.jsoup.nodes.TextNode("Some text", ""));
        jsoupDoc.appendChild(jsoupElement);

        w3CBuilder.head(jsoupElement, 0);

        Element w3cElement = (Element) w3cDoc.getFirstChild();
        assertNotNull(w3cElement);
        assertEquals("tag", w3cElement.getTagName());
        assertEquals(1, w3cElement.getAttributes().getLength());
        assertEquals("value", w3cElement.getAttribute("attribute"));

        Text w3cText = (Text) w3cElement.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("Some text", w3cText.getWholeText());
    }

    @Test
    public void testConvert_TextNodeSource_CreatesW3cText() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Some text", "");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.appendChild(jsoupTextNode);
        jsoupDoc.appendChild(jsoupElement);

        w3CBuilder.head(jsoupTextNode, 0);

        Text w3cText = (Text) w3cDoc.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("Some text", w3cText.getWholeText());
    }

    @Test
    public void testConvert_CommentSource_CreatesW3cComment() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("Some comment", "");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.appendChild(jsoupComment);
        jsoupDoc.appendChild(jsoupElement);

        w3CBuilder.head(jsoupComment, 0);

        org.w3c.dom.Comment w3cComment = (org.w3c.dom.Comment) w3cDoc.getFirstChild();
        assertNotNull(w3cComment);
        assertEquals("Some comment", w3cText.getData());
    }

    @Test
    public void testConvert_DataNodeSource_CreatesW3cTextNode() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("Some data", "");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.appendChild(jsoupDataNode);
        jsoupDoc.appendChild(jsoupElement);

        w3CBuilder.head(jsoupDataNode, 0);

        Text w3cText = (Text) w3cDoc.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("Some data", w3cText.getWholeData());
    }

    @Test
    public void testTail_WithElementSourceAndParentElement_Ignored() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        Element parentElement = new org.w3c.dom.ElementImpl(null, null, null);
        w3CBuilder.dest = parentElement;

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        w3CBuilder.tail(jsoupElement, 0);

        assertEquals(parentElement, w3CBuilder.dest);
    }

    @Test
    public void testAsString_ValidW3CDocument_ReturnsDocumentAsString() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(new org.jsoup.nodes.Document(""));
        Element rootElement = w3cDoc.createElement("root");
        w3cDoc.appendChild(rootElement);
        Text textNode = w3cDoc.createTextNode("Some text");
        rootElement.appendChild(textNode);

        String result = w3CDom.asString(w3cDoc);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?><root>Some text</root>", result);
    }

}