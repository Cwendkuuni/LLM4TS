package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        
        descriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, "typesNamespace");
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}