package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        try {
            String html = "<html><head><title>Test Page</title></head><body><h1>Test</h1></body></html>";
            Document jsoupDoc = Jsoup.parse(html);

            W3CDom w3cDom = new W3CDom();
            org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

            assertNotNull(w3cDoc);
            Element rootElement = w3cDoc.getDocumentElement();
            assertEquals("html", rootElement.getTagName());
            NodeList headElements = rootElement.getElementsByTagName("head");
            NodeList bodyElements = rootElement.getElementsByTagName("body");
            assertEquals(1, headElements.getLength());
            assertEquals(1, bodyElements.getLength());
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testFromJsoup_NullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.fromJsoup(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testConvert() {
        try {
            String html = "<html><head><title>Test Page</title></head><body><h1>Test</h1></body></html>";
            Document jsoupDoc = Jsoup.parse(html);

            W3CDom w3cDom = new W3CDom();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document w3cDoc = docBuilderFactory.newDocumentBuilder().newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);

            assertNotNull(w3cDoc);
            Element rootElement = w3cDoc.getDocumentElement();
            assertEquals("html", rootElement.getTagName());
            NodeList headElements = rootElement.getElementsByTagName("head");
            NodeList bodyElements = rootElement.getElementsByTagName("body");
            assertEquals(1, headElements.getLength());
            assertEquals(1, bodyElements.getLength());
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testConvert_NullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.convert(null, null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (ParserConfigurationException e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testAsString() {
        try {
            String html = "<html><head><title>Test Page</title></head><body><h1>Test</h1></body></html>";
            Document jsoupDoc = Jsoup.parse(html);

            W3CDom w3cDom = new W3CDom();
            org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

            String result = w3cDom.asString(w3cDoc);

            assertNotNull(result);
            assertTrue(result.contains("<html>"));
            assertTrue(result.contains("<head>"));
            assertTrue(result.contains("<title>Test Page</title>"));
            assertTrue(result.contains("<body>"));
            assertTrue(result.contains("<h1>Test</h1>"));
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testAsString_NullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.asString(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testHead_NullParent() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document w3cDoc = docBuilderFactory.newDocumentBuilder().newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

            org.jsoup.nodes.Element headElement = new org.jsoup.nodes.Element("head");
            builder.head(headElement, 0);

            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            assertNotNull(rootElement);
            assertEquals("head", rootElement.getTagName());
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testHead_NullElement() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document w3cDoc = docBuilderFactory.newDocumentBuilder().newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

            org.jsoup.nodes.Element headElement = null;
            builder.head(headElement, 0);

            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            assertNull(rootElement);
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testTail_LocalParent() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document w3cDoc = docBuilderFactory.newDocumentBuilder().newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

            org.jsoup.nodes.Element headElement = new org.jsoup.nodes.Element("head");
            builder.head(headElement, 0);
            builder.tail(headElement, 0);

            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            assertNotNull(rootElement);
            assertEquals("html", rootElement.getTagName());
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testTail_LocalParentWithAttributes() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document w3cDoc = docBuilderFactory.newDocumentBuilder().newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

            org.jsoup.nodes.Element headElement = new org.jsoup.nodes.Element("head");
            builder.head(headElement, 0);
            builder.tail(headElement, 0);

            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            assertNotNull(rootElement);
            assertEquals("html", rootElement.getTagName());
            assertNull(rootElement.getAttribute("xmlns"));
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testTail_NoParent() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document w3cDoc = docBuilderFactory.newDocumentBuilder().newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

            org.jsoup.nodes.Element headElement = new org.jsoup.nodes.Element("head");
            builder.head(headElement, 0);
            builder.tail(headElement, 0);
            builder.tail(null, 0);

            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            assertNull(rootElement);
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

}