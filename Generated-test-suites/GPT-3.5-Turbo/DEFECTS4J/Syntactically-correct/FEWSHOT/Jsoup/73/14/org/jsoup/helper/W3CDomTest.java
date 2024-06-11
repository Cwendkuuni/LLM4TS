package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDocument;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDocument = new Document("");
    }

    @Test
    public void testFromJsoup_NullInput_ThrowsException() {
        try {
            w3cDom.fromJsoup(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Input document must not be null", e.getMessage());
        }
    }

    @Test
    public void testConvert_SetLocation() {
        jsoupDocument.setBaseUri("http://example.com");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertEquals("http://example.com", w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvert_WithoutLocation() {
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertEquals("", w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvert_ConvertDocument() throws IOException, ParserConfigurationException {
        DocumentBuilder mockBuilder = mock(DocumentBuilder.class);
        DocumentBuilderFactory mockFactory = mock(DocumentBuilderFactory.class);
        NodeTraversor mockNodeTraversor = mock(NodeTraversor.class);
        W3CDom.W3CBuilder mockW3CBuilder = mock(W3CDom.W3CBuilder.class);
        Node mockNode = mock(Node.class);
        Element mockElement = mock(Element.class);
        Comment mockComment = mock(Comment.class);
        TextNode mockTextNode = mock(TextNode.class);

        when(mockFactory.newDocumentBuilder()).thenReturn(mockBuilder);
        when(mockFactory.newInstance()).thenReturn(mockFactory);
        when(mockBuilder.newDocument()).thenReturn((org.w3c.dom.Document) mockNode);
        when(mockElement.tagName()).thenReturn("div");
        when(mockElement.attributes()).thenReturn(jsoupDocument.attributes());
        when(mockNodeTraversor.traverse(mockW3CBuilder, jsoupDocument.child(0))).thenReturn(mockNode);
        when(mockNode.getParentNode()).thenReturn((Node) mockElement);
        when(mockComment.getData()).thenReturn("comment");
        when(mockTextNode.getWholeText()).thenReturn("text");

        w3cDom.factory = mockFactory;
        w3cDom.convert(jsoupDocument, (org.w3c.dom.Document) mockNode);

        Mockito.verify(mockFactory).newDocumentBuilder();
        Mockito.verify(mockFactory).newInstance();
        Mockito.verify(mockBuilder).newDocument();
        Mockito.verify(mockNodeTraversor).traverse(mockW3CBuilder, jsoupDocument.child(0));
        Mockito.verify(mockNode).getParentNode();
        Mockito.verify(mockElement).setAttribute(Matchers.anyString(), Matchers.anyString());
        Mockito.verify(mockElement).tagName();
        Mockito.verify(mockElement).attributes();
        Mockito.verify(mockNode, Mockito.times(2)).appendChild(Mockito.any());
        Mockito.verify(mockComment).getData();
        Mockito.verify(mockTextNode).getWholeText();
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testConvert_ExceptionThrownDuringParsing() throws ParserConfigurationException {
        DocumentBuilderFactory mockFactory = mock(DocumentBuilderFactory.class);
        DocumentBuilder mockBuilder = mock(DocumentBuilder.class);

        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Error occurred while converting jsoup document to w3c document");

        when(mockFactory.newDocumentBuilder()).thenReturn(mockBuilder);
        when(mockBuilder.newDocument()).thenThrow(ParserConfigurationException.class);

        w3cDom.factory = mockFactory;
        w3cDom.convert(jsoupDocument, mock(org.w3c.dom.Document.class));
    }

    @Test
    public void testW3CBuilder_Head_Element() {
        org.jsoup.nodes.Element mockElement = mock(org.jsoup.nodes.Element.class);
        NamedNodeMap mockAttributes = mock(NamedNodeMap.class);
        Node mockNode = mock(Node.class);
        org.w3c.dom.Document mockDocument = mock(org.w3c.dom.Document.class);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(mockDocument);

        when(mockElement.tagName()).thenReturn("div");
        when(mockElement.attributes()).thenReturn(jsoupDocument.attributes());
        when(mockElement.childNode(0)).thenReturn(mockNode);
        when(mockDocument.createElementNS("", "div")).thenReturn(mockElement);
        when(mockDocument.createTextNode("")).thenReturn(mock(Node.class));

        w3cBuilder.head(mockElement, 0);

        Mockito.verify(mockElement).attributes();
        Mockito.verify(mockElement).tagName();
        Mockito.verify(mockElement).childNode(0);
        Mockito.verify(mockDocument).createElementNS("", "div");
        Mockito.verify(mockDocument).createTextNode("");
        Mockito.verify(mockDocument).appendChild(mockElement);
    }

    @Test
    public void testW3CBuilder_Head_TextNode() {
        TextNode mockTextNode = mock(TextNode.class);
        org.w3c.dom.Document mockDocument = mock(org.w3c.dom.Document.class);
        Element parentNode = new Element("");

        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(mockDocument);

        when(mockTextNode.getWholeText()).thenReturn("text");
        when(mockTextNode.parentNode()).thenReturn(parentNode);
        when(mockDocument.createTextNode("text")).thenReturn(mock(Node.class));

        w3cBuilder.head(mockTextNode, 0);

        Mockito.verify(mockTextNode).getWholeText();
        Mockito.verify(mockTextNode).parentNode();
        Mockito.verify(mockDocument).createTextNode("text");
        Mockito.verify(parentNode).appendChild(mock(Node.class));
    }

    @Test
    public void testW3CBuilder_Head_Comment() {
        Comment mockComment = mock(Comment.class);
        org.w3c.dom.Document mockDocument = mock(org.w3c.dom.Document.class);
        Element parentNode = new Element("");

        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(mockDocument);

        when(mockComment.getData()).thenReturn("comment");
        when(mockComment.parentNode()).thenReturn(parentNode);

        w3cBuilder.head(mockComment, 0);

        Mockito.verify(mockComment).getData();
        Mockito.verify(mockComment).parentNode();
        Mockito.verify(mockDocument).createComment("comment");
        Mockito.verify(parentNode).appendChild(mock(Node.class));
    }

    @Test
    public void testW3CBuilder_Head_DataNode() {
        org.jsoup.nodes.DataNode mockDataNode = mock(org.jsoup.nodes.DataNode.class);
        org.w3c.dom.Document mockDocument = mock(org.w3c.dom.Document.class);
        Element parentNode = new Element("");

        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(mockDocument);

        when(mockDataNode.getWholeData()).thenReturn("data");
        when(mockDataNode.parentNode()).thenReturn(parentNode);

        w3cBuilder.head(mockDataNode, 0);

        Mockito.verify(mockDataNode).getWholeData();
        Mockito.verify(mockDataNode).parentNode();
        Mockito.verify(mockDocument).createTextNode("data");
        Mockito.verify(parentNode).appendChild(mock(Node.class));
    }

    @Test
    public void testW3CBuilder_Tail() {
        org.jsoup.nodes.Element mockElement = mock(org.jsoup.nodes.Element.class);
        Element mockParentElement = mock(Element.class);
        org.w3c.dom.Document mockDocument = mock(org.w3c.dom.Document.class);

        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(mockDocument);

        w3cBuilder.dest = mockElement;
        when(mockElement.getParentNode()).thenReturn(mockParentElement);

        w3cBuilder.tail(mockElement, 0);

        Mockito.verify(mockElement, Mockito.times(2)).getParentNode();
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document mockDocument = mock(org.w3c.dom.Document.class);
        DOMSource mockDomSource = mock(DOMSource.class);
        StringWriter mockWriter = mock(StringWriter.class);
        StreamResult mockResult = mock(StreamResult.class);
        TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        Transformer mockTransformer = mock(Transformer.class);

        when(mockDocument.getDocumentElement()).thenReturn(mock(Element.class));
        when(mockDocument.getElementsByTagName("*")).thenReturn(mock(NodeList.class));
        when(mockDomSource.getNode()).thenReturn(mock(Document.class));
        when(mockWriter.toString()).thenReturn("");
        when(mockResult.getWriter()).thenReturn(mockWriter);
        when(mockTransformerFactory.newInstance()).thenReturn(mockTransformerFactory);
        when(mockTransformerFactory.newTransformer()).thenReturn(mockTransformer);

        w3cDom.factory = mock(DocumentBuilderFactory.class);
        w3cDom.asString(mockDocument);

        Mockito.verify(mockDocument).getDocumentElement();
        Mockito.verify(mockDocument).getElementsByTagName("*");
        Mockito.verify(mockDomSource).getNode();
        Mockito.verify(mockWriter).toString();
        Mockito.verify(mockResult).getWriter();
        Mockito.verify(mockTransformerFactory).newInstance();
        Mockito.verify(mockTransformerFactory).newTransformer();
        Mockito.verify(mockTransformer).transform(mockDomSource, mockResult);
    }
}