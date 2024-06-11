package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = java.lang.String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        Class serviceClazz = java.lang.String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        Class newServiceClazz = java.util.Date.class;
        String newTargetNamespace = "http://www.example.com/newtarget";
        String newTypesNamespace = "http://www.example.com/newtypes";
        
        descriptor.setServiceClazz(newServiceClazz);
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = java.lang.String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        int expectedHashCode = 31 * 1 + ((serviceClazz == null) ? 0 : serviceClazz.hashCode());
        
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = java.lang.String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(java.util.Date.class, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz, "http://www.example.com/newtarget", typesNamespace);
        WebServiceDescriptor descriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, "http://www.example.com/newtypes");
        
        assertEquals(descriptor1, descriptor1);
        assertEquals(descriptor1, descriptor2);
        assertNotNull(descriptor1);
        assertNotNull(descriptor2);
        assertNotNull(descriptor3);
        assertNotNull(descriptor4);
        assertNotNull(descriptor5);
        
        assertEquals(false, descriptor1.equals(null));
        assertEquals(false, descriptor1.equals(new Object()));
        
        assertEquals(false, descriptor1.equals(descriptor3));
        assertEquals(false, descriptor1.equals(descriptor4));
        assertEquals(false, descriptor1.equals(descriptor5));
    }
}