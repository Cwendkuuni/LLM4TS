package com.densebrain.rif.server.transport;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = new Class();
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();

        WebServiceDescriptor descriptor = new WebServiceDescriptor();

        assertNull(descriptor.getServiceClazz());

        descriptor.setServiceClazz(serviceClazz);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com/target";

        WebServiceDescriptor descriptor = new WebServiceDescriptor();

        assertNull(descriptor.getTargetNamespace());

        descriptor.setTargetNamespace(targetNamespace);

        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor();

        assertNull(descriptor.getTypesNamespace());

        descriptor.setTypesNamespace(typesNamespace);

        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://example.com/target", "http://example.com/types");

        int expectedHashCode = 31 * 1 + serviceClazz.hashCode();

        assertEquals(expectedHashCode, descriptor.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = new Class();
        Class serviceClazz2 = new Class();

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "http://example.com/target", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, "http://example.com/target", "http://example.com/types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, "http://example.com/target", "http://example.com/types");

        assertEquals(descriptor1, descriptor1);
        assertEquals(descriptor1, descriptor2);
        assertEquals(descriptor2, descriptor1);
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
        assertEquals(descriptor1.equals(descriptor3), false);
    }
}