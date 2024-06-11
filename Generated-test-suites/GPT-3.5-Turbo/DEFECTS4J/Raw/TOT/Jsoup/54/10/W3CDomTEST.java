import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDocument;
    private org.w3c.dom.Document w3cDocument;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDocument = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup_withValidDocument_shouldReturnW3cDocument() {
        // Arrange
        jsoupDocument.appendElement("html");

        // Act
        w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Assert
        Assert.assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_withNullInput_shouldThrowIllegalArgumentException() {
        // Act
        w3cDocument = w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_withValidJsoupDocument_shouldConvertToW3cDocument() {
        // Arrange
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        rootElement.appendElement("head");
        rootElement.appendElement("body");

        jsoupDocument.appendChild(rootElement);

        // Act
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDocument);

        // Assert
        Assert.assertNotNull(convertedDoc);
        Assert.assertNotNull(convertedDoc.getDocumentElement());
        Assert.assertEquals("html", convertedDoc.getDocumentElement().getTagName());
        Assert.assertEquals(2, convertedDoc.getDocumentElement().getChildNodes().getLength());
    }

    @Test
    public void testConvert_withNullJsoupDocument_shouldDoNothing() {
        // Act
        org.w3c.dom.Document convertedDoc = w3cDom.convert(null);

        // Assert
        Assert.assertNull(convertedDoc);
    }

    @Test
    public void testConvert_withJsoupDocumentContainingTextNodes_shouldConvertToW3cDocument() {
        // Arrange
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        rootElement.appendChild(new TextNode("Hello world"));

        jsoupDocument.appendChild(rootElement);

        // Act
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDocument);

        // Assert
        Assert.assertNotNull(convertedDoc);
        Assert.assertNotNull(convertedDoc.getDocumentElement());
        Assert.assertEquals("Hello world", convertedDoc.getDocumentElement().getTextContent());
    }

    @Test
    public void testConvert_withJsoupDocumentContainingCommentNodes_shouldConvertToW3cDocument() {
        // Arrange
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        rootElement.appendChild(new org.jsoup.nodes.Comment("This is a comment"));

        jsoupDocument.appendChild(rootElement);

        // Act
        org.w3c.dom.Document convertedDoc = w3cDom.convert(jsoupDocument);

        // Assert
        Assert.assertNotNull(convertedDoc);
        Assert.assertNotNull(convertedDoc.getDocumentElement());
        NodeList commentNodes = convertedDoc.getChildNodes().item(0).getChildNodes();
        Assert.assertEquals(1, commentNodes.getLength());
        Assert.assertTrue(commentNodes.item(0) instanceof Comment);
        Assert.assertEquals("This is a comment", ((Comment) commentNodes.item(0)).getData());
    }

    @Test
    public void testAsString_withValidW3cDocument_shouldReturnStringRepresentation() {
        // Arrange
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        rootElement.appendChild(new TextNode("Hello world"));
        jsoupDocument.appendChild(rootElement);

        org.w3c.dom.Document w3cDocument = w3cDom.convert(jsoupDocument);

        // Act
        String result = w3cDom.asString(w3cDocument);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("Hello world"));
    }

    @Test
    public void testAsString_withNullW3cDocument_shouldReturnEmptyString() {
        // Act
        String result = w3cDom.asString(null);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals("", result);
    }
}