package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDocument;
    
    @Before
    public void setUp() throws ParserConfigurationException {
        w3cDom = new W3CDom();
        jsoupDocument = mock(org.jsoup.nodes.Document.class);
    }
    
    @Test
    public void testFromJsoup() {
        DocumentBuilder documentBuilder = mock(DocumentBuilder.class);
        DocumentBuilderFactory documentBuilderFactory = mock(DocumentBuilderFactory.class);
        Document w3cDocument = mock(Document.class);
        
        when(documentBuilderFactory.newInstance()).thenReturn(documentBuilderFactory);
        when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
        when(documentBuilder.newDocument()).thenReturn(w3cDocument);
        
        Document convertedDocument = w3cDom.fromJsoup(jsoupDocument);
        
        verify(documentBuilderFactory).setNamespaceAware(true);
        verify(documentBuilderFactory).newDocumentBuilder();
        verify(documentBuilder).newDocument();
        
        assertEquals(w3cDocument, convertedDocument);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWhenConfigurationExceptionThrown() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = mock(DocumentBuilderFactory.class);
        
        when(documentBuilderFactory.newInstance()).thenThrow(new ParserConfigurationException());
        
        w3cDom.fromJsoup(jsoupDocument);
        
        verify(documentBuilderFactory).setNamespaceAware(true);
    }
    
    @Test
    public void testConvert() {
        Element jsoupElement = mock(Element.class);
        org.jsoup.nodes.Node jsoupNode = mock(org.jsoup.nodes.Node.class);
        Document w3cDocument = mock(Document.class);
        org.w3c.dom.Element w3cElement = mock(org.w3c.dom.Element.class);
        
        when(jsoupElement.child(0)).thenReturn(jsoupElement);
        when(jsoupElement.location()).thenReturn("");
        when(jsoupDocument.child(0)).thenReturn(jsoupElement);
        when(w3cDocument.appendChild(any(org.w3c.dom.Element.class))).thenReturn(w3cDocument);
        when(w3cDocument.createElementNS(anyString(), anyString())).thenReturn(w3cElement);
        
        w3cDom.convert(jsoupDocument, w3cDocument);
        
        verify(jsoupElement).child(0);
        verify(jsoupElement).location();
        verify(w3cDocument).setDocumentURI("");
        verify(w3cDocument).appendChild(w3cElement);
    }
    
    @Test
    public void testConvertWhenJsoupDocumentHasLocation() {
        Element jsoupElement = mock(Element.class);
        org.jsoup.nodes.Node jsoupNode = mock(org.jsoup.nodes.Node.class);
        Document w3cDocument = mock(Document.class);
        org.w3c.dom.Element w3cElement = mock(org.w3c.dom.Element.class);
        
        when(jsoupElement.child(0)).thenReturn(jsoupElement);
        when(jsoupElement.location()).thenReturn("http://example.com");
        when(jsoupDocument.child(0)).thenReturn(jsoupElement);
        when(w3cDocument.appendChild(any(org.w3c.dom.Element.class))).thenReturn(w3cDocument);
        when(w3cDocument.createElementNS(anyString(), anyString())).thenReturn(w3cElement);
        
        w3cDom.convert(jsoupDocument, w3cDocument);
        
        verify(jsoupElement).child(0);
        verify(jsoupElement).location();
        verify(w3cDocument).setDocumentURI("http://example.com");
        verify(w3cDocument).appendChild(w3cElement);
    }
    
    @Test
    public void testConvertWhenJsoupElementIsNotRootElement() {
        Element jsoupElement = mock(Element.class);
        org.jsoup.nodes.Node jsoupNode = mock(org.jsoup.nodes.Node.class);
        Document w3cDocument = mock(Document.class);
        org.w3c.dom.Element w3cElement = mock(org.w3c.dom.Element.class);
        
        when(jsoupElement.child(0)).thenReturn(jsoupElement);
        when(jsoupElement.location()).thenReturn("");
        when(jsoupDocument.child(0)).thenReturn(jsoupElement);
        when(w3cDocument.appendChild(any(org.w3c.dom.Element.class))).thenReturn(w3cDocument);
        when(w3cDocument.createElementNS(anyString(), anyString())).thenReturn(w3cElement);
        
        w3cDom.convert(jsoupDocument, w3cDocument);
        
        verify(jsoupElement).child(0);
        verify(jsoupElement).location();
        verify(w3cDocument).setDocumentURI("");
        verify(w3cDocument).appendChild(w3cElement);
        verify(w3cElement, times(2)).getParentNode();
    }
    
    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Node jsoupNode = mock(org.jsoup.nodes.Node.class);
        Element w3cElement = mock(Element.class);
        Attributes jsoupAttributes = mock(Attributes.class);
        Attribute jsoupAttribute = mock(Attribute.class);
        
        when(jsoupNode.attributes()).thenReturn(jsoupAttributes);
        when(jsoupAttributes.iterator()).thenReturn(Arrays.asList(jsoupAttribute).iterator());
        when(jsoupAttribute.getKey()).thenReturn("key");
        when(jsoupAttribute.getValue()).thenReturn("value");
        
        w3cDom.copyAttributes(jsoupNode, w3cElement);
        
        verify(w3cElement).setAttribute("key", "value");
    }
    
    @Test
    public void testUpdateNamespaces() {
        Element jsoupElement = mock(Element.class);
        Attributes jsoupAttributes = mock(Attributes.class);
        Attribute jsoupAttribute = mock(Attribute.class);
        
        when(jsoupElement.attributes()).thenReturn(jsoupAttributes);
        when(jsoupAttributes.iterator()).thenReturn(Arrays.asList(jsoupAttribute).iterator());
        when(jsoupAttribute.getKey()).thenReturn("xmlns");
        when(jsoupAttribute.getValue()).thenReturn("http://www.example.com");
        
        String prefix = w3cDom.updateNamespaces(jsoupElement);
        
        verify(jsoupElement).attributes();
        verify(jsoupAttributes).iterator();
        verify(jsoupAttribute).getKey();
        verify(jsoupAttribute).getValue();
        
        assertEquals("", prefix);
    }
    
    @Test
    public void testUpdateTime() {
        Document w3cDocument = mock(Document.class);
        DomSource domSource = mock(DomSource.class);
        StringWriter writer = mock(StringWriter.class);
        StreamResult result = mock(StreamResult.class);
        TransformerFactory tf = mock(TransformerFactory.class);
        Transformer transformer = mock(Transformer.class);
        String expectedOutput = "output";
        
        when(domSource.setDocument(w3cDocument)).thenReturn(domSource);
        
        try {
            whenNew(DOMSource.class).withArguments(w3cDocument).thenReturn(domSource);
            whenNew(StringWriter.class).withNoArguments().thenReturn(writer);
            whenNew(StreamResult.class).withArguments(writer).thenReturn(result);
            when(TransformerFactory.newInstance()).thenReturn(tf);
            when(tf.newTransformer()).thenReturn(transformer);
            when(writer.toString()).thenReturn(expectedOutput);
        } catch (TransformerConfigurationException e) {
            // do nothing
        }
        
        String output = w3cDom.asString(w3cDocument);
        
        verify(domSource).setDocument(w3cDocument);
        verifyNew(DOMSource.class).withArguments(w3cDocument);
        verifyNew(StringWriter.class).withNoArguments();
        verifyNew(StreamResult.class).withArguments(writer);
        verify(TransformerFactory).newInstance();
        verify(tf).newTransformer();
        
        assertEquals(expectedOutput, output);
    }
}