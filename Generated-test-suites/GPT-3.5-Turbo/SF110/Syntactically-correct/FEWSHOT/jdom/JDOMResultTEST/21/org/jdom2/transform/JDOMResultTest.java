package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        result.add(element1);
        result.add(element2);
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        result.add(element1);
        result.add(element2);
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
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
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
}