package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3CDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3CDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3CDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3CDoc);
        assertNotNull(w3CDoc);
    }

    @Test
    public void testW3CBuilderHead() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(doc);

        Element parentElement = doc.createElement("testParentElement");
        Element testElement = doc.createElement("testElement");
        parentElement.appendChild(testElement);

        w3CBuilder.head(parentElement, 1);

        Node testNode = doc.getElementsByTagName("testParentElement").item(0);
        assertEquals(testNode, w3CBuilder.dest);
    }

    @Test
    public void testW3CBuilderTail() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(doc);

        Element parentElement = doc.createElement("testParentElement");
        Element testElement = doc.createElement("testElement");
        parentElement.appendChild(testElement);

        w3CBuilder.head(parentElement, 1);
        w3CBuilder.tail(parentElement, 1);

        Node parentTestNode = doc.getElementsByTagName("testParentElement").item(0);
        assertEquals(parentTestNode, w3CBuilder.dest);
    }

    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3CDoc = w3CDom.fromJsoup(jsoupDoc);
        String result = w3CDom.asString(w3CDoc);
        assertNotNull(result);
    }
}