package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "namespace1", "namespace2");
        assertEquals(expected, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        String expected = "newNamespace";
        descriptor.setTargetNamespace(expected);
        assertEquals(expected, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        assertEquals("namespace2", descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        String expected = "newNamespace";
        descriptor.setTypesNamespace(expected);
        assertEquals(expected, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        assertTrue(descriptor1.equals(descriptor2));
    }
}
