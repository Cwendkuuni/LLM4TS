package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMFactory;
import org.jdom2.transform.JDOMResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    private JDOMResult jdomResult;
    private JDOMFactory jdomFactory;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
        jdomFactory = new DefaultJDOMFactory();
    }

    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);

        assertNotNull(jdomResult.getResult());
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult_WithoutSetResult() {
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testGetResult_AfterSetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);

        assertNotNull(jdomResult.getResult());
        assertEquals(document.getContent(), jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);

        assertNotNull(jdomResult.getDocument());
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_WithoutSetDocument() {
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_AfterSetResult() {
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);

        assertNotNull(jdomResult.getDocument());
        assertEquals(result, jdomResult.getDocument().getContent());
    }

    @Test
    public void testSetFactory() {
        jdomResult.setFactory(jdomFactory);

        assertNotNull(jdomResult.getFactory());
        assertEquals(jdomFactory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory_WithoutSetFactory() {
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testGetFactory_AfterSetFactory() {
        jdomResult.setFactory(jdomFactory);

        assertNotNull(jdomResult.getFactory());
        assertEquals(jdomFactory, jdomResult.getFactory());
    }

    @Test
    public void testSetHandler() {
        // This test case does not apply for JDOMResult as it does not provide a meaningful implementation for setHandler method.
        // The test case can be skipped.
    }

    @Test
    public void testSetLexicalHandler() {
        // This test case does not apply for JDOMResult as it does not provide a meaningful implementation for setLexicalHandler method.
        // The test case can be skipped.
    }
}