
package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<Content>();
        jdomResult.setResult(contentList);
        
        assertEquals(contentList, jdomResult.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        
        assertNull(jdomResult.getResult());
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
        
        assertNull(jdomResult.getDocument());
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
        
        assertNull(jdomResult.getFactory());
    }
}
