import static org.mockito.Mockito.*;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.w3c.dom.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    private W3CDom w3cDom;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    @Before
    public void setup() throws ParserConfigurationException {
        w3cDom = new W3CDom();
        documentBuilderFactory = mock(DocumentBuilderFactory.class);
        documentBuilder = mock(DocumentBuilder.class);
        when(documentBuilderFactory.newInstance()).thenReturn(documentBuilderFactory);
        when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Document out = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(out);
        verify(documentBuilder, times(1)).newDocument();
        verify(documentBuilder, times(1)).setNamespaceAware(true);
        verify(jsoupDoc, times(1)).location();
        verify(out, times(1)).setDocumentURI(anyString());
        verify(w3cDom, times(1)).convert(any(), any());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Element jsoupRootEl = mock(Element.class);
        when(jsoupDoc.child(0)).thenReturn(jsoupRootEl);
        Element w3cRootEl = mock(Element.class);
        Document out = mock(Document.class);
        when(out.getDocumentURI()).thenReturn("http://example.com");
        when(out.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public Node item(int index) {
                return w3cRootEl;
            }

            @Override
            public int getLength() {
                return 1;
            }
        });
        w3cDom.convert(jsoupDoc, out);
        verify(jsoupDoc, times(1)).location();
        verify(jsoupDoc, times(1)).child(0);
        verify(jsoupRootEl, times(1)).tagName();
        verify(out, times(1)).setDocumentURI(anyString());
        verify(out, times(1)).createElementNS(anyString(), anyString());
        verify(w3cRootEl, times(1)).appendChild(any());
        verify(w3cDom, times(1)).copyAttributes(any(), any());
        verify(w3cDom, times(1)).updateNamespaces(any());
    }

    @Test
    public void testW3CBuilder_head_ElementSource() {
        org.jsoup.nodes.Node source = mock(org.jsoup.nodes.Element.class);
        Element element = mock(Element.class);
        Document out = mock(Document.class);
        when(documentBuilderFactory.newDocument()).thenReturn(out);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.head(source, 1);
        verify(out, times(1)).appendChild(any());
    }

    @Test
    public void testW3CBuilder_head_TextNodeSource() {
        org.jsoup.nodes.Node source = mock(org.jsoup.nodes.TextNode.class);
        Text text = mock(Text.class);
        Document out = mock(Document.class);
        when(out.createElementNS(anyString(), anyString())).thenReturn(text);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.head(source, 1);
        verify(out, times(1)).createElementNS(anyString(), anyString());
        verify(text, times(1)).setData(anyString());
        verify(out, times(1)).appendChild(any());
    }

    @Test
    public void testW3CBuilder_head_CommentSource() {
        org.jsoup.nodes.Node source = mock(org.jsoup.nodes.Comment.class);
        Comment comment = mock(Comment.class);
        Document out = mock(Document.class);
        when(out.createElementNS(anyString(), anyString())).thenReturn(comment);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.head(source, 1);
        verify(out, times(1)).createElementNS(anyString(), anyString());
        verify(comment, times(1)).setData(anyString());
        verify(out, times(1)).appendChild(any());
    }

    @Test
    public void testW3CBuilder_head_DataNodeSource() {
        org.jsoup.nodes.Node source = mock(org.jsoup.nodes.DataNode.class);
        org.w3c.dom.Text node = mock(org.w3c.dom.Text.class);
        Document out = mock(Document.class);
        when(out.createElementNS(anyString(), anyString())).thenReturn(node);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.head(source, 1);
        verify(out, times(1)).createElementNS(anyString(), anyString());
        verify(node, times(1)).setData(anyString());
        verify(out, times(1)).appendChild(any());
    }

    @Test
    public void testW3CBuilder_tail() {
        org.jsoup.nodes.Node source = mock(org.jsoup.nodes.Element.class);
        org.w3c.dom.Element el = mock(org.w3c.dom.Element.class);
        org.w3c.dom.Node parentNode = mock(org.w3c.dom.Node.class);
        when(el.getParentNode()).thenReturn(parentNode);
        Document out = mock(Document.class);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.dest = el;
        w3cBuilder.tail(source, 1);
        verify(el, times(1)).getParentNode();
    }

    @Test
    public void testW3CBuilder_copyAttributes() {
        org.jsoup.nodes.Element source = mock(org.jsoup.nodes.Element.class);
        Attribute attribute = mock(Attribute.class);
        org.w3c.dom.Element el = mock(org.w3c.dom.Element.class);
        when(source.attributes()).thenReturn(new Attributes() {
            @Override
            public String getIgnoreCase(String key) {
                return attribute.getValue();
            }

            @Override
            public Attribute get(String key) {
                return attribute;
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public Attribute remove(String key) {
                return null;
            }

            @Override
            public boolean hasKeyIgnoreCase(String key) {
                return true;
            }

            @Override
            public Iterator<Attribute> iterator() {
                return Collections.singletonList(attribute).iterator();
            }
        });
        when(attribute.getKey()).thenReturn("testKey");
        when(attribute.getValue()).thenReturn("testValue");
        Document out = mock(Document.class);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.copyAttributes(source, el);
        verify(el, times(1)).setAttribute(anyString(), anyString());
    }

    @Test
    public void testW3CBuilder_updateNamespaces() {
        org.jsoup.nodes.Element el = mock(org.jsoup.nodes.Element.class);
        Attribute attr = mock(Attribute.class);
        when(el.attributes()).thenReturn(new Attributes() {
            @Override
            public String getIgnoreCase(String key) {
                return attr.getValue();
            }

            @Override
            public Attribute get(String key) {
                return attr;
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public Attribute remove(String key) {
                return null;
            }

            @Override
            public boolean hasKeyIgnoreCase(String key) {
                return true;
            }

            @Override
            public Iterator<Attribute> iterator() {
                return Collections.singletonList(attr).iterator();
            }
        });
        when(attr.getKey()).thenReturn("testKey");
        when(attr.getValue()).thenReturn("testValue");
        Document out = mock(Document.class);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        String result = w3cBuilder.updateNamespaces(el);
        Assert.assertEquals("", result);
        verify(w3cBuilder, times(1)).updateNamespaces(any());
        verify(w3cBuilder, times(1)).updateNamespaces(any());
    }

    @Test
    public void testAsString() {
        Document doc = mock(Document.class);
        DOMSource domSource = mock(DOMSource.class);
        StringWriter writer = mock(StringWriter.class);
        StreamResult result = mock(StreamResult.class);
        TransformerFactory tf = mock(TransformerFactory.class);
        Transformer transformer = mock(Transformer.class);
        when(transformerFactory.newInstance()).thenReturn(tf);
        when(tf.newTransformer()).thenReturn(transformer);
        when(tf.transform(domSource, result)).thenReturn(anyString());
        W3CDom w3cDom;
        w3cDom.asString(doc);
        verify(domSource, times(1)).setNode(any());
        verify(transformer, times(1)).transform(domSource, result);
    }
}