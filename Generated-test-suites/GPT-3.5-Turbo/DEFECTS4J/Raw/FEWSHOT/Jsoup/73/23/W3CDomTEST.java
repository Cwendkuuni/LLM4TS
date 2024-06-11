package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.Attr;


public class W3CDomTest {
    
    W3CDom w3cDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location("http://example.com");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendElement("html").appendElement("head").appendElement("title").text("Test");
        jsoupDoc.appendElement("body");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        Element root = w3cDoc.getDocumentElement();
        assertEquals("html", root.getNodeName());
        
        Element head = (Element) root.getElementsByTagName("head").item(0);
        assertEquals("head", head.getNodeName());
        
        Element title = (Element) head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getNodeName());
        Text titleText = (Text) title.getFirstChild();
        assertEquals("Test", titleText.getData());
        
        Element body = (Element) root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getNodeName());
    }

    @Test
    public void testW3CBuilder_head_ElementNode() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        
        builder.head(jsoupElement, 0);
        
        Element w3cElement = (Element) w3cDoc.getFirstChild();
        assertEquals("div", w3cElement.getNodeName());
    }

    @Test
    public void testW3CBuilder_head_TextNode() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Test", "");
        
        builder.head(jsoupTextNode, 0);
        
        Text w3cText = (Text) w3cDoc.getFirstChild();
        assertEquals("Test", w3cText.getData());
    }

    @Test
    public void testW3CBuilder_head_CommentNode() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Comment jsoupCommentNode = new org.jsoup.nodes.Comment("Test");
        
        builder.head(jsoupCommentNode, 0);
        
        Comment w3cComment = (Comment) w3cDoc.getFirstChild();
        assertEquals("Test", w3cComment.getData());
    }

    @Test
    public void testW3CBuilder_head_DataNode() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("Test", "");
        
        builder.head(jsoupDataNode, 0);
        
        Text w3cText = (Text) w3cDoc.getFirstChild();
        assertEquals("Test", w3cText.getData());
    }

    @Test
    public void testW3CBuilder_tail() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        builder.head(jsoupElement, 0);
        
        builder.tail(jsoupElement, 0);
        assertNull(builder.dest);
    }

    @Test
    public void testW3CBuilder_copyAttributes() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupElement.attr("id", "test");
        
        Element w3cElement = w3cDoc.createElement("div");
        builder.copyAttributes(jsoupElement, w3cElement);
        
        Attr w3cAttr = w3cElement.getAttributeNode("id");
        assertEquals("test", w3cAttr.getValue());
    }

    @Test
    public void testW3CBuilder_updateNamespaces() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupElement.attr("xmlns", "http://www.w3.org/1999/xhtml");
        
        String prefix = builder.updateNamespaces(jsoupElement);
        
        assertEquals("", prefix);
        assertEquals("http://www.w3.org/1999/xhtml", builder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        String w3cDocString = w3cDom.asString(w3cDoc);
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html></html>", w3cDocString);
    }
}