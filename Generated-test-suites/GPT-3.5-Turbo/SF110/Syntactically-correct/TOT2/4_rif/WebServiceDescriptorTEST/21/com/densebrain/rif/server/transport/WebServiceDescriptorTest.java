package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Test typical use case
        Class clazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, "namespace1", "namespace2");
        assertEquals(clazz, descriptor.getServiceClazz());

        // Test edge case
        assertNull(new WebServiceDescriptor(null, "targetNamespace", "typesNamespace").getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        Class clazz = String.class;
        descriptor.setServiceClazz(clazz);
        assertEquals(clazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        // Test typical use case
        String namespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, namespace, "typesNamespace");
        assertEquals(namespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        String namespace = "targetNamespace";
        descriptor.setTargetNamespace(namespace);
        assertEquals(namespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        // Test typical use case
        String namespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", namespace);
        assertEquals(namespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        String namespace = "typesNamespace";
        descriptor.setTypesNamespace(namespace);
        assertEquals(namespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        // Test typical use case
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "namespace3", "namespace4");
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test typical use case
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "namespace1", "namespace2");
        assertEquals(descriptor1, descriptor2);

        // Test error scenario where object is not of WebServiceDescriptor class
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(clazz, "namespace1", "namespace2");
        Integer notDescriptor = 10;
        assertNotEquals(descriptor3, notDescriptor);

        // Test edge case where serviceClazz is null
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(null, "namespace1", "namespace2");
        WebServiceDescriptor descriptor5 = new WebServiceDescriptor(null, "namespace1", "namespace2");
        assertEquals(descriptor4, descriptor5);
    }
}