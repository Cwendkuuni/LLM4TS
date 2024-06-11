package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructor() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://www.test.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://www.test.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}