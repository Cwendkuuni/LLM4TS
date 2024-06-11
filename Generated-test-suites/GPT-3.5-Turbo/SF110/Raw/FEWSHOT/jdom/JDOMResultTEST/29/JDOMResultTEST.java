package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element1"));
        contentList.add(new Element("element2"));
        contentList.add(new Element("element3"));
        
        result.setResult(contentList);
        
        assertEquals(contentList, result.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element1"));
        contentList.add(new Element("element2"));
        contentList.add(new Element("element3"));
        
        result.setResult(contentList);
        
        assertEquals(contentList, result.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document(new Element("root"));
        
        result.setDocument(document);
        
        assertEquals(document, result.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document(new Element("root"));
        
        result.setDocument(document);
        
        assertEquals(document, result.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        
        result.setFactory(factory);
        
        assertEquals(factory, result.getFactory());
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        
        result.setFactory(factory);
        
        assertEquals(factory, result.getFactory());
    }
    
    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        ContentHandler handler = new DefaultHandler();
        
        result.setHandler(handler);
        
        // No assertion. Just testing that the method does not throw an exception.
    }
    
    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        LexicalHandler handler = new DefaultHandler();
        
        result.setLexicalHandler(handler);
        
        // No assertion. Just testing that the method does not throw an exception.
    }
}