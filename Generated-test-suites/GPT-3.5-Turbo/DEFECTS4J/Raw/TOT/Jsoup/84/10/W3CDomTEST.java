import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Test typical use case
        org.jsoup.nodes.Document jSoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jSoupDoc);
        
        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getNodeName());
        NodeList titleElements = w3cDoc.getElementsByTagName("title");
        assertEquals(1, titleElements.getLength());
        assertEquals("Test", titleElements.item(0).getTextContent());
        NodeList h1Elements = w3cDoc.getElementsByTagName("h1");
        assertEquals(1, h1Elements.getLength());
        assertEquals("Hello World", h1Elements.item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        // Test typical use case
        org.jsoup.nodes.Document jSoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = docBuilder.newDocument();
            w3cDom.convert(jSoupDoc, w3cDoc);
            
            assertNotNull(w3cDoc);
            Element rootElement = w3cDoc.getDocumentElement();
            assertEquals("html", rootElement.getNodeName());
            NodeList titleElements = w3cDoc.getElementsByTagName("title");
            assertEquals(1, titleElements.getLength());
            assertEquals("Test", titleElements.item(0).getTextContent());
            NodeList h1Elements = w3cDoc.getElementsByTagName("h1");
            assertEquals(1, h1Elements.getLength());
            assertEquals("Hello World", h1Elements.item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        // Test typical use case
        org.jsoup.nodes.Document jSoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jSoupDoc);
        
        String xmlString = w3cDom.asString(w3cDoc);
        assertTrue(xmlString.contains("<title>Test</title>"));
        assertTrue(xmlString.contains("<h1>Hello World</h1>"));
        assertTrue(xmlString.contains("<html xmlns=\"http://www.w3.org/1999/xhtml\">"));
    }

}