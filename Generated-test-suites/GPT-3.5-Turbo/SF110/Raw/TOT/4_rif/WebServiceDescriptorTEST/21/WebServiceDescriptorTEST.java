
package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructor() {
        Class serviceClazz = TestService.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        // Check if the values are set correctly
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        // Change the serviceClazz
        Class updatedServiceClazz = AnotherTestService.class;
        descriptor.setServiceClazz(updatedServiceClazz);
        
        // Check if the serviceClazz is updated correctly
        assertEquals(updatedServiceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        // Change the targetNamespace
        String updatedTargetNamespace = "http://www.example.org";
        descriptor.setTargetNamespace(updatedTargetNamespace);
        
        // Check if the targetNamespace is updated correctly
        assertEquals(updatedTargetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        // Change the typesNamespace
        String updatedTypesNamespace = "http://www.example.org/types";
        descriptor.setTypesNamespace(updatedTypesNamespace);
        
        // Check if the typesNamespace is updated correctly
        assertEquals(updatedTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        // Check if the hashCode of two objects with same properties are equal
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        
        // Check if the equals method returns true for two objects with same properties
        assertTrue(descriptor1.equals(descriptor2));
    }
    
    @Test
    public void testNotEquals() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(AnotherTestService.class, "http://www.example.com", "http://www.example.com/types");
        
        // Check if the equals method returns false for two objects with different properties
        assertFalse(descriptor1.equals(descriptor2));
    }
    
    private static class TestService {
    }
    
    private static class AnotherTestService {
    }
}

