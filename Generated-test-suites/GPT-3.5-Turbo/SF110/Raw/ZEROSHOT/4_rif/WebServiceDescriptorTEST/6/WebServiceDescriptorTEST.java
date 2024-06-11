package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor descriptor;

    @Before
    public void setUp() {
        descriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testGetServiceClazz() {
        assertNull(descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        descriptor.setServiceClazz(Object.class);
        assertEquals(Object.class, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertNull(descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        descriptor.setTargetNamespace("namespace");
        assertEquals("namespace", descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertNull(descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        descriptor.setTypesNamespace("namespace");
        assertEquals("namespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        assertEquals(31, descriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor other = new WebServiceDescriptor(null, null, null);
        assertTrue(descriptor.equals(other));

        other.setServiceClazz(Object.class);
        assertFalse(descriptor.equals(other));

        descriptor.setServiceClazz(Object.class);
        assertTrue(descriptor.equals(other));
    }
}