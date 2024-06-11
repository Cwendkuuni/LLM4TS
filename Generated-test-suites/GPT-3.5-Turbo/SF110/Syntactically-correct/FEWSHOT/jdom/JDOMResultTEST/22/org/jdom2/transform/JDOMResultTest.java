package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {
    private JDOMResult jdomResult;
    
    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }
    
    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element", null, null));
        
        jdomResult.setResult(result);
        
        assertEquals(result, jdomResult.getResult());
        assertNull(jdomResult.getDocument());
    }
    
    @Test
    public void testGetResultWithResultList() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element", null, null));
        jdomResult.setResult(result);
        
        List<Content> returnedResult = jdomResult.getResult();
        
        assertEquals(result, returnedResult);
        assertTrue(jdomResult.queried);
    }
    
    @Test
    public void testGetResultWithResultDocument() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element", null, null));
        jdomResult.setDocument(new Document(new Element("root")));
        
        List<Content> returnedResult = jdomResult.getResult();
        
        assertEquals(result, returnedResult);
        assertTrue(jdomResult.queried);
    }
    
    @Test
    public void testSetDocument() {
        Document document = new Document(new Element("root"));
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
        assertNull(jdomResult.getResult());
    }
    
    @Test
    public void testGetDocumentWithResultDocument() {
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        
        Document returnedDocument = jdomResult.getDocument();
        
        assertEquals(document, returnedDocument);
        assertTrue(jdomResult.queried);
    }
    
    @Test
    public void testGetDocumentWithResultList() {
        Document expectedDocument = new Document(new Element("root"));
        List<Content> result = new ArrayList<>();
        result.add(new Element("element", null, null));
        jdomResult.setResult(result);
        
        Document returnedDocument = jdomResult.getDocument();
        
        assertEquals(expectedDocument, returnedDocument);
        assertTrue(jdomResult.queried);
    }
    
    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
    
}