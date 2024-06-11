package org.jsoup.helper;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

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

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();

            w3cDom.convert(jsoupDoc, w3cDoc);

            assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head></head><body></body></html>";
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String result = w3cDom.asString(w3cDoc);

        assertEquals(expected, result);
    }
}