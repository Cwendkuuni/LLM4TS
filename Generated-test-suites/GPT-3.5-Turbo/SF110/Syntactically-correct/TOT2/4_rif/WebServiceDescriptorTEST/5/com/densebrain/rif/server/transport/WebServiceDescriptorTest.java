package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    // Test for constructor
    @Test
    public void testConstructor() {
        Class serviceClazz = TestService.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://types.example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    // Test for setting and getting ServiceClazz
    @Test
    public void testSetGetServiceClazz() {
        Class serviceClazz = TestService.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    // Test for setting and getting TargetNamespace
    @Test
    public void testSetGetTargetNamespace() {
        String targetNamespace = "http://example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    // Test for setting and getting TypesNamespace
    @Test
    public void testSetGetTypesNamespace() {
        String typesNamespace = "http://types.example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    // Test for hashCode
    @Test
    public void testHashCode() {
        Class serviceClazz = TestService.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);
        
        assertEquals(31 * (serviceClazz.hashCode()), descriptor.hashCode());
    }

    // Test for equals
    @Test
    public void testEquals() {
        Class serviceClazz = TestService.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, null, null);
        
        assertTrue(descriptor1.equals(descriptor2));
    }
    
    // Test for not equals
    @Test
    public void testNotEquals() {
        Class serviceClazz = TestService.class;
        Class otherServiceClazz = OtherTestService.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(otherServiceClazz, null, null);
        
        assertFalse(descriptor1.equals(descriptor2));
    }

    // Test for null check in equals
    @Test
    public void testEqualsWithNull() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, null, null);
        
        assertTrue(descriptor1.equals(descriptor2));
    }

    // Test for different class in equals
    @Test
    public void testEqualsDifferentClass() {
        Class serviceClazz = TestService.class;

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);
        
        assertFalse(descriptor.equals(new Object()));
    }

    // Test for hashCode with null serviceClazz
    @Test
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        assertEquals(1, descriptor.hashCode());
    }

    class TestService {}
    class OtherTestService {}
}