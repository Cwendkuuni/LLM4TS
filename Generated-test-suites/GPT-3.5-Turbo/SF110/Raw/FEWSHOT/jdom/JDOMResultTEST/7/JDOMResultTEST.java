package org.jdom2.transform;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.input.*;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<Content>();
        content.add(new Element("element"));
        result.setResult(content);
        
        assertEquals(content, result.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<Content>();
        content.add(new Element("element"));
        result.setResult(content);
        
        assertEquals(content, result.getResult());
        
        result.setResult(null);
        
        assertNull(result.getResult());
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
        
        result.setDocument(null);
        
        assertNull(result.getDocument());
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
    public void testRetrieveResult() throws JDOMException {
        JDOMResult result = new JDOMResult();
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new StringReader("<root><element/></root>"));
        result.setDocument(document);
        
        assertEquals(document.getRootElement().getContent(), result.getResult());
        
        result.setDocument(null);
        
        assertNull(result.getResult());
    }
}