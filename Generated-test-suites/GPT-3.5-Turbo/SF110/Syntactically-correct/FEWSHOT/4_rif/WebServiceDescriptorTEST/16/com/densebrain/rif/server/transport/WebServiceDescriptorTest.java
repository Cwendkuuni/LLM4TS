package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructor() {
        Class serviceClazz = new Class();
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        Class serviceClazz = new Class();
        
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        String targetNamespace = "http://www.example.com";
        
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        String typesNamespace = "http://www.example.com/types";
        
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        int hashCode = descriptor.hashCode();
        
        // TODO: Assert the expected hash code based on the values of serviceClazz, targetNamespace, and typesNamespace
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz1 = new Class();
        String targetNamespace1 = "http://www.example.com";
        String typesNamespace1 = "http://www.example.com/types";
        
        Class serviceClazz2 = new Class();
        String targetNamespace2 = "http://www.anotherexample.com";
        String typesNamespace2 = "http://www.anotherexample.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
}