package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructor() {
        Class serviceClazz = null;
        String targetNamespace = null;
        String typesNamespace = null;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertNull(descriptor.getServiceClazz());
        assertNull(descriptor.getTargetNamespace());
        assertNull(descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSettersAndGetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
        
        Class newServiceClazz = Integer.class;
        String newTargetNamespace = "http://example.org";
        String newTypesNamespace = "http://example.org/types";
        
        descriptor.setServiceClazz(newServiceClazz);
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor, descriptor2);
    }

}