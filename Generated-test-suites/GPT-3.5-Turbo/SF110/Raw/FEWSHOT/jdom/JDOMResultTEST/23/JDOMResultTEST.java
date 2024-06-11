
package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        result.setResult(contentList);
        assertEquals(contentList, result.getResult());
    }

    @Test
    public void testGetResultWhenResultListIsNull() {
        JDOMResult result = new JDOMResult();
        result.setDocument(new Document());
        assertEquals(result.getDocument().getContent(), result.getResult());
    }

    @Test
    public void testGetResultWhenResultListIsNotNull() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        result.setResult(contentList);
        assertEquals(contentList, result.getResult());
    }

    @Test
    public void testGetResultWhenResultDocIsNull() {
        JDOMResult result = new JDOMResult();
        result.setResult(new ArrayList<>());
        assertEquals(result.getResult(), result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void testGetDocumentWhenResultListIsNull() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void testGetDocumentWhenResultDocIsNull() {
        JDOMResult result = new JDOMResult();
        result.setResult(new ArrayList<>());
        assertEquals(result.getResult(), result.getDocument().getContent());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        result.setHandler(new MockContentHandler());
        assertTrue(true); // Test that no exception is thrown
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        result.setLexicalHandler(new MockLexicalHandler());
        assertTrue(true); // Test that no exception is thrown
    }

    // Auxiliary classes for testing

    private static class MockContentHandler implements org.xml.sax.ContentHandler {

        @Override
        public void setDocumentLocator(org.xml.sax.Locator locator) {}

        @Override
        public void startDocument() throws SAXException {}

        @Override
        public void endDocument() throws SAXException {}

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {}

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {}

        @Override
        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes atts) throws SAXException {}

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void processingInstruction(String target, String data) throws SAXException {}

        @Override
        public void skippedEntity(String name) throws SAXException {}
    }

    private static class MockLexicalHandler implements org.xml.sax.ext.LexicalHandler {

        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException {}

        @Override
        public void endDTD() throws SAXException {}

        @Override
        public void startEntity(String name) throws SAXException {}

        @Override
        public void endEntity(String name) throws SAXException {}

        @Override
        public void startCDATA() throws SAXException {}

        @Override
        public void endCDATA() throws SAXException {}

        @Override
        public void comment(char[] ch, int start, int length) throws SAXException {}
    }
}

