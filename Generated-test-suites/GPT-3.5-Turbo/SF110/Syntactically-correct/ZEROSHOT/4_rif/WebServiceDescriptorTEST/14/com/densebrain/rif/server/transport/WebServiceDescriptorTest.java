package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class expectedServiceClazz = null;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedServiceClazz, targetNamespace, typesNamespace);
        
        Class actual = descriptor.getServiceClazz();
        
        assertEquals(expectedServiceClazz, actual);
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expectedServiceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);
        
        descriptor.setServiceClazz(expectedServiceClazz);
        
        Class actual = descriptor.getServiceClazz();
        
        assertEquals(expectedServiceClazz, actual);
    }
    
    @Test
    public void testGetTargetNamespace() {
        Class serviceClazz = String.class;
        String expectedTargetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, expectedTargetNamespace, typesNamespace);
        
        String actual = descriptor.getTargetNamespace();
        
        assertEquals(expectedTargetNamespace, actual);
    }
    
    @Test
    public void testSetTargetNamespace() {
        Class serviceClazz = String.class;
        String expectedTargetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        
        descriptor.setTargetNamespace(expectedTargetNamespace);
        
        String actual = descriptor.getTargetNamespace();
        
        assertEquals(expectedTargetNamespace, actual);
    }
    
    @Test
    public void testGetTypesNamespace() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String expectedTypesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, expectedTypesNamespace);
        
        String actual = descriptor.getTypesNamespace();
        
        assertEquals(expectedTypesNamespace, actual);
    }
    
    @Test
    public void testSetTypesNamespace() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String expectedTypesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);
        
        descriptor.setTypesNamespace(expectedTypesNamespace);
        
        String actual = descriptor.getTypesNamespace();
        
        assertEquals(expectedTypesNamespace, actual);
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        boolean result = descriptor1.equals(descriptor2);
        
        assertTrue(result);
    }
}