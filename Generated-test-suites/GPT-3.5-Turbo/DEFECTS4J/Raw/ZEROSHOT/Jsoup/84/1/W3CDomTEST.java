import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.W3CDom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {
    
    @Mock
    private DocumentBuilder documentBuilder;

    @Before
    public void setup() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
    }

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();

        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        assertNotNull(jsoupDoc.location());

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();

        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.NodeList nodeList = mock(org.jsoup.nodes.NodeList.class);
        org.jsoup.nodes.Node node = mock(org.jsoup.nodes.Node.class);
        when(jsoupDoc.child(0)).thenReturn(rootEl);
        when(rootEl.childNodes()).thenReturn(nodeList);
        when(nodeList.size()).thenReturn(1);
        when(nodeList.get(0)).thenReturn(node);
        when(node.childNodeSize()).thenReturn(1);
        when(node.childNode(0)).thenReturn(node);

        documentBuilder = mock(DocumentBuilder.class);
        when(documentBuilder.newDocument()).thenReturn(mock(Document.class));

        Document w3cDoc = mock(Document.class);
        w3cDom.convert(jsoupDoc, w3cDoc);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(mock(Document.class));
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Attribute attribute = mock(org.jsoup.nodes.Attribute.class);
        when(attribute.getKey()).thenReturn("key");
        when(attribute.getValue()).thenReturn("value");
        when(jsoupElement.attributes()).thenReturn(new org.jsoup.nodes.Attributes().add(attribute));

        Element w3cElement = mock(Element.class);
        w3cBuilder.copyAttributes(jsoupElement, w3cElement);
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(mock(Document.class));
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Attributes attributes = mock(org.jsoup.nodes.Attributes.class);
        org.jsoup.nodes.Attribute attribute = mock(org.jsoup.nodes.Attribute.class);
        when(jsoupElement.attributes()).thenReturn(attributes);
        when(attributes.size()).thenReturn(1);
        when(attributes.get(0)).thenReturn(attribute);
        when(attribute.getKey()).thenReturn("key");
        when(attribute.getValue()).thenReturn("value");
        when(jsoupElement.tagName()).thenReturn("tag");

        w3cBuilder.updateNamespaces(jsoupElement);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();

        Document w3cDoc = mock(Document.class);
        w3cDom.asString(w3cDoc);
    }
}