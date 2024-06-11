package org.jdom2.transform;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<>();

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(result);

        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(result);

        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        Document document = new Document();

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(document);

        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(result);

        assertNotNull(jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(factory);

        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();

        assertNull(jdomResult.getFactory());
    }
}