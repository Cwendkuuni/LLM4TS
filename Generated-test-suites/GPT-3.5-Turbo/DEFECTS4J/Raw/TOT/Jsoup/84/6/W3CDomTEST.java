import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_validDocument_returnsW3cDocument() {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Welcome</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);
        assertEquals("Test HTML", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Welcome", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullDocument_throwsIllegalArgumentException() {
        // Act
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_validDocuments_convertsSuccessfully() {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Welcome</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        Document outputDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(outputDoc);
        assertEquals("Test HTML", outputDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Welcome", outputDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert_blankLocation_setsDocumentURI() {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Welcome</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.setBaseUri("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        Document outputDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(outputDoc);
        assertEquals("", outputDoc.getDocumentURI());
    }

    @Test
    public void testConvert_nonBlankLocation_setsDocumentURI() {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Welcome</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.setBaseUri("http://example.com");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        Document outputDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(outputDoc);
        assertEquals("http://example.com", outputDoc.getDocumentURI());
    }

    @Test
    public void testConvert_elementWithNamespace_convertsSuccessfully() {
        // Arrange
        String html = "<html xmlns:custom='http://custom.com'><custom:tag>Hello</custom:tag></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        Document outputDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        assertNotNull(outputDoc);
        assertEquals("Hello", outputDoc.getElementsByTagName("custom:tag").item(0).getTextContent());
    }

    @Test
    public void testAsString_validDocument_returnsStringRepresentation() {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Welcome</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        String outputString = w3cDom.asString(w3cDoc);

        // Assert
        assertNotNull(outputString);
        assertTrue(outputString.contains("<html>"));
        assertTrue(outputString.contains("<title>Test HTML</title>"));
        assertTrue(outputString.contains("<h1>Welcome</h1>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullDocument_throwsIllegalArgumentException() {
        // Act
        w3cDom.asString(null);
    }

}