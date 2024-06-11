package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = null;
        String targetNamespace = null;
        String typesNamespace = null;

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = null;

        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setServiceClazz(serviceClazz);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = null;

        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setTargetNamespace(targetNamespace);

        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = null;

        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setTypesNamespace(typesNamespace);

        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = null;

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);

        int expected = 1;
        int actual = descriptor.hashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class serviceClazz = null;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, null, null);

        assertTrue(descriptor1.equals(descriptor2));
    }

}