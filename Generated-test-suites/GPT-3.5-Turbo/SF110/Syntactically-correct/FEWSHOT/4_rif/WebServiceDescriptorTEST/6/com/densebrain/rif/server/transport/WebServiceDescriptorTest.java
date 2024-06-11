package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = new Class();
        String targetNamespace = "namespace1";
        String typesNamespace = "namespace2";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetters() {
        Class serviceClazz = new Class();
        String targetNamespace = "namespace1";
        String typesNamespace = "namespace2";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        descriptor.setServiceClazz(serviceClazz);
        descriptor.setTargetNamespace(targetNamespace);
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = new Class();
        Class serviceClazz2 = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, null, null);
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = new Class();
        Class serviceClazz2 = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, null, null);
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
}