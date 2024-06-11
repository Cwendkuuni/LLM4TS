

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDocMock;
    private org.jsoup.nodes.Element jsoupRootElMock;
    private DocumentBuilder documentBuilderMock;
    private DocumentBuilderFactory documentBuilderFactoryMock;
    private Document outMock;

    @Before
    public void setUp() throws Exception {
        w3CDom = new W3CDom();
        jsoupDocMock = mock(org.jsoup.nodes.Document.class);
        jsoupRootElMock = mock(org.jsoup.nodes.Element.class);
        documentBuilderMock = mock(DocumentBuilder.class);
        documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        outMock = mock(Document.class);
    }

    @Test
    public void testFromJsoup_withValidJsoupDocument_returnW3cDocument() throws Exception {
        // Arrange
        when(documentBuilderFactoryMock.newInstance()).thenReturn(documentBuilderFactoryMock);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        when(documentBuilderMock.newDocument()).thenReturn(outMock);
        when(jsoupDocMock.child(0)).thenReturn(jsoupRootElMock);

        // Act
        Document result = w3CDom.fromJsoup(jsoupDocMock);

        // Assert
        assertEquals(outMock, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_withInvalidJsoupDocument_throwIllegalStateException() throws Exception {
        // Arrange
        when(documentBuilderFactoryMock.newInstance()).thenThrow(new ParserConfigurationException());

        // Act
        w3CDom.fromJsoup(jsoupDocMock);
    }

    @Test
    public void testConvert_withValidJsoupAndW3cDocuments_convertSuccessfully() throws Exception {
        // Arrange
        String jsoupLocation = "http://example.com";
        String tagName = "div";
        String text = "sample text";
        String commentData = "sample comment";
        String data = "sample data";
        Node nodeMock = mock(Node.class);
        Element elementMock = mock(Element.class);
        Text textMock = mock(Text.class);
        org.jsoup.nodes.TextNode jsoupTextNodeMock = mock(org.jsoup.nodes.TextNode.class);
        org.jsoup.nodes.Comment jsoupCommentMock = mock(org.jsoup.nodes.Comment.class);
        org.jsoup.nodes.DataNode jsoupDataMock = mock(org.jsoup.nodes.DataNode.class);

        when(jsoupDocMock.location()).thenReturn(jsoupLocation);
        when(jsoupRootElMock.tagName()).thenReturn(tagName);
        when(jsoupRootElMock.childNodeCount()).thenReturn(4);
        when(jsoupRootElMock.childNode(0)).thenReturn(jsoupTextNodeMock);
        when(jsoupTextNodeMock.getWholeText()).thenReturn(text);
        when(jsoupRootElMock.childNode(1)).thenReturn(jsoupCommentMock);
        when(jsoupCommentMock.getData()).thenReturn(commentData);
        when(jsoupRootElMock.childNode(2)).thenReturn(jsoupDataMock);
        when(jsoupDataMock.getWholeData()).thenReturn(data);
        when(documentBuilderFactoryMock.newInstance()).thenReturn(documentBuilderFactoryMock);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        when(documentBuilderMock.newDocument()).thenReturn(outMock);
        when(outMock.createElementNS(anyString(), eq(tagName))).thenReturn(elementMock);
        when(outMock.createTextNode(eq(text))).thenReturn(textMock);

        // Act
        w3CDom.convert(jsoupDocMock, outMock);

        // Assert
        verify(outMock).setDocumentURI(eq(jsoupLocation));
        verify(outMock).appendChild(eq(elementMock));
        verify(elementMock).appendChild(eq(textMock));
        verify(elementMock).appendChild(eq(textMock));
        verify(elementMock).appendChild(eq(textMock));
        verify(outMock, times(3)).createTextNode(anyString());
    }

    @Test
    public void testConvert_withBlankJsoupLocation_setDocumentUriAsBlank() throws Exception {
        // Arrange
        when(jsoupDocMock.location()).thenReturn("");

        // Act
        w3CDom.convert(jsoupDocMock, outMock);

        // Assert
        verify(outMock).setDocumentURI(eq(""));
    }

    @Test
    public void testConvert_withNoChildNodes_doNothing() throws Exception {
        // Arrange
        when(jsoupRootElMock.childNodeCount()).thenReturn(0);

        // Act
        w3CDom.convert(jsoupDocMock, outMock);

        // Assert
        verifyNoInteractions(outMock);
    }

    @Test
    public void testConvert_withInvalidJsoupTagName_doNothing() throws Exception {
        // Arrange
        when(jsoupRootElMock.tagName()).thenReturn("");

        // Act
        w3CDom.convert(jsoupDocMock, outMock);

        // Assert
        verifyNoInteractions(outMock);
    }

    @Test
    public void testConvert_withInvalidJsoupChildNode_doNothing() throws Exception {
        // Arrange
        when(jsoupRootElMock.childNodeCount()).thenReturn(1);
        when(jsoupRootElMock.childNode(0)).thenReturn(mock(org.jsoup.nodes.Node.class));

        // Act
        w3CDom.convert(jsoupDocMock, outMock);

        // Assert
        verifyNoInteractions(outMock);
    }

    @Test
    public void testCopyAttributes_withValidJsoupNode_copyAttributesSuccessfully() throws Exception {
        // Arrange
        org.jsoup.nodes.Node attrNodeMock = mock(org.jsoup.nodes.Node.class);
        org.jsoup.nodes.Attribute attributeMock = mock(org.jsoup.nodes.Attribute.class);
        when(attributeMock.getKey()).thenReturn("attr1");
        when(attributeMock.getValue()).thenReturn("value1");
        when(attrNodeMock.attributes()).thenReturn(new org.jsoup.nodes.Attributes().put(attributeMock));

        // Act
        Element el = outMock.createElementNS(null, "tag");
        w3CDom.copyAttributes(attrNodeMock, el);

        // Assert
        assertEquals("value1", el.getAttribute("attr1"));
    }

    @Test
    public void testCopyAttributes_withInvalidJsoupKey_doNotCopyAttribute() throws Exception {
        // Arrange
        org.jsoup.nodes.Node attrNodeMock = mock(org.jsoup.nodes.Node.class);
        org.jsoup.nodes.Attribute attributeMock = mock(org.jsoup.nodes.Attribute.class);
        when(attributeMock.getKey()).thenReturn("invalid-key");
        when(attrNodeMock.attributes()).thenReturn(new org.jsoup.nodes.Attributes().put(attributeMock));

        // Act
        Element el = outMock.createElementNS(null, "tag");
        w3CDom.copyAttributes(attrNodeMock, el);

        // Assert
        assertEquals("", el.getAttribute("invalid-key"));
    }

    @Test
    public void testCopyAttributes_withInvalidJsoupKeyChars_doNotCopyAttribute() throws Exception {
        // Arrange
        org.jsoup.nodes.Node attrNodeMock = mock(org.jsoup.nodes.Node.class);
        org.jsoup.nodes.Attribute attributeMock = mock(org.jsoup.nodes.Attribute.class);
        when(attributeMock.getKey()).thenReturn("invalid$key");
        when(attrNodeMock.attributes()).thenReturn(new org.jsoup.nodes.Attributes().put(attributeMock));

        // Act
        Element el = outMock.createElementNS(null, "tag");
        w3CDom.copyAttributes(attrNodeMock, el);

        // Assert
        assertEquals("", el.getAttribute("invalid$key"));
    }

    @Test
    public void testAsString_withValidW3cDocument_returnDocumentAsString() throws Exception {
        // Arrange
        DOMSource domSourceMock = mock(DOMSource.class);
        StringWriter writerMock = mock(StringWriter.class);
        StreamResult resultMock = mock(StreamResult.class);
        TransformerFactory transformerFactoryMock = mock(TransformerFactory.class);
        Transformer transformerMock = mock(Transformer.class);

        when(documentBuilderFactoryMock.newInstance()).thenReturn(documentBuilderFactoryMock);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        when(documentBuilderMock.newDocument()).thenReturn(outMock);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        when(outMock.createElementNS(anyString(), anyString())).thenReturn(mock(Element.class));
        when(outMock.createTextNode(anyString())).thenReturn(mock(Text.class));
        when(outMock.getDocumentElement()).thenReturn(mock(Element.class));
        when(outMock.getDoctype()).thenReturn(null);
        when(transformerFactoryMock.newInstance()).thenReturn(transformerFactoryMock);
        when(transformerFactoryMock.newTransformer()).thenReturn(transformerMock);
        when(transformerMock.transform(any(DOMSource.class), any(StreamResult.class))).thenReturn(null);
        when(transformerMock.transform(domSourceMock, resultMock)).thenReturn(null);
        when(resultMock.getWriter()).thenReturn(writerMock);
        when(writerMock.toString()).thenReturn("result");

        // Act
        String result = w3CDom.asString(outMock);

        // Assert
        assertEquals("result", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_withInvalidW3cDocument_throwIllegalStateException() throws Exception {
        // Arrange
        when(documentBuilderFactoryMock.newInstance()).thenReturn(documentBuilderFactoryMock);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        when(documentBuilderMock.newDocument()).thenReturn(outMock);
        when(outMock.getDocumentElement()).thenReturn(mock(Element.class));
        when(outMock.getDoctype()).thenReturn(null);
        when(transformerFactoryMock.newInstance()).thenReturn(transformerFactoryMock);
        when(transformerFactoryMock.newTransformer()).thenThrow(new TransformerException());

        // Act
        w3CDom.asString(outMock);
    }

}
