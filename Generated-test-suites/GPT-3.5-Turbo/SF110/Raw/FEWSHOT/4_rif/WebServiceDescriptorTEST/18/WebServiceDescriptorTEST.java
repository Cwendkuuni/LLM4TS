package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webService;

    @Before
    public void setUp() {
        webService = new WebServiceDescriptor(null, null, null);
    }
    
    @Test
    public void testGetServiceClazz() {
        assertNull(webService.getServiceClazz());
        
        webService.setServiceClazz(String.class);
        assertEquals(String.class, webService.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        webService.setServiceClazz(null);
        assertNull(webService.getServiceClazz());
        
        webService.setServiceClazz(String.class);
        assertEquals(String.class, webService.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        assertNull(webService.getTargetNamespace());
        
        webService.setTargetNamespace("http://www.example.com");
        assertEquals("http://www.example.com", webService.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        webService.setTargetNamespace(null);
        assertNull(webService.getTargetNamespace());
        
        webService.setTargetNamespace("http://www.example.com");
        assertEquals("http://www.example.com", webService.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        assertNull(webService.getTypesNamespace());
        
        webService.setTypesNamespace("http://www.example.com/types");
        assertEquals("http://www.example.com/types", webService.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        webService.setTypesNamespace(null);
        assertNull(webService.getTypesNamespace());
        
        webService.setTypesNamespace("http://www.example.com/types");
        assertEquals("http://www.example.com/types", webService.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 31;
        assertEquals(expectedHashCode, webService.hashCode());
        
        webService.setServiceClazz(String.class);
        expectedHashCode = 31 * expectedHashCode + String.class.hashCode();
        assertEquals(expectedHashCode, webService.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor otherWebService = new WebServiceDescriptor(null, null, null);
        assertTrue(webService.equals(webService));
        assertTrue(webService.equals(otherWebService));
        
        webService.setServiceClazz(String.class);
        assertFalse(webService.equals(null));
        
        otherWebService.setServiceClazz(Integer.class);
        assertFalse(webService.equals(otherWebService));
        
        otherWebService.setServiceClazz(String.class);
        assertTrue(webService.equals(otherWebService));
    }
}