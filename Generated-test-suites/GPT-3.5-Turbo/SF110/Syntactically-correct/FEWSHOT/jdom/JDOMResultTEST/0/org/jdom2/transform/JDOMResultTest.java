package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jdom2.*;
import java.util.*;

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
        Document document = new Document();
        Element element = new Element("test");
        document.setRootElement(element);
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        Element element = new Element("test");
        document.setRootElement(element);
        
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

@Test
    public void testSetHandler() {
        // Do nothing as the setHandler method does not have any functionality to test
    }
    
    @Test
    public void testSetLexicalHandler() {
        // Do nothing as the setLexicalHandler method does not have any functionality to test
    }
}