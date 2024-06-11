import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup_validInput_returnW3CDocument() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");

        // Act
        org.w3c.dom.Document result = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals("http://example.com", result.getDocumentURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullInput_throwIllegalArgumentException() {
        // Arrange
        W3CDom w3cDom = new W3CDom();

        // Act
        org.w3c.dom.Document result = w3cDom.fromJsoup(null);

        // Assert
        // Expecting IllegalArgumentException
    }

    @Test
    public void testConvert_validInput_convertJsoupToW3C() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(getEmptyW3CDocument());
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");

        // Act
        w3cBuilder.convert(jsoupDoc, w3cBuilder.getDocument());

        // Assert
        org.w3c.dom.Document w3cDoc = w3cBuilder.getDocument();
        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("http://example.com", w3cDoc.getDocumentURI());
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Assert.assertEquals(1, w3cDoc.getElementsByTagName("h1").getLength());
    }

    @Test
    public void testAsString_validInput_returnStringRepresentation() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        String result = w3cDom.asString(w3cDoc);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("<html"));
        Assert.assertTrue(result.contains("<h1>Title</h1>"));
    }

    private org.w3c.dom.Document getEmptyW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}