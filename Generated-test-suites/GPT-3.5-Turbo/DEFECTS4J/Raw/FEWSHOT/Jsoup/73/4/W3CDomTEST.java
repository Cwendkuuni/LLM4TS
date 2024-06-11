package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    private W3CDom w3cDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");

        builder.head(jsoupEl, 0);
        Element w3cEl = (Element) w3cDoc.getChildNodes().item(0);
        assertNotNull(w3cEl);
        assertEquals("tagName", w3cEl.getTagName());

        builder.tail(jsoupEl, 0);
        assertEquals(w3cDoc, w3cEl.getOwnerDocument());
    }

    @Test
    public void testW3CBuilderHead() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        Text jsoupText = new org.jsoup.nodes.TextNode("textNode", "");

        builder.head(jsoupEl, 0);
        Element w3cEl = (Element) w3cDoc.getChildNodes().item(0);
        assertNotNull(w3cEl);
        assertEquals("tagName", w3cEl.getTagName());

        builder.head(jsoupText, 0);
        Text w3cText = (Text) w3cEl.getChildNodes().item(0);
        assertNotNull(w3cText);
        assertEquals("textNode", w3cText.getWholeText());

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("commentData");
        builder.head(jsoupComment, 0);
        Comment w3cComment = (Comment) w3cEl.getChildNodes().item(1);
        assertNotNull(w3cComment);
        assertEquals("commentData", w3cComment.getData());

        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("dataNode", "");
        builder.head(jsoupData, 0);
        Text w3cData = (Text) w3cEl.getChildNodes().item(2);
        assertNotNull(w3cData);
        assertEquals("dataNode", w3cData.getWholeText());
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        builder.head(jsoupEl, 0);

        builder.tail(jsoupEl, 0);
        assertEquals(w3cDoc, w3cEl.getOwnerDocument());
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupEl.attr("attr1", "value1");
        jsoupEl.attr("attr2", "value2");

        Element w3cEl = w3cDoc.createElementNS("", "tagName");
        builder.copyAttributes(jsoupEl, w3cEl);

        assertEquals("value1", w3cEl.getAttribute("attr1"));
        assertEquals("value2", w3cEl.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupEl.attr("xmlns", "namespace");
        jsoupEl.attr("xmlns:prefix", "prefixNamespace");

        assertEquals("prefixNamespace", builder.updateNamespaces(jsoupEl));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        String w3cStr = w3cDom.asString(w3cDoc);
        assertNotNull(w3cStr);
    }
}