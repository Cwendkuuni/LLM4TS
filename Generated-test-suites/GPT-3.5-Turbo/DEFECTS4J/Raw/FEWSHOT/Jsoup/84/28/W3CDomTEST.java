package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class W3CDomTest {
    @Test
    public void testFromJsoup() throws Exception {
        String html = "<html><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        Element element = jsoupDoc.select("p").first();
        Node node = w3cDoc.getElementsByTagName("p").item(0);

        assertNotNull(element);
        assertNotNull(node);
        assertEquals(element.text(), node.getTextContent());
    }

    @Test
    public void testConvert() throws Exception {
        String html = "<html><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Node node = w3cDoc.getElementsByTagName("p").item(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);
        assertEquals("p", ((Element) node).getTagName());
        assertEquals("Hello World", ((Text) node.getFirstChild()).getData());
    }

    @Test
    public void testConvert_withMultipleElements() throws Exception {
        String html = "<html><body><h1>Header 1</h1><h2>Header 2</h2><h3>Header 3</h3></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        NodeList nodeList = w3cDoc.getElementsByTagName("*");
        assertNotNull(nodeList);
        assertEquals(4, nodeList.getLength()); // Including 'html' element

        Node node1 = nodeList.item(1);
        Node node2 = nodeList.item(2);
        Node node3 = nodeList.item(3);

        assertTrue(node1 instanceof Element);
        assertTrue(node2 instanceof Element);
        assertTrue(node3 instanceof Element);

        assertEquals("h1", ((Element) node1).getTagName());
        assertEquals("Header 1", ((Text) node1.getFirstChild()).getData());

        assertEquals("h2", ((Element) node2).getTagName());
        assertEquals("Header 2", ((Text) node2.getFirstChild()).getData());

        assertEquals("h3", ((Element) node3).getTagName());
        assertEquals("Header 3", ((Text) node3.getFirstChild()).getData());
    }

    @Test
    public void testConvert_withAttributes() throws Exception {
        String html = "<html><body><p class=\"red\">Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Node node = w3cDoc.getElementsByTagName("p").item(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);

        Element element = (Element) node;
        assertEquals("red", element.getAttribute("class"));
    }

    @Test
    public void testConvert_withComments() throws Exception {
        String html = "<html><body><!--This is a comment--><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        NodeList nodeList = w3cDoc.getChildNodes();
        Node commentNode = nodeList.item(0);
        Node pTagNode = nodeList.item(1);

        assertNotNull(commentNode);
        assertNotNull(pTagNode);
        assertTrue(commentNode instanceof org.w3c.dom.Comment);
        assertTrue(pTagNode instanceof Element);

        org.w3c.dom.Comment comment = (org.w3c.dom.Comment) commentNode;
        Element pTag = (Element) pTagNode;

        assertEquals("This is a comment", comment.getData());
        assertEquals("p", pTag.getTagName());
        assertEquals("Hello World", ((Text) pTag.getFirstChild()).getData());
    }

    @Test
    public void testConvert_withCDATA() throws Exception {
        String html = "<html><body><![CDATA[<p>Hello World</p>]]></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        NodeList nodeList = w3cDoc.getElementsByTagName("body");
        Node bodyNode = nodeList.item(0);
        assertNotNull(bodyNode);
        assertTrue(nodeList.getLength() > 0);

        NodeList children = bodyNode.getChildNodes();
        assertEquals(1, children.getLength());

        Node child = children.item(0);
        assertTrue(child instanceof Text);
        assertEquals("<p>Hello World</p>", ((Text) child).getTextContent());
    }

    @Test
    public void testConvertibleElements() throws Exception {
        String html = "<html><body><span>1</span> text <span>2</span></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        NodeList nodeList = w3cDoc.getElementsByTagName("body");
        assertNotNull(nodeList);
        assertTrue(nodeList.getLength() > 0);

        Node bodyNode = nodeList.item(0);
        assertEquals("body", bodyNode.getNodeName());

        NodeList children = bodyNode.getChildNodes();
        assertNotNull(children);
        assertEquals(3, children.getLength());

        Node node1 = children.item(0);
        Node node2 = children.item(1);
        Node node3 = children.item(2);

        assertTrue(node1 instanceof Element);
        assertTrue(node2 instanceof Text);
        assertTrue(node3 instanceof Element);

        assertEquals("span", ((Element) node1).getTagName());
        assertEquals("1", ((Text) node1.getFirstChild()).getData());

        assertEquals("#text", node2.getNodeName());
        assertEquals(" text ", ((Text) node2).getData());

        assertEquals("span", ((Element) node3).getTagName());
        assertEquals("2", ((Text) node3.getFirstChild()).getData());
    }

    @Test
    public void testAsString() throws Exception {
        String html = "<p>Hello World</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        String convertedHtml = w3cDom.asString(w3cDoc);
        assertEquals(html, convertedHtml);
    }

    @Test
    public void testDocumentURI() throws Exception {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String uri = "http://www.example.com";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html, uri);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals(uri, w3cDoc.getDocumentURI());
    }
}