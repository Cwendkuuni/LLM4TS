package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    
    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }
    
    @After
    public void tearDown() {
        w3cDom = null;
    }
    
    @Test
    public void testFromJsoup_validDocument_returnW3CDocument() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello, world!</div></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
        assertNotNull(w3cDoc.getElementsByTagName("title"));
        assertNotNull(w3cDoc.getElementsByTagName("body"));
        NodeList divList = w3cDoc.getElementsByTagName("div");
        assertEquals(1, divList.getLength());
        Element div = (Element) divList.item(0);
        assertEquals("content", div.getAttribute("id"));
        NodeList textList = div.getChildNodes();
        assertEquals(1, textList.getLength());
        Text text = (Text) textList.item(0);
        assertEquals("Hello, world!", text.getWholeText());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullDocument_throwException() {
        w3cDom.fromJsoup(null);
    }
    
    @Test
    public void testConvert_validDocuments_convertSuccessfully() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello, world!</div></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document destW3cDoc = Jsoup.parse("<html></html>");
        w3cDom.convert(w3cDoc, destW3cDoc);
        
        assertNotNull(destW3cDoc.getDocumentElement());
        assertEquals("html", destW3cDoc.getDocumentElement().getNodeName());
        assertNotNull(destW3cDoc.getElementsByTagName("title"));
        assertNotNull(destW3cDoc.getElementsByTagName("body"));
        NodeList divList = destW3cDoc.getElementsByTagName("div");
        assertEquals(1, divList.getLength());
        Element div = (Element) divList.item(0);
        assertEquals("content", div.getAttribute("id"));
        NodeList textList = div.getChildNodes();
        assertEquals(1, textList.getLength());
        Text text = (Text) textList.item(0);
        assertEquals("Hello, world!", text.getWholeText());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullInputDocument_throwException() {
        w3cDom.convert(null, Jsoup.parse("<html></html>"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullOutputDocument_throwException() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello, world!</div></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(w3cDoc, null);
    }
    
    @Test
    public void testAsString_validDocument_returnString() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello, world!</div></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String result = w3cDom.asString(w3cDoc);
        
        assertNotNull(result);
        assertTrue(result.startsWith("<?xml"));
        assertTrue(result.contains("<html"));
        assertTrue(result.contains("<title>Test</title>"));
        assertTrue(result.contains("<div id=\"content\">Hello, world!</div>"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullDocument_throwException() {
        w3cDom.asString(null);
    }
}