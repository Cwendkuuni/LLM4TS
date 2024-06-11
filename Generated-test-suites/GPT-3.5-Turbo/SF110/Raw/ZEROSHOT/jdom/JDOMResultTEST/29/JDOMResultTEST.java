package org.jdom2.transform;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.*;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));
        result.setResult(contentList);
        
        List<Content> resultContentList = result.getResult();
        assertEquals(contentList, resultContentList);
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));
        result.setResult(contentList);
        
        List<Content> resultContentList = result.getResult();
        assertEquals(contentList, resultContentList);
        
        List<Content> secondResultContentList = result.getResult();
        assertEquals(contentList, secondResultContentList);
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        
        Document resultDocument = result.getDocument();
        assertEquals(document, resultDocument);
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        
        Document resultDocument = result.getDocument();
        assertEquals(document, resultDocument);
        
        Document secondResultDocument = result.getDocument();
        assertEquals(document, secondResultDocument);
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        
        JDOMFactory resultFactory = result.getFactory();
        assertEquals(factory, resultFactory);
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        
        JDOMFactory resultFactory = result.getFactory();
        assertEquals(factory, resultFactory);
        
        JDOMFactory secondResultFactory = result.getFactory();
        assertEquals(factory, secondResultFactory);
    }
}