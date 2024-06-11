import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Test;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void fromJsoup_validInput_returnsW3CDocument() throws Exception {
        // Arrange
        String html = "<html><head><title>Test</title></head>"
                + "<body><h1>Heading</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        // Act
        W3CDom domConverter = new W3CDom();
        org.w3c.dom.Document w3cDoc = domConverter.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);
    }

    @Test(expected = NullPointerException.class)
    public void fromJsoup_nullInput_throwsNullPointerException() {
        // Arrange
        Document jsoupDoc = null;

        // Act
        W3CDom domConverter = new W3CDom();
        domConverter.fromJsoup(jsoupDoc);

        // Assert
        // Exception expected
    }

    @Test
    public void convert_validInput_modifiesW3CDocument() throws Exception {
        // Arrange
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = db.newDocument();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head>"
                + "<body><h1>Heading</h1></body></html>");

        // Act
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.convert(jsoupDoc, w3cDoc);

        // Assert
        NodeList nodeList = w3cDoc.getElementsByTagName("h1");
        Element element = (Element) nodeList.item(0);
        assertNotNull(element);
    }

    @Test
    public void asString_validDocument_returnsString() throws Exception {
        // Arrange
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = db.newDocument();
        W3CDom domConverter = new W3CDom();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>";
        w3cDoc = db.parse(new InputSource(new StringReader(expected)));

        // Act
        String result = domConverter.asString(w3cDoc);

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void asString_transformerException_throwsIllegalStateException() {
        // Arrange
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = db.newDocument();
        W3CDom domConverter = new W3CDom();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>";
        w3cDoc = db.parse(new InputSource(new StringReader(expected)));

        // Act
        String result = domConverter.asString(w3cDoc);

        // Assert
        // Exception expected
    }

    @Test
    public void convert_elementWithNamespaceInAttribute_shouldAddNamespaceToW3CElement() throws Exception {
        // Arrange
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = db.newDocument();
        Document jsoupDoc = Jsoup.parse("<ns:root xmlns:ns=\"urn:namespace-uri\"/>");

        // Act
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.convert(jsoupDoc, w3cDoc);
        Element element = (Element) w3cDoc.getElementsByTagName("ns:root").item(0);
        String namespace = element.lookupNamespaceURI("ns");

        // Assert
        assertNotNull(element);
        assertEquals("urn:namespace-uri", namespace);
    }
}