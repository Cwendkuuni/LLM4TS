package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
        assertNull(jdomResult.getResult());
        
        // Add additional test cases here
        
    }
    
    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
        
        // Add additional test cases here
        
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
        assertNull(jdomResult.getDocument());
        
        // Add additional test cases here
        
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getDocument());
        
        // Add additional test cases here
        
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        assertNull(jdomResult.getFactory());
        
        // Add additional test cases here
        
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getFactory());
        
        // Add additional test cases here
        
    }
    
}