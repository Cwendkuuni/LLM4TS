package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = new Class();
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        
        assertEquals("targetNamespace", descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        
        descriptor.setTargetNamespace("targetNamespace");
        
        assertEquals("targetNamespace", descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        
        assertEquals("typesNamespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        
        descriptor.setTypesNamespace("typesNamespace");
        
        assertEquals("typesNamespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = new Class();
        Class serviceClazz2 = new Class();
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, "targetNamespace", "typesNamespace");
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
}