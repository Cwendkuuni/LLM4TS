package org.jdom2.transform;

import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

    @Test
    public void setResultTest() {
        JDOMResult result = new JDOMResult();
        result.setResult(null);
        assertNull(result.getResult());       
    }
    
    @Test
    public void getResultTest() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getResult());       
    }

    @Test
    public void setDocumentTest() {
        JDOMResult result = new JDOMResult();
        result.setDocument(null);
        assertNull(result.getDocument());       
    }
    
    @Test
    public void getDocumentTest() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getDocument());       
    }
    
    @Test
    public void setFactoryTest() {
        JDOMResult result = new JDOMResult();
        result.setFactory(null);
        assertNull(result.getFactory());       
    }
    
    @Test
    public void getFactoryTest() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getFactory());       
    }

}