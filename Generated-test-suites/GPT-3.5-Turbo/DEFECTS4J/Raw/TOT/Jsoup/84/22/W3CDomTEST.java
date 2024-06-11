import org.junit.Test;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup_validDocument_shouldConvertToW3CDocument() throws Exception {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document expectedW3CDoc = builder.newDocument();

        // Act
        Document actualW3CDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertEquals("The jsoup document should be converted to the expected W3C document",
                expectedW3CDoc, actualW3CDoc);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullInput_shouldThrowException() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = null;
        W3CDom w3cDom = new W3CDom();
        
        // Act
        w3cDom.fromJsoup(jsoupDoc);
        
        // Assert - expecting IllegalArgumentException
    }
    
    @Test
    public void testConvert_validInput_shouldConvertJsoupDocToW3CDoc() throws Exception {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document expectedW3CDoc = builder.newDocument();

        // Act
        w3cDom.convert(jsoupDoc, expectedW3CDoc);
        Document actualW3CDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertEquals("The jsoup document should be converted to the expected W3C document",
                expectedW3CDoc, actualW3CDoc);
    }
    
    @Test
    public void testConvert_blankLocation_shouldSetDocumentURIAsEmpty() throws Exception {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        jsoupDoc.location("");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document expectedW3CDoc = builder.newDocument();
        expectedW3CDoc.setDocumentURI("");

        // Act
        Document actualW3CDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertEquals("The document URI should be empty",
                expectedW3CDoc.getDocumentURI(), actualW3CDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert_rootElementWithNamespacePrefix_shouldAddNamespaceToRootElement() throws Exception {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<prefix:root xmlns:prefix='http://www.example.com'></prefix:root>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document expectedW3CDoc = builder.newDocument();
        Element expectedRootElement = expectedW3CDoc.createElementNS("http://www.example.com", "prefix:root");
        expectedW3CDoc.appendChild(expectedRootElement);

        // Act
        Document actualW3CDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertEquals("The root element should have a namespace prefix",
                expectedRootElement.getTagName(), actualW3CDoc.getDocumentElement().getTagName());
        assertEquals("The root element's namespace URI should be correct",
                expectedRootElement.getNamespaceURI(),
                actualW3CDoc.getDocumentElement().getNamespaceURI());
    }
    
    @Test
    public void testConvert_elementWithAttributeContainingSpecialCharacters_shouldCopyAttribute() throws Exception {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<root attribute='value with special characters: <>&'></root>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document expectedW3CDoc = builder.newDocument();
        Element expectedRootElement = expectedW3CDoc.createElement("root");
        expectedRootElement.setAttribute("attribute", "value with special characters: <>&");
        expectedW3CDoc.appendChild(expectedRootElement);

        // Act
        Document actualW3CDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertEquals("The root element should have the expected attribute value",
                expectedRootElement.getAttribute("attribute"),
                actualW3CDoc.getDocumentElement().getAttribute("attribute"));
    }
    
    @Test
    public void testAsString_validDocument_shouldSerializeToExpectedString() throws Exception {
        // Arrange
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDoc = builder.parse(new InputSource(new StringReader("<root></root>")));
        W3CDom w3cDom = new W3CDom();
        String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root></root>";

        // Act
        String actualString = w3cDom.asString(w3cDoc);

        // Assert
        assertEquals("The serialized string should be the expected string",
                expectedString, actualString);
    }
    
}