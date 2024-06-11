package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = new Class();
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
    
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        Class serviceClazz = new Class();
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
    
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    
        Class newServiceClazz = new Class();
        String newTargetNamespace = "newTargetNamespace";
        String newTypesNamespace = "newTypesNamespace";
    
        descriptor.setServiceClazz(newServiceClazz);
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);
    
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
    
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    
        int expectedHashCode = 31 * 1 + ((serviceClazz == null) ? 0 : serviceClazz.hashCode());
    
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = new Class();
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
    
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(new Class(), targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz, "differentTargetNamespace", typesNamespace);
        WebServiceDescriptor descriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, "differentTypesNamespace");
    
        assertTrue(descriptor1.equals(descriptor2));
        assertTrue(!descriptor1.equals(descriptor3));
        assertTrue(!descriptor1.equals(descriptor4));
        assertTrue(!descriptor1.equals(descriptor5));
    }
}