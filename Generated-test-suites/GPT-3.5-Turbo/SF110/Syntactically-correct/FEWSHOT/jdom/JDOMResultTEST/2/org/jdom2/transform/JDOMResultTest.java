
package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element1"));
        result.add(new Element("element2"));
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult_whenResultListIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetResult_whenResultListIsNotEmpty() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element1"));
        result.add(new Element("element2"));
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult_whenResultListIsEmptyAndResultDocumentIsNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        assertEquals(document.getContent(), jdomResult.getResult());
    }

    @Test
    public void testGetResult_whenResultListIsEmptyAndResultDocumentIsNotNullAndQueryNotPerformed() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        assertEquals(document.getContent(), jdomResult.getResult());
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetResult_whenResultListIsEmptyAndResultDocumentIsNotNullAndQueryPerformed() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        jdomResult.getDocument();
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_whenResultDocumentIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_whenResultDocumentIsNotNullAndQueryNotPerformed() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_whenResultDocumentIsNotNullAndQueryPerformed() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        jdomResult.getResult();
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult_whenResultListIsNullAndResultDocumentIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testRetrieveResult_whenResultListIsNullAndResultDocumentIsNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        assertEquals(document.getContent(), jdomResult.getResult());
    }

    @Test
    public void testSetHandler() {
        JDOMResult jdomResult = new JDOMResult();
        ContentHandler handler = new SAXBuilder().getContentHandler();
        jdomResult.setHandler(handler);
        // Nothing to assert, just checking if the method runs without error
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult jdomResult = new JDOMResult();
        LexicalHandler handler = new SAXBuilder().getContentHandler();
        jdomResult.setLexicalHandler(handler);
        // Nothing to assert, just checking if the method runs without error
    }
}
