import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup_ValidInput_ReturnsW3CDocument() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();

        // Act
        Document result = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testFromJsoup_NullInput_ThrowsException() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = null;
        W3CDom w3cDom = new W3CDom();

        // Act and Assert
        try {
            w3cDom.fromJsoup(jsoupDoc);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testConvert_ValidInput_ConvertsJsoupDocumentToW3CDocument() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document out = null;
        W3CDom w3cDom = new W3CDom();

        // Act
        w3cDom.convert(jsoupDoc, out);

        // Assert
        // Perform validation on the converted W3CDocument
        Assert.assertNotEquals("", out.getDocumentURI());
        Assert.assertNotNull(out.getFirstChild());
    }

    @Test
    public void testConvert_LocationNotEmpty_SetsDocumentURI() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.setBaseUri("http://www.example.com");
        Document out = null;
        W3CDom w3cDom = new W3CDom();

        // Act
        w3cDom.convert(jsoupDoc, out);

        // Assert
        Assert.assertEquals("http://www.example.com", out.getDocumentURI());
    }

    @Test
    public void testConvert_EmptyInput_DoesNotThrowException() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document out = null;
        W3CDom w3cDom = new W3CDom();

        // Act and Assert
        try {
            w3cDom.convert(jsoupDoc, out);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAsString_ValidInput_ReturnsSerializedString() {
        // Arrange
        Document doc = null;
        W3CDom w3cDom = new W3CDom();

        // Act
        String result = w3cDom.asString(doc);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testAsString_NullInput_ThrowsException() {
        // Arrange
        Document doc = null;
        W3CDom w3cDom = new W3CDom();

        // Act and Assert
        try {
            w3cDom.asString(doc);
            Assert.fail("Expected IllegalStateException was not thrown.");
        } catch (IllegalStateException e) {
            // Success
        }
    }

}