import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.junit.Assert;

public class W3CDomTest {

    private W3CDom w3cDom;
    
    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup_ValidDocument_ReturnsValidW3CDocument() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        
        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Assert
        Assert.assertNotNull(w3cDoc);
    }
    
    @Test
    public void testFromJsoup_NullDocument_ThrowsIllegalArgumentException() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = null;
        
        // Act and Assert
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(jsoupDoc);
        });
    }
    
    @Test
    public void testConvert_ValidDocuments_ConvertsSuccessfully() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, out);
        
        // Assert
        Assert.assertEquals(w3cDoc.getDocumentURI(), out.getDocumentURI());
    }
    
    @Test
    public void testConvert_WithBlankLocation_SetsDocumentURI() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, out);
        
        // Assert
        Assert.assertEquals(jsoupDoc.location(), out.getDocumentURI());
    }
    
    @Test
    public void testConvert_WithElement_CreatesValidW3CElement() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupDoc.appendChild(jsoupEl);
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, out);
        
        // Assert
        Assert.assertEquals(jsoupEl.tagName(), out.getDocumentElement().getTagName());
    }
    
    @Test
    public void testConvert_WithTextNode_CreatesValidW3CTextNode() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("text", "");
        jsoupDoc.appendChild(jsoupText);
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, out);
        
        // Assert
        Assert.assertEquals(jsoupText.getWholeText(), out.getDocumentElement().getFirstChild().getNodeValue());
    }
    
    @Test
    public void testConvert_WithComment_CreatesValidW3CComment() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        jsoupDoc.appendChild(jsoupComment);
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, out);
        
        // Assert
        Assert.assertEquals(jsoupComment.getData(), out.getDocumentElement().getFirstChild().getNodeValue());
    }
    
    @Test
    public void testConvert_WithDataNode_CreatesValidW3CDataNode() {
        // Arrange
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("data", "text");
        jsoupDoc.appendChild(jsoupData);
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, out);
        
        // Assert
        Assert.assertEquals(jsoupData.getWholeData(), out.getDocumentElement().getFirstChild().getNodeValue());
    }
    
    @Test
    public void testCopyAttributes_ValidAttributes_CopiesCorrectly() {
        // Arrange
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupEl.attr("attr1", "value1");
        jsoupEl.attr("attr2", "value2");
        org.w3c.dom.Element w3cEl = w3cDom.factory.newDocumentBuilder().newDocument().createElement("tagName");
        
        // Act
        w3cDom.copyAttributes(jsoupEl, w3cEl);
        
        // Assert
        Assert.assertEquals(jsoupEl.attr("attr1"), w3cEl.getAttribute("attr1"));
        Assert.assertEquals(jsoupEl.attr("attr2"), w3cEl.getAttribute("attr2"));
    }
    
    @Test
    public void testCopyAttributes_InvalidAttributes_DoesNotCopy() {
        // Arrange
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupEl.attr("attr1", "value1");
        jsoupEl.attr("invalid attr", "value2");
        org.w3c.dom.Element w3cEl = w3cDom.factory.newDocumentBuilder().newDocument().createElement("tagName");
        
        // Act
        w3cDom.copyAttributes(jsoupEl, w3cEl);
        
        // Assert
        Assert.assertNull(w3cEl.getAttribute("invalid attr"));
    }
    
    @Test
    public void testUpdateNamespaces_ValidNamespace_DeclaresCorrectly() {
        // Arrange
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupEl.attr("xmlns:prefix", "urn:xmlns");
        org.jsoup.nodes.Attributes attributes = jsoupEl.attributes();
        
        // Act
        String prefix = w3cDom.updateNamespaces(jsoupEl);
        
        // Assert
        Assert.assertEquals("prefix", prefix);
        Assert.assertEquals("urn:xmlns", w3cDom.namespacesStack.peek().get(prefix));
    }
    
    @Test
    public void testUpdateNamespaces_InvalidNamespace_DoesNotDeclare() {
        // Arrange
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        jsoupEl.attr("xmlns", "urn:xmlns");
        org.jsoup.nodes.Attributes attributes = jsoupEl.attributes();
        
        // Act
        String prefix = w3cDom.updateNamespaces(jsoupEl);
        
        // Assert
        Assert.assertNull(prefix);
        Assert.assertFalse(w3cDom.namespacesStack.peek().containsKey("xmlns"));
    }
    
    @Test
    public void testAsString_ValidDocument_ReturnsValidString() {
        // Arrange
        org.w3c.dom.Document w3cDoc = w3cDom.factory.newDocumentBuilder().newDocument();
        org.w3c.dom.Element w3cEl = w3cDoc.createElement("tagName");
        w3cDoc.appendChild(w3cEl);
        String expectedString = "<tagName></tagName>";
        
        // Act
        String result = w3cDom.asString(w3cDoc);
        
        // Assert
        Assert.assertEquals(expectedString, result);
    }
    
    @Test
    public void testAsString_NullDocument_ThrowsIllegalArgumentException() {
        // Arrange
        org.w3c.dom.Document w3cDoc = null;
        
        // Act and Assert
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.asString(w3cDoc);
        });
    }
}