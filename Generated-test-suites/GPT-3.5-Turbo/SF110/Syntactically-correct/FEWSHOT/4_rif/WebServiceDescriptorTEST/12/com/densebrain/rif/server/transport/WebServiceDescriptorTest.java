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
        
        serviceClazz = String.class;
        targetNamespace = "http://www.example.com";
        typesNamespace = "http://www.example.com/types";
        
        descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        Class newServiceClazz = Integer.class;
        descriptor.setServiceClazz(newServiceClazz);
        
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        String newTargetNamespace = "http://www.example.org";
        descriptor.setTargetNamespace(newTargetNamespace);
        
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        String newTypesNamespace = "http://www.example.org/types";
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor1, descriptor2);
    }
}