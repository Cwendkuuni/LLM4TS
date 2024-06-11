import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @BeforeEach
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDoc = mock(org.jsoup.nodes.Document.class);
    }

    @Test
    public void testFromJsoup_ValidDocument_ReturnsW3CDocument() {
        DocumentBuilderFactory factory = mock(DocumentBuilderFactory.class);
        DocumentBuilder builder = mock(DocumentBuilder.class);
        Document out = mock(Document.class);
        
        Mockito.when(jsoupDoc.child(0)).thenReturn(mock(Element.class));
        Mockito.when(jsoupDoc.location()).thenReturn("http://example.com");
        Mockito.when(factory.newDocumentBuilder()).thenReturn(builder);
        Mockito.when(builder.newDocument()).thenReturn(out);
        
        Document result = w3cDom.fromJsoup(jsoupDoc);
        
        Assertions.assertEquals(out, result);
        verify(out).setDocumentURI("http://example.com");
        verify(jsoupDoc).child(0);
    }

    @Test
    public void testFromJsoup_NullDocument_ThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(null);
        });
    }

    @Test
    public void testConvert_ValidDocuments_ConvertsSuccessfully() {
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);
        Document out = mock(Document.class);
        NodeTraversor traversor = mock(NodeTraversor.class);
        W3CDom.W3CBuilder w3cBuilder = mock(W3CDom.W3CBuilder.class);
        
        Mockito.when(jsoupDoc.child(0)).thenReturn(rootEl);
        Mockito.when(rootEl.tagName()).thenReturn("body");
        Mockito.when(w3cBuilder.namespaces).thenReturn(new HashMap<>());
        Mockito.when(w3cBuilder.dest).thenReturn(out);
        Mockito.when(traversor.traverse(rootEl)).thenReturn(w3cBuilder);
        
        w3cDom.convert(jsoupDoc, out);
        
        verify(out).setDocumentURI("");
        verify(traversor).traverse(rootEl);
    }

    @Test
    public void testAsString_ValidDocument_ReturnsStringRepresentation() {
        org.w3c.dom.Document doc = mock(org.w3c.dom.Document.class);
        DOMSource domSource = mock(DOMSource.class);
        StringWriter writer = mock(StringWriter.class);
        StreamResult result = mock(StreamResult.class);
        TransformerFactory tf = mock(TransformerFactory.class);
        Transformer transformer = mock(Transformer.class);
        
        Mockito.when(doc.getDocumentURI()).thenReturn("http://example.com");
        Mockito.when(jsoupDoc.child(0)).thenReturn(mock(Element.class));
        Mockito.when(factory.newDocumentBuilder()).thenReturn(builder);
        Mockito.when(builder.newDocument()).thenReturn(out);
        Mockito.when(result.getWriter()).thenReturn(writer);
        Mockito.when(writer.toString()).thenReturn("document string");
        
        String docString = w3cDom.asString(doc);
        
        Assertions.assertEquals("document string", docString);
        verify(result).getWriter();
        verify(jsoupDoc).child(0);
    }
}