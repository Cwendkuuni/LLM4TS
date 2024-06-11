package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private static W3CDom w3cDom;
    private static org.jsoup.nodes.Document jsoupDoc;

    @BeforeClass
    public static void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");

        Element rootElement = new Element("html");
        jsoupDoc.appendChild(rootElement);
        Element bodyElement = new Element("body");
        rootElement.appendChild(bodyElement);
        TextNode textNode = new TextNode("This is a test.");
        bodyElement.appendChild(textNode);
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
        assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertNotNull(w3cDoc);
            assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
            assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        assertTrue(result.contains("<html>"));
        assertTrue(result.contains("<body>"));
        assertTrue(result.contains("This is a test."));
    }
}