package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

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
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, targetNamespace, "typesNamespace");
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, null, "typesNamespace");
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", null);
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "targetNamespace2", "typesNamespace2");
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        assertEquals(descriptor1, descriptor2);
    }

}