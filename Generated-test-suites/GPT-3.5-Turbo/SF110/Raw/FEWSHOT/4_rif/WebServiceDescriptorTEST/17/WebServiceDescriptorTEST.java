package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        descriptor.setServiceClazz(serviceClazz);
        descriptor.setTargetNamespace(targetNamespace);
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);
        
        int expectedHashCode = serviceClazz.hashCode() * 31;
        
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz1 = String.class;
        Class serviceClazz2 = Integer.class;
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, null, null);
        
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, null, null);
        
        assertEquals(true, descriptor1.equals(descriptor1));
        assertEquals(true, descriptor1.equals(descriptor2));
        assertEquals(false, descriptor1.equals(descriptor3));
        assertEquals(false, descriptor1.equals(null));
        assertEquals(false, descriptor1.equals("Not a descriptor object"));
    }
}