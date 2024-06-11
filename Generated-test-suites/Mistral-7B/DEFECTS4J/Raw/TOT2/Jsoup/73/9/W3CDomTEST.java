package org.example.w3cdom;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given
        Document jsoupDocument = new Document();
        // When
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        // Then
        assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupWithNullJsoupDocument() {
        // Given
        Document jsoupDocument = null;
        // When
        W3CDom obj = new W3CDom();
        // Then
        obj.fromJsoup(jsoupDocument);
    }

    @Test
    public void testFromJsoupEmptyJsoupDocument() {
        // Given
        Document jsoupDocument = new Document();
        // When
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        // Then
        assertNotNull(w3cDocument);
        assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());
    }

    @Test
    public void testFromJsoupEmptyW3CDocument() {
        // Given
        Document jsoupDocument = new Document();
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        // When
        String result = new W3CDom().asString(w3cDocument);
        // Then
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testConvertMultipleElementsJsoupDocument() {
        // Given
        Document jsoupDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        Element childEl1 = jsoupDocument.createElement("child1");
        Element childEl2 = jsoupDocument.createElement("child2");
        rootEl.appendChild(childEl1);
        rootEl.appendChild(childEl2);
        jsoupDocument.appendChild(rootEl);
        Document w3cDocument = new Document();
        // When
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Then
        Node rootNode = w3cDocument.getDocumentElement();
        assertNotNull(rootNode);
        assertEquals("root", rootNode.getNodeName());
        assertEquals(2, rootNode.getChildNodes().getLength());
        Node childNode1 = rootNode.getChildNodes().item(0);
        assertEquals("child1", childNode1.getNodeName());
        Node childNode2 = rootNode.getChildNodes().item(1);
        assertEquals("child2", childNode2.getNodeName());
    }

    @Test(expected = ParserConfigurationException.class)
    public void testFromJsoupParserConfigurationException() {
        // Given
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // When
            throw e;
        } finally {
            // Then
        }
    }

    @Test(expected = TransformerException.class)
    public void testAsStringTransformerException() {
        // Given
        Document w3cDocument = new Document();
        // When
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(w3cDocument);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // When
            throw e;
        } finally {
            // Then
        }
    }

    @Test
    public void testConvert() {
        // Given
        Document jsoupDocument = new Document();
        Document w3cDocument = new Document();
        // When
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Then
        assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullJsoupDocument() {
        // Given
        Document jsoupDocument = null;
        Document w3cDocument = new Document();
        // When
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Then
        fail("Method should throw an IllegalArgumentException with a null jsoup document.");
    }

    @Test
    public void testAsString() {
        // Given
        Document w3cDocument = new Document();
        // When
        String result = new W3CDom().asString(w3cDocument);
        // Then
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsStringWithNullDocument() {
        // Given
        Document w3cDocument = null;
        // When
        String result = new W3CDom().asString(w3cDocument);
        // Then
        fail("Method should throw an IllegalArgumentException with a null document.");
    }

    @Test
    public void testFromJsoupWithAttributes() {
        // Given
        Document jsoupDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        rootEl.addClass("class1");
        rootEl.attr("data-custom", "data");
        jsoupDocument.appendChild(rootEl);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        // When
        Element w3cRootEl = w3cDocument.getDocumentElement();
        // Then
        assertEquals("root", w3cRootEl.getTagName());
        assertEquals("class1", w3cRootEl.getAttribute("class"));
        assertEquals("data", w3cRootEl.getAttribute("data-custom"));
    }

    @Test
    public void testConvertWithComments() {
        // Given
        Document jsoupDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        rootEl.appendChild(new TextNode("Text content"));
        rootEl.appendChild(new CommentNode("This is a comment"));
        jsoupDocument.appendChild(rootEl);
        Document w3cDocument = new Document();
        // When
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Then
        NodeList w3cNodeList = w3cDocument.getElementsByTagName("root");
        Node w3cRootEl = w3cNodeList.item(0);
        assertEquals("root", w3cRootEl.getNodeName());
        assertEquals("Text content", w3cRootEl.getTextContent());
        Node w3cCommentNode = w3cRootEl.getChildNodes().item(1);
        assertEquals("#comment", w3cCommentNode.getNodeName());
        assertEquals("This is a comment", w3cCommentNode.getNodeValue());
    }

    @Test
    public void testConvertWithTextNode() {
        // Given
        Document jsoupDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        Text textNode = jsoupDocument.createTextNode("Text content");
        rootEl.appendChild(textNode);
        jsoupDocument.appendChild(rootEl);
        Document w3cDocument = new Document();
        // When
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Then
        NodeList w3cNodeList = w3cDocument.getElementsByTagName("root");
        Node w3cRootEl = w3cNodeList.item(0);
        assertEquals("root", w3cRootEl.getNodeName());
        assertEquals("Text content", w3cRootEl.getTextContent());
    }

    @Test
    public void testConvertWithDocumentType() {
        // Given
        Document jsoupDocument = new Document();
        DocumentType documentType = jsoupDocument.getDoctype();
        Document w3cDocument = new Document();
        // When
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Then
        DocumentType w3cDocumentType = w3cDocument.getDoctype();
        assertEquals("HTML", w3cDocumentType.getNodeName());
        assertEquals(documentType.getPublicId(), w3cDocumentType.getPublicId());
        assertEquals(documentType.getSystemId(), w3cDocumentType.getSystemId());
    }
}