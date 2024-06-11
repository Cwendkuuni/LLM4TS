package org.jdom2.transform;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;

import java.util.*;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        
        ArrayList<Content> contentList = new ArrayList<Content>();
        Element element1 = new Element("element1");
        contentList.add(element1);
        Element element2 = new Element("element2");
        contentList.add(element2);
        
        jdomResult.setResult(contentList);
        
        assertEquals(contentList, jdomResult.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        
        ArrayList<Content> contentList = new ArrayList<Content>();
        Element element1 = new Element("element1");
        contentList.add(element1);
        Element element2 = new Element("element2");
        contentList.add(element2);
        
        jdomResult.setResult(contentList);
        
        assertEquals(contentList, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        
        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        
        Element rootElement = new Element("root");
        Document document = new Document(rootElement);
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        
        JDOMFactory factory = new JDOMFactory() {
            // Implementation of JDOMFactory methods
        };
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        
        JDOMFactory factory = new JDOMFactory() {
            // Implementation of JDOMFactory methods
        };
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
}