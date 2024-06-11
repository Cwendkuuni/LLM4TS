package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.transform.JDOMResult.DocumentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

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
        assertSame(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult_withResultDoc() {
        Document resultDoc = new Document(new Element("root"));
        jdomResult.setDocument(resultDoc);
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void testGetResult_withResultListAndNotQueried() {
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element"));
        jdomResult.setResult(resultList);
        assertSame(resultList, jdomResult.getResult());
    }

    @Test
    public void testGetResult_withResultListAndQueried() {
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element"));
        jdomResult.setResult(resultList);
        jdomResult.getResult();
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void testGetDocument_withResultList() {
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element"));
        jdomResult.setResult(resultList);
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_withResultDocAndNotQueried() {
        Document resultDoc = new Document(new Element("root"));
        jdomResult.setDocument(resultDoc);
        assertSame(resultDoc, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_withResultDocAndQueried() {
        Document resultDoc = new Document(new Element("root"));
        jdomResult.setDocument(resultDoc);
        jdomResult.getDocument();
        assertEquals(null, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertSame(factory, jdomResult.getFactory());
    }

    @Test
    public void testSetHandler() {
        ContentHandler handler = new SAXHandler();
        jdomResult.setHandler(handler);
        // Since the method does not have a functionality the test case should pass if no exception is thrown
    }

    @Test
    public void testSetLexicalHandler() {
        LexicalHandler handler = new SAXHandler();
        jdomResult.setLexicalHandler(handler);
        // Since the method does not have a functionality the test case should pass if no exception is thrown
    }

    @Test
    public void testDocumentBuilder_startDocumentReceived() throws SAXException {
        DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        documentBuilder.startElement("", "", "", null);
        documentBuilder.startPrefixMapping("", "");
        documentBuilder.characters(new char[0], 0, 0);
        documentBuilder.ignorableWhitespace(new char[0], 0, 0);
        documentBuilder.processingInstruction("", "");
        documentBuilder.skippedEntity("");
        documentBuilder.startDTD("", "", "");
        documentBuilder.endDTD();
        documentBuilder.startEntity("");
        documentBuilder.endEntity("");
        documentBuilder.startCDATA();
        documentBuilder.endCDATA();
        documentBuilder.comment(new char[0], 0, 0);

        // Resetting startDocumentReceived so that getResult does not return null
        jdomResult.setDocument(null);

        // invocations of methods related to start document
        documentBuilder.getResult();
        assertNull(documentBuilder.getResult()); // should return null as start document is not received

        documentBuilder.startDocument();

        List<Content> expectedResult = new ArrayList<>();
        assertEquals(expectedResult, documentBuilder.getResult()); // should return an empty list

        // invocations of methods related to start document
        documentBuilder.startElement("", "", "", null);
        documentBuilder.startPrefixMapping("", "");
        documentBuilder.characters(new char[0], 0, 0);
        documentBuilder.ignorableWhitespace(new char[0], 0, 0);
        documentBuilder.processingInstruction("", "");
        documentBuilder.skippedEntity("");
        documentBuilder.startDTD("", "", "");
        documentBuilder.endDTD();
        documentBuilder.startEntity("");
        documentBuilder.endEntity("");
        documentBuilder.startCDATA();
        documentBuilder.endCDATA();
        documentBuilder.comment(new char[0], 0, 0);

        expectedResult.add(new Element("root"));
        assertEquals(expectedResult, documentBuilder.getResult()); // should return a list with root element

        documentBuilder.getResult();
        assertNull(documentBuilder.getResult()); // should return null as start document is already received
    }

    @Test
    public void testFragmentHandler() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.new FragmentHandler(factory);

        // Since the methods in FragmentHandler are not called in this class, the test case should pass if no exception is thrown
    }
}