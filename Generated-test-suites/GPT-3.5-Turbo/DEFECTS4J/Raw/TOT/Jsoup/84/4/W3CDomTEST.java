import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {

    private static W3CDom w3cDom;
    
    @BeforeClass
    public static void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup_withValidDocument_returnsW3CDocument() {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        
        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Assert
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_withNullDocument_throwsIllegalArgumentException() {
        // Act
        w3cDom.fromJsoup(null);
    }
    
    @Test
    public void testConvert_withValidJsoupDocument_convertsToW3CDocument() {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        assertNotNull(convertedDoc);
        assertEquals("html", convertedDoc.getDocumentElement().getTagName());
        assertEquals("Test", convertedDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello", convertedDoc.getElementsByTagName("h1").item(0).getTextContent());
    }
    
    @Test
    public void testConvert_withBlankLocation_setsDocumentURI() {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.setBaseUri("http://www.example.com");

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(convertedDoc);
        assertEquals(jsoupDoc.location(), convertedDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert_withNonBlankLocation_doesNotSetDocumentURI() {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(convertedDoc);
        assertNotEquals(jsoupDoc.location(), convertedDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert_withElement_convertsToW3CElement() {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Act
        Element w3cElement = convertedDoc.createElementNS("http://www.w3.org/1999/xhtml", "h2");
        Text textNode = convertedDoc.createTextNode("World");
        w3cElement.appendChild(textNode);
        convertedDoc.getDocumentElement().appendChild(w3cElement);
        
        // Assert
        assertEquals("h2", convertedDoc.getElementsByTagName("h2").item(0).getNodeName());
        assertEquals("World", convertedDoc.getElementsByTagName("h2").item(0).getTextContent());
    }
    
    @Test
    public void testAsString_withValidW3CDocument_returnsString() {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Act
        String xmlString = w3cDom.asString(w3cDoc);
        
        // Assert
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<html>"));
        assertTrue(xmlString.contains("<title>Test</title>"));
        assertTrue(xmlString.contains("<h1>Hello</h1>"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAsString_withTransformerException_throwsIllegalStateException() {
        // Arrange
        org.w3c.dom.Document w3cDoc = new org.apache.xerces.dom.DocumentImpl();
        
        // Act
        w3cDom.asString(w3cDoc);
    }
}