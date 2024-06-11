package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
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
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(String.class, "http://www.example.com/different", typesNamespace);
        
        assertEquals(descriptor1, descriptor2);
        assertEquals(descriptor2, descriptor1);
        assertEquals(descriptor1, descriptor1);
        assertEquals(descriptor2, descriptor2);
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        
        assertNull(descriptor1.equals(null));
        assertNull(descriptor1.equals("not a descriptor"));
        
        assertEquals(descriptor1.equals(descriptor3), false);
        assertEquals(descriptor3.equals(descriptor1), false);
    }
}