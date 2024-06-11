import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private DocumentBuilder documentBuilder;
    private Document document;
    private DocumentBuilderFactory documentBuilderFactory;
    
    @Before
    public void setup() throws ParserConfigurationException {
        w3cDom = new W3CDom();
        
        jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Element element = mock(Element.class);
        when(element.tagName()).thenReturn("test");
        when(jsoupDoc.child(0)).thenReturn(element);
        
        documentBuilder = mock(DocumentBuilder.class);
        document = mock(Document.class);
        documentBuilderFactory = mock(DocumentBuilderFactory.class);
        
        when(documentBuilderFactory.newInstance()).thenReturn(documentBuilderFactory);
        when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
        when(documentBuilder.newDocument()).thenReturn(document);
    }
    
    @Test
    public void testFromJsoupNotNull() {
        Document result = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(result);
    }
    
    @Test
    public void testConvert() {
        w3cDom.convert(jsoupDoc, document);
        verify(jsoupDoc).location();
        verify(document).setDocumentURI(anyString());
        verify(jsoupDoc).child(0);
        verify(element, times(2)).tagName();
        verify(documentBuilder, times(2)).newDocument();
        verify(document).appendChild(any(Element.class));
        verify(document, times(2)).createElementNS(anyString(), anyString());
        verify(element, times(2)).attributes();
        verify(element, times(2)).childNodes();
        verify(element).childNode(0);
        verify(element, times(2)).siblingIndex();
        verify(element, times(2)).childNodeSize();
        verify(element).tag();
        verify(element, times(2)).className();
        verify(element, times(2)).id();
        verify(element, times(2)).ownText();
        verify(element, times(2)).text();
        verify(element).attributes();
        verify(element, times(2)).attributes();
        verify(element).childNode(0);
        verify(element, times(2)).getElementsByTag(anyString());
        verify(element).previousSibling();
        verify(element).nextSibling();
        verify(element).childNodes();
        verify(document).createTextNode(anyString());
        verify(element).appendChild(any(org.w3c.dom.Node.class));
        verify(element).getElementsByTag(anyString());
        verify(element).getAllElements();
    }
    
    @Test
    public void testW3CBuilder() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(document);
        TextNode textNode = mock(TextNode.class);
        org.jsoup.nodes.Node sourceNode = mock(org.jsoup.nodes.Node.class);
        
        builder.head(sourceNode, 0);
        verify(namespacesStack).push(any(HashMap.class));
        verify(textNode).getWholeText();
        verify(document).createTextNode(anyString());
        verify(element).appendChild(any(org.w3c.dom.Node.class));
        verify(element).tagName();
        
        builder.tail(sourceNode, 0);
        verify(element).getParentNode();
        verify(namespacesStack).pop();
    }
    
    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(document);
        org.jsoup.nodes.Node sourceNode = mock(org.jsoup.nodes.Node.class);
        org.jsoup.nodes.Attributes attributes = mock(org.jsoup.nodes.Attributes.class);
        Attribute attribute = mock(Attribute.class);
        
        when(sourceNode.attributes()).thenReturn(attributes);
        when(attributes.iterator()).thenReturn(Arrays.asList(attribute).iterator());
        when(attribute.getKey()).thenReturn("key");
        when(attribute.getValue()).thenReturn("value");
        
        builder.copyAttributes(sourceNode, element);
        verify(sourceNode).attributes();
        verify(attributes).iterator();
        verify(attribute).getKey();
        verify(attribute).getValue();
        verify(element).setAttribute(anyString(), anyString());
    }
    
    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(document);
        org.jsoup.nodes.Element el = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Attributes attributes = mock(org.jsoup.nodes.Attributes.class);
        Attribute attribute = mock(Attribute.class);
        
        when(el.attributes()).thenReturn(attributes);
        when(attributes.iterator()).thenReturn(Arrays.asList(attribute).iterator());
        when(attribute.getKey()).thenReturn("xmlns:");
        
        builder.updateNamespaces(el);
        verify(el).attributes();
        verify(attributes).iterator();
        verify(attribute).getKey();
        verify(attribute).getValue();
    }
    
    @Test
    public void testAsString() {
        String result = w3cDom.asString(document);
        assertNotNull(result);
    }
}