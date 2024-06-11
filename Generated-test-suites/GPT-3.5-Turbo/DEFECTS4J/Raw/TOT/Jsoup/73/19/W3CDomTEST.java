import org.jsoup.helper.W3CDom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class W3CDomTest {
    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup_WhenValidJsoupDocument_ExpectValidW3CDocument() {
        // Arrange

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);

        // Assert
        Assert.assertNotNull(out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_WhenNullJsoupDocument_ExpectIllegalArgumentException() {
        // Arrange

        // Act
        w3CDom.fromJsoup(null);

        // Assert (Exception)
    }

    @Test
    public void testConvert_WhenValidJsoupDocumentAndValidW3CDocument_ExpectConvertedW3CDocument() {
        // Arrange
        Document out = w3CDom.fromJsoup(jsoupDoc);

        // Act
        w3CDom.convert(jsoupDoc, out);

        // Assert
        Assert.assertFalse(out.getChildNodes().getLength() == 0);
    }

    @Test
    public void testConvert_WhenJsoupDocumentWithLocation_ExpectSetDocumentURIToOutputDocument() {
        // Arrange
        jsoupDoc = Jsoup.parse("body");

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, out);

        // Assert
        Assert.assertEquals(jsoupDoc.location(), out.getDocumentURI());
    }

    @Test
    public void testConvert_WhenJsoupDocumentWithRootElement_ExpectRootElementInOutputDocument() {
        // Arrange
        jsoupDoc = Jsoup.parse("<html></html>");

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, out);

        // Assert
        Assert.assertNotNull(out.getDocumentElement());
    }

    @Test
    public void testConvert_WhenJsoupDocumentWithTextNodes_ExpectTextNodesInOutputDocument() {
        // Arrange
        jsoupDoc = Jsoup.parse("<html>Hello, World!</html>");

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, out);

        // Assert
        List<Object> expectedTextNodes = new ArrayList<>();
        expectedTextNodes.add("Hello, World!");

        List<Object> actualTextNodes = new ArrayList<>();
        recursiveVisit(out, actualTextNodes);

        Assert.assertEquals(expectedTextNodes, actualTextNodes);
    }

    @Test
    public void testConvert_WhenJsoupDocumentWithComments_ExpectCommentsInOutputDocument() {
        // Arrange
        jsoupDoc = Jsoup.parse("<html><!-- This is a comment --></html>");

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, out);

        // Assert
        List<Object> expectedComments = new ArrayList<>();
        expectedComments.add(" This is a comment ");

        List<Object> actualComments = new ArrayList<>();
        recursiveVisit(out, actualComments);

        Assert.assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testConvert_WhenJsoupDocumentWithDataNodes_ExpectDataNodesInOutputDocument() {
        // Arrange
        jsoupDoc = Jsoup.parse("<html><![CDATA[This is some data]]></html>");

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, out);

        // Assert
        List<Object> expectedDataNodes = new ArrayList<>();
        expectedDataNodes.add("This is some data");

        List<Object> actualDataNodes = new ArrayList<>();
        recursiveVisit(out, actualDataNodes);

        Assert.assertEquals(expectedDataNodes, actualDataNodes);
    }

    @Test
    public void testConvert_WhenJsoupDocumentWithAttributes_ExpectAttributesInOutputDocument() {
        // Arrange
        jsoupDoc = Jsoup.parse("<html lang=\"en\"></html>");

        // Act
        Document out = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, out);

        // Assert
        List<Map<String, String>> expectedAttributes = new ArrayList<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("lang", "en");
        expectedAttributes.add(attributes);

        List<Map<String, String>> actualAttributes = new ArrayList<>();
        recursiveVisitWithAttributes(out, actualAttributes);

        Assert.assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testAsString_WhenValidW3CDocument_ExpectSerializedString() {
        // Arrange
        Document doc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, doc);

        // Act
        String result = w3CDom.asString(doc);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testAsString_WhenEmptyW3CDocument_ExpectEmptySerializedString() {
        // Arrange
        Document doc = w3CDom.fromJsoup(jsoupDoc);

        // Act
        String result = w3CDom.asString(doc);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    private void recursiveVisit(Element element, List<Object> nodeList) {
        nodeList.add(element.getTextContent());

        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            if (element.getChildNodes().item(i) instanceof Element) {
                recursiveVisit((Element) element.getChildNodes().item(i), nodeList);
            } else {
                nodeList.add(element.getChildNodes().item(i).getTextContent());
            }
        }
    }

    private void recursiveVisitWithAttributes(Element element, List<Map<String, String>> nodeList) {
        Map<String, String> attributes = new HashMap<>();
        for (int i = 0; i < element.getAttributes().getLength(); i++) {
            attributes.put(element.getAttributes().item(i).getNodeName(), element.getAttributes().item(i).getNodeValue());
        }
        nodeList.add(attributes);

        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            if (element.getChildNodes().item(i) instanceof Element) {
                recursiveVisitWithAttributes((Element) element.getChildNodes().item(i), nodeList);
            } else {
                Map<String, String> textNode = new HashMap<>();
                textNode.put(element.getChildNodes().item(i).getNodeName(), element.getChildNodes().item(i).getNodeValue());
                nodeList.add(textNode);
            }
        }
    }
}