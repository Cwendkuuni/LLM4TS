package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://types.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = String.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://types.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    // Edge case testing for hashCode() and equals() methods can be added here

}