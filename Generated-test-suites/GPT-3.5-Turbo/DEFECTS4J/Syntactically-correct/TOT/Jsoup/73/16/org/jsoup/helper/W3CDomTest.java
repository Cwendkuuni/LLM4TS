import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_ValidInput_ReturnsW3CDocument() {
        // Arrange
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        
        // Act
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        
        // Assert
        assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput_ThrowsIllegalArgumentException() {
        // Arrange
	    org.jsoup.nodes.Document jsoupDocument = null;
        
        // Act
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
    }

    @Test
    public void testConvert_ValidInput_ConvertsJsoupToW3CDocument() {
        // Arrange
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Act
        w3cDom.convert(jsoupDocument, w3cDocument);

        // Assert
        org.jsoup.nodes.Element jsoupElement = jsoupDocument.child(0);
        Element w3cElement = w3cDocument.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals(jsoupElement.tagName(), w3cElement.getTagName());
    }

    @Test
    public void testConvert_BlankLocation_SetsDocumentURI() {
        // Arrange
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("http://example.com");
	    Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Act
        w3cDom.convert(jsoupDocument, w3cDocument);
        
        // Assert
        assertEquals(jsoupDocument.location(), w3cDocument.getDocumentURI());
    }

    @Test
    public void testAsString_ValidInput_ReturnsStringRepresentation() {
        // Arrange
        Document w3cDocument = mock(Document.class);

        // Act
        String result = w3cDom.asString(w3cDocument);
        
        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_NullInput_ThrowsIllegalArgumentException() {
        // Arrange
        Document w3cDocument = null;
        
        // Act
        w3cDom.asString(w3cDocument);
    }
}