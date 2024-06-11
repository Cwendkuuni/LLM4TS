package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element = new Element("test");
        result.add(element);
        
        jdomResult.setResult(result);
        
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element = new Element("test");
        result.add(element);
        
        jdomResult.setResult(result);
        
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("test"));
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("test"));
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
}