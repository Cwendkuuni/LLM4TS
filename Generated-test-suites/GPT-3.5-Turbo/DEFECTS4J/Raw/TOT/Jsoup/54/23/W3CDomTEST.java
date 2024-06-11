package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

public class W3CDomTest {
    
    // Test case for fromJsoup method
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Check if the converted document is not null
        assertNotNull(w3cDoc);
        
        // Check if the converted document has the correct doctype
        DocumentType doctype = w3cDoc.getDoctype();
        assertNotNull(doctype);
        assertEquals("HTML", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicId());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemId());
        
        // Check if the converted document has the correct root element
        Element rootEl = w3cDoc.getDocumentElement();
        assertEquals("html", rootEl.getTagName());
        
        // Check if the converted document has the correct title element
        NodeList titleElements = w3cDoc.getElementsByTagName("title");
        assertEquals(1, titleElements.getLength());
        Element title = (Element) titleElements.item(0);
        assertEquals("Test", title.getTextContent());
        
        // Check if the converted document has the correct body element
        NodeList bodyElements = w3cDoc.getElementsByTagName("body");
        assertEquals(1, bodyElements.getLength());
        Element body = (Element) bodyElements.item(0);
        NodeList paragraphs = body.getElementsByTagName("p");
        assertEquals(1, paragraphs.getLength());
        Element paragraph = (Element) paragraphs.item(0);
        assertEquals("Hello World!", paragraph.getTextContent());
    }
    
    // Test case for convert method
    @Test
    public void testConvert() throws IOException {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document w3cDoc = dBuilder.newDocument();

            W3CDom w3cDom = new W3CDom();
            w3cDom.convert(jsoupDoc, w3cDoc);
            
            // Check if the converted document is not null
            assertNotNull(w3cDoc);
            
            // Check if the converted document has the correct doctype
            DocumentType doctype = w3cDoc.getDoctype();
            assertNotNull(doctype);
            assertEquals("HTML", doctype.getName());
            assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicId());
            assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemId());
            
            // Check if the converted document has the correct root element
            Element rootEl = w3cDoc.getDocumentElement();
            assertEquals("html", rootEl.getTagName());
            
            // Check if the converted document has the correct title element
            NodeList titleElements = w3cDoc.getElementsByTagName("title");
            assertEquals(1, titleElements.getLength());
            Element title = (Element) titleElements.item(0);
            assertEquals("Test", title.getTextContent());
            
            // Check if the converted document has the correct body element
            NodeList bodyElements = w3cDoc.getElementsByTagName("body");
            assertEquals(1, bodyElements.getLength());
            Element body = (Element) bodyElements.item(0);
            NodeList paragraphs = body.getElementsByTagName("p");
            assertEquals(1, paragraphs.getLength());
            Element paragraph = (Element) paragraphs.item(0);
            assertEquals("Hello World!", paragraph.getTextContent());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }
    
    // Test case for asString method
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Check if the converted document is not null
        assertNotNull(w3cDoc);
        
        // Check if the converted document can be serialized to a string without throwing an exception
        String w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
        
        // Check if the serialized string is not empty
        assertFalse(w3cString.isEmpty());
    }
}