import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup_ValidInput_ReturnsW3CDocument() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        
        // Act
        org.w3c.dom.Document result = w3CDom.fromJsoup(jsoupDoc);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testFromJsoup_NullInput_ThrowsException() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        
        // Act & Assert
        try {
            w3CDom.fromJsoup(null);
        } catch (NullPointerException e) {
            assertEquals("in", e.getMessage());
        }
    }
    
    @Test
    public void testConvert_ValidInput_ConvertsJsoupToW3C() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        
        // Act
        w3CDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert_BlankLocation_SetsDocumentURI() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        
        // Act
        w3CDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        assertEquals("", w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert_NonBlankLocation_SetsDocumentURI() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        
        // Act
        w3CDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert_ValidElement_CreatesElementNode() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        org.w3c.dom.Document w3cDoc = null;
        Element w3cElement = null;
        
        // Act
        w3CBuilder.head(jsoupElement, 0);
        if (w3CBuilder.dest instanceof Element) {
            w3cElement = (Element) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cElement);
    }
    
    @Test
    public void testConvert_ValidTextNode_CreatesTextNode() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("text", "");
        org.w3c.dom.Text w3cText = null;
        
        // Act
        w3CBuilder.head(jsoupTextNode, 0);
        if (w3CBuilder.dest instanceof Text) {
            w3cText = (Text) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cText);
        assertEquals("text", w3cText.getWholeText());
    }
    
    @Test
    public void testConvert_ValidComment_CreatesCommentNode() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        org.w3c.dom.Comment w3cComment = null;
        
        // Act
        w3CBuilder.head(jsoupComment, 0);
        if (w3CBuilder.dest instanceof Comment) {
            w3cComment = (Comment) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cComment);
        assertEquals("comment", w3cComment.getData());
    }
    
    @Test
    public void testConvert_ValidDataNode_CreatesTextNode() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("data", "");
        org.w3c.dom.Text w3cText = null;
        
        // Act
        w3CBuilder.head(jsoupDataNode, 0);
        if (w3CBuilder.dest instanceof Text) {
            w3cText = (Text) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cText);
        assertEquals("data", w3cText.getWholeText());
    }
    
    @Test
    public void testTail_ValidElementParent_Undescends() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        Element w3cElement = null;
        
        // Act
        w3CBuilder.head(jsoupElement, 0);
        w3CBuilder.tail(jsoupElement, 0);
        if (w3CBuilder.dest instanceof Element) {
            w3cElement = (Element) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cElement);
        assertEquals(null, w3cElement.getParentNode());
    }
    
    @Test
    public void testCopyAttributes_ValidNode_CopiesAttributes() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("id", "myId");
        jsoupElement.attr("class", "myClass");
        jsoupElement.attr("data-value", "123");
        Element w3cElement = null;
        
        // Act
        w3CBuilder.head(jsoupElement, 0);
        if (w3CBuilder.dest instanceof Element) {
            w3cElement = (Element) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cElement);
        assertEquals("myId", w3cElement.getAttribute("id"));
        assertEquals("myClass", w3cElement.getAttribute("class"));
        assertEquals("123", w3cElement.getAttribute("data-value"));
    }
    
    @Test
    public void testUpdateNamespaces_ValidElementWithNamespace_DeclaresNamespace() {
        // Arrange
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("custom:div");
        jsoupElement.attr("xmlns:custom", "http://www.example.com");
        Element w3cElement = null;
        
        // Act
        String result = w3CBuilder.updateNamespaces(jsoupElement);
        if (w3CBuilder.dest instanceof Element) {
            w3cElement = (Element) w3CBuilder.dest;
        }
        
        // Assert
        assertNotNull(w3cElement);
        assertEquals("http://www.example.com", w3CBuilder.namespaces.get("custom"));
        assertEquals("custom", result);
    }
    
    @Test
    public void testAsString_ValidInput_ReturnsStringRepresentationOfW3CDocument() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(new org.jsoup.nodes.Document(""));
        
        // Act
        String result = w3CDom.asString(w3cDoc);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testAsString_NullInput_ThrowsException() {
        // Arrange
        W3CDom w3CDom = new W3CDom();
        
        // Act & Assert
        try {
            w3CDom.asString(null);
        } catch (NullPointerException e) {
            assertEquals("doc", e.getMessage());
        }
    }
}