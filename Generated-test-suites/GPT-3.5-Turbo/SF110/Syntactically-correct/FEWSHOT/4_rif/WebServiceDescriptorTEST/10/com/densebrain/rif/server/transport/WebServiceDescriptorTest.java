package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://target";
        String typesNamespace = "http://types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://target";
        String typesNamespace = "http://types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        Class newServiceClazz = Integer.class;
        String newTargetNamespace = "http://newtarget";
        String newTypesNamespace = "http://newtypes";
        
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
        String targetNamespace = "http://target";
        String typesNamespace = "http://types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        int expectedHashCode = 31 * (31 + serviceClazz.hashCode());
        
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://target";
        String typesNamespace = "http://types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(Integer.class, targetNamespace, typesNamespace);
        
        assertEquals(descriptor1, descriptor2);
        assertEquals(descriptor2, descriptor1);
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        
        assertNotEquals(descriptor1, descriptor3);
        assertNotEquals(descriptor3, descriptor1);
    }
}