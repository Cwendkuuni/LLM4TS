package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.jdom2.*;
import java.util.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<Content>();

        result.setResult(content);
        assertEquals(content, result.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();

        assertNull(result.getResult());

        List<Content> content = new ArrayList<Content>();
        content.add(new Element("test", null, null));

        result.setResult(content);
        assertEquals(content, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();

        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getDocument());

        List<Content> content = new ArrayList<Content>();
        content.add(new Element("test", null, null));

        result.setResult(content);
        assertNotNull(result.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();

        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }
}