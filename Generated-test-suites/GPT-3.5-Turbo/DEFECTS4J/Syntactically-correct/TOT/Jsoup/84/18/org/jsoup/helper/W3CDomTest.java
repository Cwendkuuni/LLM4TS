package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class W3CDomTest {
    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_validDocument() {
        org.jsoup.nodes.Document jsoupDoc = createValidJsoupDocument();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullDocument() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testFromJsoup_emptyDocument() {
        org.jsoup.nodes.Document jsoupDoc = createEmptyJsoupDocument();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertTrue(w3cDoc.getChildNodes().getLength() == 0);
    }

    @Test
    public void testConvert_validDocument() {
        org.jsoup.nodes.Document jsoupDoc = createValidJsoupDocument();
        Document w3cDoc = createW3CDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("http://example.com", w3cDoc.getDocumentURI());
        Assert.assertEquals(1, w3cDoc.getChildNodes().getLength());

        NodeList nodeList = w3cDoc.getChildNodes().item(0).getChildNodes();
        Node node = nodeList.item(0);
        Assert.assertEquals(Node.ELEMENT_NODE, node.getNodeType());
        Assert.assertEquals("div", node.getNodeName());
        Assert.assertEquals("testDiv", ((Element)node).getAttribute("id"));
        Assert.assertEquals("Example Div Content", node.getTextContent().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullJsoupDocument() {
        Document w3cDoc = createW3CDocument();
        w3CDom.convert(null, w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = createValidJsoupDocument();
        w3CDom.convert(jsoupDoc, null);
    }

    @Test
    public void testConvert_emptyJsoupDocument() {
        org.jsoup.nodes.Document jsoupDoc = createEmptyJsoupDocument();
        Document w3cDoc = createW3CDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("", w3cDoc.getDocumentURI());
        Assert.assertTrue(w3cDoc.getChildNodes().getLength() == 0);
    }

    @Test
    public void testAsString_validDocument() {
        Document w3cDoc = createW3CDocument();
        String xmlString = w3CDom.asString(w3cDoc);

        Assert.assertNotNull(xmlString);
        Assert.assertTrue(xmlString.contains("<div id=\"testDiv\">Example Div Content</div>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullDocument() {
        w3CDom.asString(null);
    }

    @Test
    public void testAsString_emptyDocument() {
        Document w3cDoc = createEmptyW3CDocument();
        String xmlString = w3CDom.asString(w3cDoc);

        Assert.assertNotNull(xmlString);
        Assert.assertEquals("", xmlString);
    }

    private org.jsoup.nodes.Document createValidJsoupDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");

        Element root = new Element("#root");
        jsoupDoc.appendChild(root);

        Element div = new Element("div");
        div.attr("id", "testDiv");
        div.text("Example Div Content");
        root.appendChild(div);

        return jsoupDoc;
    }

    private org.jsoup.nodes.Document createEmptyJsoupDocument() {
        return new org.jsoup.nodes.Document("");
    }

    private Document createW3CDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Document createEmptyW3CDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}