package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = new Class();
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://example.com/types");
        
        int expectedHashCode = 31 * 1 + serviceClazz.hashCode();
        
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://example.com/types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(new Class(), "http://example.com", "http://example.com/types");
        
        assertEquals(true, descriptor1.equals(descriptor2));
        assertEquals(false, descriptor1.equals(descriptor3));
    }
}