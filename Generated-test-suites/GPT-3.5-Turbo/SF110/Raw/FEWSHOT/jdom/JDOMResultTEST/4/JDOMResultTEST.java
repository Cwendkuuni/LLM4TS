package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {
    private JDOMResult jdomResult;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
        assertTrue(jdomResult.getResult().isEmpty());
    }

    @Test
    public void testGetResultWithoutResultList() {
        jdomResult.setResult(null);
        assertNull(jdomResult.getResult());

        Document resultDoc = new Document();
        jdomResult.setDocument(resultDoc);
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetResultWithResultList() {
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResultWithResultDocument() {
        Document resultDoc = new Document();
        jdomResult.setDocument(resultDoc);
        assertEquals(resultDoc.getContent(), jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetDocumentWithoutResultDocument() {
        jdomResult.setDocument(null);
        assertNull(jdomResult.getDocument());

        List<Content> resultList = new ArrayList<>();
        jdomResult.setResult(resultList);
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocumentWithResultDocument() {
        Document resultDoc = new Document();
        jdomResult.setDocument(resultDoc);
        assertEquals(resultDoc, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        assertNull(jdomResult.getFactory());

        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testSetHandler() {
        // No assertion as the method is empty
        jdomResult.setHandler(null);
    }

    @Test
    public void testSetLexicalHandler() {
        // No assertion as the method is empty
        jdomResult.setLexicalHandler(null);
    }

    @Test
    public void testFragmentHandlerGetResult() {
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        assertEquals(Collections.emptyList(), fragmentHandler.getResult());
    }

    @Test
    public void testDocumentBuilderGetResult() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        assertNull(documentBuilder.getResult());
    }

    @Test
    public void testDocumentBuilderEnsureInitialization() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.ensureInitialization();
        assertTrue(documentBuilder.startDocumentReceived);
    }

    @Test
    public void testDocumentBuilderStartDocument() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.startDocument();
        assertTrue(documentBuilder.startDocumentReceived);
        assertNull(documentBuilder.saxHandler);
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testDocumentBuilderStartElement() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String nsUri = "http://example.com";
        String localName = "element";
        String qName = "prefix:element";
        Attributes atts = null;
        documentBuilder.startElement(nsUri, localName, qName, atts);
    }

    @Test
    public void testDocumentBuilderStartPrefixMapping() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String prefix = "prefix";
        String uri = "http://example.com";
        documentBuilder.startPrefixMapping(prefix, uri);
    }

    @Test
    public void testDocumentBuilderCharacters() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        char[] ch = "test".toCharArray();
        int start = 0;
        int length = ch.length;
        documentBuilder.characters(ch, start, length);
    }

    @Test
    public void testDocumentBuilderIgnorableWhitespace() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        char[] ch = "   ".toCharArray();
        int start = 0;
        int length = ch.length;
        documentBuilder.ignorableWhitespace(ch, start, length);
    }

    @Test
    public void testDocumentBuilderProcessingInstruction() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String target = "target";
        String data = "data";
        documentBuilder.processingInstruction(target, data);
    }

    @Test
    public void testDocumentBuilderSkippedEntity() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String name = "entity";
        documentBuilder.skippedEntity(name);
    }

    @Test
    public void testDocumentBuilderStartDTD() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String name = "dtd";
        String publicId = "public";
        String systemId = "system";
        documentBuilder.startDTD(name, publicId, systemId);
    }

    @Test
    public void testDocumentBuilderEndDTD() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        documentBuilder.endDTD();
    }

    @Test
    public void testDocumentBuilderStartEntity() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String name = "entity";
        documentBuilder.startEntity(name);
    }

    @Test
    public void testDocumentBuilderEndEntity() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        String name = "entity";
        documentBuilder.endEntity(name);
    }

    @Test
    public void testDocumentBuilderStartCDATA() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        documentBuilder.startCDATA();
    }

    @Test
    public void testDocumentBuilderEndCDATA() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        documentBuilder.endCDATA();
    }

    @Test
    public void testDocumentBuilderComment() throws SAXException {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        documentBuilder.saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        char[] ch = "comment".toCharArray();
        int start = 0;
        int length = ch.length;
        documentBuilder.comment(ch, start, length);
    }
}