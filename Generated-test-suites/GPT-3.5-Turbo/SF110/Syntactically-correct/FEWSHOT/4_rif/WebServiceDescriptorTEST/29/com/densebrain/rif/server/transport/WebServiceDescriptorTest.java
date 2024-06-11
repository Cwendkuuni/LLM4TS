package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        // assert that getServiceClazz returns the correct value
        assertEquals(null, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        // set the serviceClazz to a new value
        descriptor.setServiceClazz(String.class);

        // assert that getServiceClazz returns the new value
        assertEquals(String.class, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "namespace", null);

        // assert that getTargetNamespace returns the correct value
        assertEquals("namespace", descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        // set the targetNamespace to a new value
        descriptor.setTargetNamespace("newNamespace");

        // assert that getTargetNamespace returns the new value
        assertEquals("newNamespace", descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "typesNamespace");

        // assert that getTypesNamespace returns the correct value
        assertEquals("typesNamespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        // set the typesNamespace to a new value
        descriptor.setTypesNamespace("newTypesNamespace");

        // assert that getTypesNamespace returns the new value
        assertEquals("newTypesNamespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        // assert that hashCode returns the expected value
        assertEquals(31, descriptor.hashCode());
    }

    @Test
    public void testEquals() {
        // create instance of WebServiceDescriptor
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, null, null);

        // assert that descriptor1 equals descriptor2
        assertEquals(true, descriptor1.equals(descriptor2));
    }
}