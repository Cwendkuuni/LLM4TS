package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    private WebServiceDescriptor webServiceDescriptor;
    private Class serviceClazz;
    private String targetNamespace;
    private String typesNamespace;
    
    @Before
    public void setUp() {
        serviceClazz = null; // set the value of serviceClazz
        targetNamespace = null; // set the value of targetNamespace
        typesNamespace = null; // set the value of typesNamespace
        
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }
    
    @Test
    public void testGetServiceClazz() {
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class newServiceClazz = null; // set new value for serviceClazz
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String newTargetNamespace = null; // set new value for targetNamespace
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = null; // set new value for typesNamespace
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        int expectedResult = (31 * 1) + ((serviceClazz == null) ? 0 : serviceClazz.hashCode());
        assertEquals(expectedResult, webServiceDescriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test 1: comparing an instance with itself
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        
        // Test 2: comparing with null
        assertFalse(webServiceDescriptor.equals(null));
        
        // Test 3: comparing with an instance of a different class
        assertFalse(webServiceDescriptor.equals(new Object()));
        
        // Test 4: comparing with an instance with different serviceClazz value
        WebServiceDescriptor differentServiceClazzDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);
        assertFalse(webServiceDescriptor.equals(differentServiceClazzDescriptor));
        
        // Test 5: comparing with an instance with same serviceClazz value
        assertEquals(webServiceDescriptor, new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace));
    }
}