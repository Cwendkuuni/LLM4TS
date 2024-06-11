
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_withNonNullJsoupDocument_returnW3CDocument() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");

        // Act
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Assert
        assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_withNullJsoupDocument_throwIllegalArgumentException() {
        // Act
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_withValidJsoupDocumentAndW3CDocument_convertSuccessfully() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Act
        w3cDom.convert(jsoupDocument, w3cDocument);

        // Assert
        assertEquals(jsoupDocument.location(), w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvert_withInvalidJsoupDocumentAndW3CDocument_noConversionOccurs() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Act
        w3cDom.convert(jsoupDocument, w3cDocument);

        // Assert
        assertNull(w3cDocument.getDocumentURI());
    }

    @Test
    public void testW3CBuilder_head_withElementNode_createsW3CElementNode() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head();

        // Act
        w3cBuilder.head(element, 0);

        // Assert
        assertEquals(element.tagName(), w3cBuilder.dest.getTagName());
    }

    @Test
    public void testW3CBuilder_head_withTextNode_createsW3CTextNode() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        TextNode textNode = new TextNode("Hello World");

        // Act
        w3cBuilder.head(textNode, 0);

        // Assert
        assertEquals(textNode.getWholeText(), w3cBuilder.dest.getTextContent());
    }

    @Test
    public void testW3CBuilder_head_withCommentNode_createsW3CCommentNode() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Comment commentNode = new Comment("This is a comment");

        // Act
        w3cBuilder.head(commentNode, 0);

        // Assert
        assertEquals(commentNode.getData(), w3cBuilder.dest.getNodeValue());
    }

    @Test
    public void testW3CBuilder_head_withDataNode_createsW3CDataNode() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        DataNode dataNode = new DataNode("12345", "");

        // Act
        w3cBuilder.head(dataNode, 0);

        // Assert
        assertEquals(dataNode.getWholeData(), w3cBuilder.dest.getTextContent());
    }

    @Test(expected = NullPointerException.class)
    public void testW3CBuilder_head_withNullNode_throwNullPointerException() {
        // Arrange
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(new Document());
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);

        // Act
        w3cBuilder.head(null, 0);
    }

    @Test
    public void testW3CBuilder_tail_withElementNodeAndParentElementNode_undeDescendSuccessfully() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head();
        w3cBuilder.head(element, 0);

        // Act
        w3cBuilder.tail(element, 0);

        // Assert
        assertEquals(w3cBuilder.dest.getParentNode().getNodeType(), org.w3c.dom.Node.ELEMENT_NODE);
    }

    @Test
    public void testCopyAttributes_withValidJsoupNodeAndW3CElement_copyAttributesSuccessfully() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head().appendElement("meta").attr("charset", "UTF-8");

        // Act
        w3cBuilder.head(element, 0);
        Element w3cElement = w3cBuilder.dest;
        w3cBuilder.copyAttributes(element, w3cElement);

        // Assert
        Attr attr = w3cElement.getAttributeNode("charset");
        assertNotNull(attr);
        assertEquals("UTF-8", attr.getValue());
    }

    @Test
    public void testCopyAttributes_withInvalidJsoupNodeAndW3CElement_noAttributesCopied() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head().appendElement("meta").attr("charset#", "???");

        // Act
        w3cBuilder.head(element, 0);
        Element w3cElement = w3cBuilder.dest;
        w3cBuilder.copyAttributes(element, w3cElement);

        // Assert
        assertNull(w3cElement.getAttributeNode("charset"));
    }

    @Test
    public void testCopyAttributes_withNullJsoupNodeAndW3CElement_noAttributesCopied() {
        // Arrange
        Document jsoupDocument = new Document();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head().appendElement("meta");

        // Act
        w3cBuilder.head(element, 0);
        Element w3cElement = w3cBuilder.dest;
        w3cBuilder.copyAttributes(null, w3cElement);

        // Assert
        assertFalse(w3cElement.hasAttributes());
    }

    @Test
    public void testUpdateNamespaces_withValidJsoupElement_updateNamespacesSuccessfully() {
        // Arrange
        Document jsoupDocument = new Document("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:example=\"http://example.com\"></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head();

        // Act
        String prefix = w3cBuilder.updateNamespaces(element);

        // Assert
        assertEquals("example", prefix);
        assertEquals("http://example.com", w3cBuilder.namespaces.get(prefix));
    }

    @Test
    public void testUpdateNamespaces_withInvalidJsoupElement_noNamespacesUpdated() {
        // Arrange
        Document jsoupDocument = new Document("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head();

        // Act
        String prefix = w3cBuilder.updateNamespaces(element);

        // Assert
        assertNull(prefix);
        assertTrue(w3cBuilder.namespaces.isEmpty());
    }

    @Test
    public void testUpdateNamespaces_withElementWithoutNamespaces_noNamespacesUpdated() {
        // Arrange
        Document jsoupDocument = new Document("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        Element element = jsoupDocument.head();

        // Act
        String prefix = w3cBuilder.updateNamespaces(element);

        // Assert
        assertNull(prefix);
        assertTrue(w3cBuilder.namespaces.isEmpty());
    }

    @Test
    public void testAsString_withValidW3CDocument_returnDocumentAsString() {
        // Arrange
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(new Document("<html><head><title>Test</title></head><body></body></html>"));

        // Act
        String docAsString = w3cDom.asString(w3cDocument);

        // Assert
        assertNotNull(docAsString);
        assertTrue(docAsString.contains("<html"));
        assertTrue(docAsString.contains("<head"));
        assertTrue(docAsString.contains("<title"));
        assertTrue(docAsString.contains("<body"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_withNullW3CDocument_throwIllegalStateException() {
        // Act
        w3cDom.asString(null);
    }

}

