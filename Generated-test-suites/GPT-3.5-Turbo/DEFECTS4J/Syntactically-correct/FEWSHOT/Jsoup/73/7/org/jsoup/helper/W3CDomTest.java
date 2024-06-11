package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;

public class W3CDomTest {
	
    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        // Create a jsoup document
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        
        // Convert jsoup doc to w3c doc
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Test if conversion is successful
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }
    
    @Test
    public void testConvert() throws ParserConfigurationException {
        // Create a jsoup document
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        
        // Create a w3c document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        
        // Convert jsoup doc to w3c doc
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Test if conversion is successful
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }
    
    @Test
    public void testW3CBuilderHead() throws ParserConfigurationException {
        // Create a jsoup element
        String html = "<div id=\"test\">Hello World!</div>";
        Document jsoupDoc = Jsoup.parse(html);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.getElementById("test");
        
        // Create a w3c document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        
        // Create a W3CDom.W3CBuilder
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        // Test the W3CBuiler's head() method
        int depth = 0;
        w3cBuilder.head(jsoupEl, depth);
        
        // Test if the element is created and added to the w3c doc
        Element w3cEl = w3cDoc.getElementById("test");
        assertNotNull(w3cEl);
        assertEquals("div", w3cEl.getTagName());
    }
    
    @Test
    public void testW3CBuilderTail() throws ParserConfigurationException {
        // Create a jsoup element
        String html = "<div id=\"test\">Hello World!</div>";
        Document jsoupDoc = Jsoup.parse(html);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.getElementById("test");
        
        // Create a w3c document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        
        // Create a W3CDom.W3CBuilder
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        // Call the W3CBuilder's head() method
        int depth = 0;
        w3cBuilder.head(jsoupEl, depth);
        
        // Call the W3CBuilder's tail() method
        w3cBuilder.tail(jsoupEl, depth);
        
        // Test if the element is removed from the w3c doc
        assertNull(w3cDoc.getElementById("test"));
    }
    
    @Test
    public void testCopyAttributes() throws ParserConfigurationException {
        // Create a jsoup element with attributes
        String html = "<div id=\"test\" class=\"example\" style=\"color:red;\">Hello World!</div>";
        Document jsoupDoc = Jsoup.parse(html);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.getElementById("test");
        
        // Create a w3c document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        
        // Create a w3c element
        org.w3c.dom.Element w3cEl = w3cDoc.createElement("div");
        
        // Create a W3CDom
        W3CDom w3cDom = new W3CDom();
        
        // Call the copyAttributes() method
        w3cDom.copyAttributes(jsoupEl, w3cEl);
        
        // Test if attributes are copied correctly
        assertEquals("test", w3cEl.getAttribute("id"));
        assertEquals("example", w3cEl.getAttribute("class"));
        assertEquals("color:red;", w3cEl.getAttribute("style"));
    }
    
    @Test
    public void testUpdateNamespaces() throws ParserConfigurationException {
        // Create a jsoup element with namespaces defined
        String html = "<div id=\"test\" xmlns:ns1=\"http://www.example.com\" ns1:attr1=\"test\">Hello World!</div>";
        Document jsoupDoc = Jsoup.parse(html);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.getElementById("test");
        
        // Create a w3c document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        
        // Create a W3CDom.W3CBuilder
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        // Call the updateNamespaces() method
        String prefix = w3cBuilder.updateNamespaces(jsoupEl);
        
        // Test if namespaces are updated correctly
        assertNotNull(w3cBuilder.namespaces);
        assertEquals("ns1", prefix);
        assertEquals("http://www.example.com", w3cBuilder.namespaces.get("ns1"));
    }
    
    @Test
    public void testAsString() throws ParserConfigurationException {
        // Create a w3c document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        
        // Create a w3c element
        Element w3cEl = w3cDoc.createElement("div");
        w3cEl.setTextContent("Hello World!");
        w3cDoc.appendChild(w3cEl);
        
        // Serialize the w3c document to a string
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(w3cDoc);
        
        // Test if serialization is successful
        assertNotNull(result);
        assertEquals("<div>Hello World!</div>", result);
    }
}