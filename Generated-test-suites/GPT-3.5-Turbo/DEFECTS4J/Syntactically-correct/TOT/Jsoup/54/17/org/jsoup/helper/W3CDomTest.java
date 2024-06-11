import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.w3c.dom.*;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup_withValidDocument_returnsValidW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);

        when(jsoupDoc.child(0)).thenReturn(rootEl);
        when(rootEl.tagName()).thenReturn("html");

        Element root = mock(Element.class);
        Document w3cDoc = mock(Document.class);

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        verify(w3cDoc).setDocumentURI(null);
        verify(w3cDoc).createElementNS(null, "html");
        verify(root).appendChild(any(Element.class));
        assertEquals(w3cDoc.getElementsByTagName("html").getLength(), 1);
    }

    @Test
    public void testFromJsoup_withNullDocument_throwsIllegalArgumentException() {
        W3CDom w3cDom = new W3CDom();
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(null);
        });
    }

    @Test
    public void testConvert_withValidJsoupDocumentAndW3CDocument_modifiesW3CDocumentCorrectly() {
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        org.jsoup.nodes.Element rootEl = mock(org.jsoup.nodes.Element.class);

        when(jsoupDoc.child(0)).thenReturn(rootEl);
        when(rootEl.tagName()).thenReturn("html");

        Element root = mock(Element.class);
        Document w3cDoc = mock(Document.class);

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        verify(w3cDoc).setDocumentURI(null);
        verify(w3cDoc).createElementNS(null, "html");
        verify(root).appendChild(any(Element.class));
        assertEquals(w3cDoc.getElementsByTagName("html").getLength(), 1);
    }

    @Test
    public void testConvert_withJsoupDocumentContainingLocationAttribute_setsDocumentURIOfW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);

        when(jsoupDoc.location()).thenReturn("http://example.com");

        Element root = mock(Element.class);
        Document w3cDoc = mock(Document.class);

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        verify(w3cDoc).setDocumentURI("http://example.com");
    }

    @Test
    public void testW3CBuilder_head_withElementAppendsElementToW3CDocument() {
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Element w3cElement = mock(Element.class);
        NodeTraversor traversor = mock(NodeTraversor.class);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder((Document) w3cElement);
        builder.dest = w3cElement;

        when(jsoupElement.tagName()).thenReturn("div");
        when(jsoupDoc.createElementNS(null, "div")).thenReturn((org.w3c.dom.Element) w3cElement);
        when(jsoupElement.childNodeSize()).thenReturn(0);
        when(jsoupElement.childNode(0)).thenReturn(null);
        when(jsoupDoc.createTextNode("")).thenReturn(mock(Text.class));

        builder.head(jsoupElement, 0);

        verify(jsoupDoc).createElementNS(null, "div");
        verify(w3cElement, times(0)).setPrefix(null);
        verify(w3cElement).appendChild(any(Element.class));
    }

    @Test
    public void testW3CBuilder_head_withTextNodeAppendsTextToW3CDocument() {
        org.jsoup.nodes.TextNode jsoupTextNode = mock(org.jsoup.nodes.TextNode.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Document w3cDoc = mock(Document.class);
        Text w3cText = mock(Text.class);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.dest = mock(Element.class);

        when(jsoupTextNode.getWholeText()).thenReturn("text");

        builder.head(jsoupTextNode, 0);

        verify(w3cDoc).createTextNode("text");
        verify(builder.dest).appendChild(w3cText);
    }

    @Test
    public void testW3CBuilder_head_withCommentAppendsCommentToW3CDocument() {
        org.jsoup.nodes.Comment jsoupComment = mock(org.jsoup.nodes.Comment.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Document w3cDoc = mock(Document.class);
        Comment w3cComment = mock(Comment.class);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.dest = mock(Element.class);

        when(jsoupComment.getData()).thenReturn("comment");

        builder.head(jsoupComment, 0);

        verify(w3cDoc).createComment("comment");
        verify(builder.dest).appendChild(w3cComment);
    }

    @Test
    public void testW3CBuilder_head_withDataNodeAppendsNodeToW3CDocument() {
        org.jsoup.nodes.DataNode jsoupDataNode = mock(org.jsoup.nodes.DataNode.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Document w3cDoc = mock(Document.class);
        Text w3cText = mock(Text.class);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.dest = mock(Element.class);

        when(jsoupDataNode.getWholeData()).thenReturn("data");

        builder.head(jsoupDataNode, 0);

        verify(w3cDoc).createTextNode("data");
        verify(builder.dest).appendChild(w3cText);
    }

    @Test
    public void testW3CBuilder_tail_withElementAndParentElement_setsDestToParentElement() {
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Document w3cDoc = mock(Document.class);
        Element w3cParentElement = mock(Element.class);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.dest = mock(Element.class);

        when(jsoupElement.getParentNode()).thenReturn((org.w3c.dom.Element) w3cParentElement);

        builder.tail(jsoupElement, 0);

        assertEquals(builder.dest, w3cParentElement);
    }

    @Test
    public void testCopyAttributes_withValidJsoupNode_copiesAttributesToW3CDocument() {
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Element w3cElement = mock(Element.class);
        Attribute jsoupAttribute = mock(Attribute.class);

        when(jsoupAttribute.getKey()).thenReturn("attr");
        when(jsoupAttribute.getValue()).thenReturn("value");
        when(jsoupElement.attributes()).thenReturn(new Attributes());
        jsoupElement.attributes().put(jsoupAttribute);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder((Document) w3cElement);

        builder.copyAttributes(jsoupElement, w3cElement);

        verify(w3cElement).setAttribute("attr", "value");
    }

    @Test
    public void testCopyAttributes_withAttributeKeyContainingInvalidCharacters_removesInvalidCharacters() {
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Document jsoupDoc = mock(org.jsoup.nodes.Document.class);
        Element w3cElement = mock(Element.class);
        Attribute jsoupAttribute = mock(Attribute.class);

        when(jsoupAttribute.getKey()).thenReturn("attr!");
        when(jsoupAttribute.getValue()).thenReturn("value");
        when(jsoupElement.attributes()).thenReturn(new Attributes());
        jsoupElement.attributes().put(jsoupAttribute);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder((Document) w3cElement);

        builder.copyAttributes(jsoupElement, w3cElement);

        verify(w3cElement).setAttribute("attr", "value");
    }

    @Test
    public void testUpdateNamespaces_withValidJsoupElement_returnsPrefix() {
        org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
        Attribute jsoupAttribute = mock(Attribute.class);
        HashMap<String, String> namespaces = new HashMap<>();

        when(jsoupElement.attributes()).thenReturn(new Attributes());
        when(jsoupAttribute.getKey()).thenReturn("xmlns:prefix");
        when(jsoupAttribute.getValue()).thenReturn("urn");

        jsoupElement.attributes().put(jsoupAttribute);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        builder.namespaces.putAll(namespaces);

        String prefix = builder.updateNamespaces(jsoupElement);

        assertEquals(prefix, "prefix");
        assertEquals(namespaces.get("prefix"), "urn");
    }

    @Test
    public void testAsString_withValidW3CDocument_returnsStringRepresentation() throws TransformerException {
        Document w3cDoc = mock(Document.class);
        DOMSource domSource = mock(DOMSource.class);
        StringWriter writer = mock(StringWriter.class);
        StreamResult result = mock(StreamResult.class);
        TransformerFactory tf = mock(TransformerFactory.class);
        Transformer transformer = mock(Transformer.class);

        when(w3cDoc.toString()).thenReturn("<xml></xml>");
        when(domSource.getNode()).thenReturn(w3cDoc);
        when(writer.toString()).thenReturn("<xml></xml>");

        W3CDom w3cDom = new W3CDom();
        String resultString = w3cDom.asString(w3cDoc);

        verify(w3cDoc).toString();
        verify(tf).newTransformer();
        verify(transformer).transform(domSource, result);
        assertEquals(resultString, "<xml></xml>");
    }

    @Test
    public void testAsString_withTransformerException_throwsIllegalStateException() throws TransformerException {
        Document w3cDoc = mock(Document.class);
        DOMSource domSource = mock(DOMSource.class);
        StreamResult result = mock(StreamResult.class);
        TransformerFactory tf = mock(TransformerFactory.class);
        Transformer transformer = mock(Transformer.class);

        when(w3cDoc.toString()).thenReturn("<xml></xml>");
        when(domSource.getNode()).thenReturn(w3cDoc);
        when(tf.newTransformer()).thenReturn(transformer);
        when(transformer.transform(domSource, result)).thenThrow(TransformerException.class);

        W3CDom w3cDom = new W3CDom();

        assertThrows(IllegalStateException.class, () -> {
            w3cDom.asString(w3cDoc);
        });
    }
}