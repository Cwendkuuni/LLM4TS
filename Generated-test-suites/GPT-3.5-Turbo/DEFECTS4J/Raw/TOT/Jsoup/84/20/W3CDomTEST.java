package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.w3c.dom.Document;

public class W3CDomTest {

    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        // Initialize jsoup document for testing
        jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendElement("html")
                .appendElement("head")
                .appendElement("title").text("Test Title")
                .appendElement("body")
                .appendElement("h1").text("Test Heading")
                .appendElement("p").text("Test Paragraph");
    }

    @Test
    public void testFromJsoup_NormalCase() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Test if the converted document is not null
        assertNotNull(w3cDoc);
        
        // Test if the converted document has the correct title
        assertEquals(w3cDoc.getElementsByTagName("title").item(0).getTextContent(), "Test Title");
        
        // Test if the converted document has the correct heading
        assertEquals(w3cDoc.getElementsByTagName("h1").item(0).getTextContent(), "Test Heading");
        
        // Test if the converted document has the correct paragraph
        assertEquals(w3cDoc.getElementsByTagName("p").item(0).getTextContent(), "Test Paragraph");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }
    
    @Test
    public void testConvert_NormalCase() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Test if the output document is the same as the converted document
        assertSame(w3cDoc, out);
        
        // Test if the output document has the correct title
        assertEquals(w3cDoc.getElementsByTagName("title").item(0).getTextContent(), "Test Title");
        
        // Test if the output document has the correct heading
        assertEquals(w3cDoc.getElementsByTagName("h1").item(0).getTextContent(), "Test Heading");
        
        // Test if the output document has the correct paragraph
        assertEquals(w3cDoc.getElementsByTagName("p").item(0).getTextContent(), "Test Paragraph");
    }
    
    @Test
    public void testConvert_LocationNotBlank() {
        W3CDom w3cDom = new W3CDom();
        jsoupDoc.location("http://example.com");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Test if the output document has the correct URI
        assertEquals(out.getDocumentURI(), "http://example.com");
    }
    
    @Test
    public void testAsString_NormalCase() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        
        // Test if the output xml string is not null or empty
        assertNotNull(xmlString);
        assertNotEquals("", xmlString);
        
        // Test if the output xml string contains the correct title
        assertTrue(xmlString.contains("<title>Test Title</title>"));
        
        // Test if the output xml string contains the correct heading
        assertTrue(xmlString.contains("<h1>Test Heading</h1>"));
        
        // Test if the output xml string contains the correct paragraph
        assertTrue(xmlString.contains("<p>Test Paragraph</p>"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAsString_NullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(null);
    }
}