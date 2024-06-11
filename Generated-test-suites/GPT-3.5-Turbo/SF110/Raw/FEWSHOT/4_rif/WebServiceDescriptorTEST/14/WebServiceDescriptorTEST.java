package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = new Class();
        String targetNamespace = "testTargetNamespace";
        String typesNamespace = "testTypesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNotNull(descriptor);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetters() {
        Class serviceClazz = new Class();
        String targetNamespace = "testTargetNamespace";
        String typesNamespace = "testTypesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNotNull(descriptor);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());

        Class newServiceClazz = new Class();
        String newTargetNamespace = "newTestTargetNamespace";
        String newTypesNamespace = "newTestTypesNamespace";

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
        String targetNamespace = "testTargetNamespace";
        String typesNamespace = "testTypesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = new Class();
        String targetNamespace = "testTargetNamespace";
        String typesNamespace = "testTypesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertTrue(descriptor1.equals(descriptor2));
        assertTrue(descriptor2.equals(descriptor1));

        targetNamespace = "newTestTargetNamespace";
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertFalse(descriptor1.equals(descriptor3));
        assertFalse(descriptor3.equals(descriptor1));
    }
}