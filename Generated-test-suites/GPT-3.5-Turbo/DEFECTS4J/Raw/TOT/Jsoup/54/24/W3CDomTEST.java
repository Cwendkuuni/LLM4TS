import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws Exception {
        w3cDom = new W3CDom();
        jsoupDoc = mock(org.jsoup.nodes.Document.class);
        documentBuilder = mock(DocumentBuilder.class);
        when(jsoupDoc.location()).thenReturn("http://example.com");
    }

    @Test
    public void testFromJsoup_validInput_returnsW3CDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
        when(documentBuilder.newDocument()).thenReturn(mock(Document.class));

        Document result = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(result);
        verify(documentBuilderFactory).setNamespaceAware(true);
        verify(documentBuilderFactory).newDocumentBuilder();
        verify(documentBuilder).newDocument();
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_nullInput_throwsIllegalStateException() throws ParserConfigurationException {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_validInput_modifiesTargetW3CDocument() {
        Document w3cDoc = mock(Document.class);
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);
        NodeTraversor traversor = mock(NodeTraversor.class);
        W3CBuilder w3cBuilder = mock(W3CBuilder.class);
        when(jsoupDoc.child(0)).thenReturn(rootEl);
        when(mock(NodeTraversor.class).traverse(rootEl)).thenReturn(traversor);
        whenNew(W3CBuilder.class).withArguments(w3cDoc).thenReturn(w3cBuilder);

        w3cDom.convert(jsoupDoc, w3cDoc);

        verify(rootEl).tagName();
        verifyNew(W3CBuilder.class).withArguments(w3cDoc);
        verify(traversor).traverse(rootEl);
    }

    @Test
    public void testConvert_nonBlankLocation_setsDocumentURI() {
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);
        when(jsoupDoc.child(0)).thenReturn(rootEl);
        when(rootEl.tagName()).thenReturn("html");
        when(jsoupDoc.location()).thenReturn("http://example.com");
        Document w3cDoc = mock(Document.class);
        W3CBuilder w3cBuilder = mock(W3CBuilder.class);
        whenNew(W3CBuilder.class).withArguments(w3cDoc).thenReturn(w3cBuilder);

        w3cDom.convert(jsoupDoc, w3cDoc);

        verify(w3cDoc).setDocumentURI("http://example.com");
    }

    @Test
    public void testConvert_blankLocation_doesNotSetDocumentURI() {
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);
        when(jsoupDoc.child(0)).thenReturn(rootEl);
        when(rootEl.tagName()).thenReturn("html");
        when(jsoupDoc.location()).thenReturn("");
        Document w3cDoc = mock(Document.class);
        W3CBuilder w3cBuilder = mock(W3CBuilder.class);
        whenNew(W3CBuilder.class).withArguments(w3cDoc).thenReturn(w3cBuilder);

        w3cDom.convert(jsoupDoc, w3cDoc);

        verify(w3cDoc, never()).setDocumentURI(anyString());
    }

    @Test
    public void testW3CBuilder_head_elementSource_createsW3CElementWithNamespaceAndAttributes() {
        org.jsoup.nodes.Element source = mock(org.jsoup.nodes.Element.class);
        when(source.attributes()).thenReturn(mock(Attributes.class));
        when(source.tagName()).thenReturn("div");
        when(source.attributes().iterator()).thenReturn(mock(Iterator.class));

        Document w3cDoc = mock(Document.class);
        Element w3cElement = mock(Element.class);
        when(w3cDoc.createElementNS(any(), any())).thenReturn(w3cElement);
        when(w3cDoc.appendChild(any())).thenReturn(null);
        when(w3cElement.getParentNode()).thenReturn(null);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(source, 0);

        verify(w3cDoc).createElementNS(any(), any());
        verify(w3cElement).getParentNode();
        verify(w3cDoc).appendChild(any());
    }

    @Test
    public void testW3CBuilder_head_textSource_createsW3CText() {
        org.jsoup.nodes.TextNode source = mock(org.jsoup.nodes.TextNode.class);
        when(source.getWholeText()).thenReturn("Text");

        Document w3cDoc = mock(Document.class);
        Text w3cText = mock(Text.class);
        when(w3cDoc.createTextNode(any())).thenReturn(w3cText);
        when(w3cText.getTextContent()).thenReturn("Text");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(source, 0);

        verify(w3cDoc).createTextNode("Text");
    }

    @Test
    public void testW3CBuilder_head_commentSource_createsW3CComment() {
        org.jsoup.nodes.Comment source = mock(org.jsoup.nodes.Comment.class);
        when(source.getData()).thenReturn("Comment");

        Document w3cDoc = mock(Document.class);
        Comment w3cComment = mock(Comment.class);
        when(w3cDoc.createComment(any())).thenReturn(w3cComment);
        when(w3cComment.getData()).thenReturn("Comment");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(source, 0);

        verify(w3cDoc).createComment("Comment");
    }

    @Test
    public void testW3CBuilder_head_dataNodeSource_createsW3CDataNode() {
        org.jsoup.nodes.DataNode source = mock(org.jsoup.nodes.DataNode.class);
        when(source.getWholeData()).thenReturn("Data");

        Document w3cDoc = mock(Document.class);
        Text w3cTextNode = mock(Text.class);
        when(w3cDoc.createTextNode(any())).thenReturn(w3cTextNode);
        when(w3cTextNode.getNodeValue()).thenReturn("Data");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(source, 0);

        verify(w3cDoc).createTextNode("Data");
    }

    @Test
    public void testW3CBuilder_tail_elementSource_undescendsW3CElement() {
        org.jsoup.nodes.Element source = mock(org.jsoup.nodes.Element.class);
        org.w3c.dom.Element dest = mock(org.w3c.dom.Element.class);
        when(source.getParent()).thenReturn(source);
        when(dest.getParentNode()).thenReturn(dest);

        Document w3cDoc = mock(Document.class);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.dest = dest;
        w3cBuilder.tail(source, 0);

        verify(w3cBuilder).dest = dest;
    }

    @Test
    public void testCopyAttributes_validInput_copiesAttributesToW3CElement() {
        org.jsoup.nodes.Node source = mock(org.jsoup.nodes.Node.class);
        org.w3c.dom.Element el = mock(org.w3c.dom.Element.class);
        org.jsoup.nodes.Attributes attributes = mock(Attributes.class);
        Iterator<org.jsoup.nodes.Attribute> iterator = mock(Iterator.class);
        org.jsoup.nodes.Attribute attribute = mock(org.jsoup.nodes.Attribute.class);
        when(source.attributes()).thenReturn(attributes);
        when(attributes.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(attribute);
        when(attribute.getKey()).thenReturn("key");
        when(attribute.getValue()).thenReturn("value");

        w3cDom.copyAttributes(source, el);

        verify(el).setAttribute("key", "value");
    }

    @Test
    public void testUpdateNamespaces_validElement_updatesNamespacesMapAndReturnsTagPrefix() {
        org.jsoup.nodes.Element el = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Attributes attributes = mock(Attributes.class);
        Iterator<org.jsoup.nodes.Attribute> iterator = mock(Iterator.class);
        when(el.attributes()).thenReturn(attributes);
        when(attributes.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        org.jsoup.nodes.Attribute prefixAttr = mock(org.jsoup.nodes.Attribute.class);
        org.jsoup.nodes.Attribute urnAttr = mock(org.jsoup.nodes.Attribute.class);
        when(iterator.next()).thenReturn(prefixAttr, urnAttr);
        when(prefixAttr.getKey()).thenReturn("xmlns:prefix");
        when(prefixAttr.getValue()).thenReturn("urn:prefix");
        when(urnAttr.getKey()).thenReturn("xmlns");
        when(urnAttr.getValue()).thenReturn("urn");

        String result = w3cDom.updateNamespaces(el);

        assertEquals("prefix", result);
        assertEquals(2, w3cDom.namespaces.size());
        assertEquals("urn", w3cDom.namespaces.get(""));
        assertEquals("urn:prefix", w3cDom.namespaces.get("prefix"));
    }

    @Test
    public void testAsString_validDocument_returnsSerializedString() throws TransformerException {
        Document doc = mock(Document.class);
        DOMSource domSource = mock(DOMSource.class);
        StreamResult streamResult = mock(StreamResult.class);
        StringWriter writer = mock(StringWriter.class);
        TransformerFactory transformerFactory = mock(TransformerFactory.class);
        Transformer transformer = mock(Transformer.class);
        whenNew(DOMSource.class).withArguments(doc).thenReturn(domSource);
        whenNew(StreamResult.class).withArguments(writer).thenReturn(streamResult);
        whenNew(StringWriter.class).withNoArguments().thenReturn(writer);
        when(transformerFactory.newTransformer()).thenReturn(transformer);
        when(writer.toString()).thenReturn("SerializedString");

        String result = w3cDom.asString(doc);

        assertEquals("SerializedString", result);
        verifyNew(DOMSource.class).withArguments(doc);
        verifyNew(StreamResult.class).withArguments(writer);
        verifyNew(StringWriter.class).withNoArguments();
        verify(transformerFactory).newTransformer();
        verify(transformer).transform(domSource, streamResult);
    }
}