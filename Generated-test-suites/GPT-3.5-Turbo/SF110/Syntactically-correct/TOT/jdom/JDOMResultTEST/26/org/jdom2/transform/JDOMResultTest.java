import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;

import java.util.Arrays;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertEquals(contents, jdomResult.getResult());
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetResult_EmptyResultList() {
        JDOMResult jdomResult = new JDOMResult();

        assertEquals(Collections.emptyList(), jdomResult.getResult());
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetResult_ResultListNotNull() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertEquals(contents, jdomResult.getResult());
        assertNull(jdomResult.getDocument());

        assertEquals(contents, jdomResult.getResult()); // Second retrieval should return the same list
    }

    @Test
    public void testGetResult_ResultDocumentNotNull() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        jdomResult.setDocument(document);

        assertNull(jdomResult.getResult());
        assertEquals(document, jdomResult.getDocument());

        assertEquals(document, jdomResult.getDocument()); // Second retrieval should return the same document
    }

    @Test
    public void testGetResult_ResultListToDocumentConversion() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertNull(jdomResult.getDocument());

        Document retrievedDocument = jdomResult.getDocument();

        assertNotNull(retrievedDocument);
        assertEquals(contents, retrievedDocument.getContent());
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetResult_ExceptionInDocumentConversion() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        jdomResult.setFactory(null); // Setting factory to null should cause RuntimeException in document conversion

        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        jdomResult.setDocument(document);

        assertNull(jdomResult.getResult());
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_EmptyResultList() {
        JDOMResult jdomResult = new JDOMResult();

        assertNull(jdomResult.getDocument());
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void testGetDocument_ResultListNotNull() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertNull(jdomResult.getDocument());
        assertEquals(contents, jdomResult.getResult());

        assertNull(jdomResult.getDocument()); // Second retrieval should still return null
    }

    @Test
    public void testGetDocument_ResultDocumentNotNull() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        jdomResult.setDocument(document);

        assertEquals(document, jdomResult.getDocument());
        assertNull(jdomResult.getResult());

        assertEquals(document, jdomResult.getDocument()); // Second retrieval should return the same document
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();

        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);

        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();

        assertNull(jdomResult.getFactory());

        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);

        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult_ResultListNotNull() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertNull(jdomResult.getResult());
        assertNull(jdomResult.getDocument());

        jdomResult.retrieveResult();

        assertEquals(contents, jdomResult.getResult());
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testRetrieveResult_ResultListToDocumentConversion() {
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertNull(jdomResult.getDocument());

        jdomResult.retrieveResult();

        assertNotNull(jdomResult.getDocument());
        assertEquals(contents, jdomResult.getDocument().getContent());
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testSetHandler() {
        JDOMResult jdomResult = new JDOMResult();

        ContentHandler handler = new DefaultHandler();
        jdomResult.setHandler(handler);

        // No assertion, method does not modify class state
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult jdomResult = new JDOMResult();

        LexicalHandler handler = new DefaultHandler();
        jdomResult.setLexicalHandler(handler);

        // No assertion, method does not modify class state
    }

    @Test
    public void testFragmentHandler_GetResult() {
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));

        assertEquals(contents, fragmentHandler.getDetachedContent(rootElement));

        rootElement.addContent(contents);
        assertEquals(contents, fragmentHandler.getDetachedContent(rootElement));
    }

    @Test
    public void testDocumentBuilder_GetResult() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        assertNull(documentBuilder.getResult());

        documentBuilder.startDocument();

        assertEquals(contents, documentBuilder.getResult());
        assertNull(documentBuilder.getResult());

        documentBuilder.startDocument();

        assertNull(documentBuilder.getResult());
    }

    @Test
    public void testDocumentBuilder_ExceptionInInitialization() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();

        try {
            documentBuilder.characters("test".toCharArray(), 0, "test".length());
            fail("characters() did not throw an exception");
        } catch (SAXException e) {
            assertTrue(e instanceof SAXParseException);
        }
    }

    @Test
    public void testDocumentBuilder_ExceptionInDocumentConversion() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        JDOMResult jdomResult = new JDOMResult();

        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        List<Content> contents = Arrays.asList(new Comment("This is a comment"), new Element("child"));
        jdomResult.setResult(contents);

        jdomResult.setFactory(null);

        assertNull(documentBuilder.getResult());
    }

    @Test
    public void testDocumentBuilder_LexicalHandlerMethods() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();

        try {
            documentBuilder.startDTD("test", "public", "system");
            documentBuilder.endDTD();
            documentBuilder.startEntity("test");
            documentBuilder.endEntity("test");
            documentBuilder.startCDATA();
            documentBuilder.endCDATA();
            documentBuilder.comment("test".toCharArray(), 0, "test".length());
        } catch (SAXException e) {
            fail("Exception occurred in a LexicalHandler method");
        }
    }

}