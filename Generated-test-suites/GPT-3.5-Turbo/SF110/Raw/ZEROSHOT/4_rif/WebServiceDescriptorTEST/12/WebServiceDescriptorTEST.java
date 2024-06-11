package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, null);

        // Set serviceClazz using reflection
        Class clazz = String.class;
        wsDescriptor.setServiceClazz(clazz);

        // Verify that the set value is returned correctly
        assertEquals(clazz, wsDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, null);

        // Set serviceClazz using setter method
        Class clazz = String.class;
        wsDescriptor.setServiceClazz(clazz);

        // Verify that the set value is returned correctly
        assertEquals(clazz, wsDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);

        // Verify that the set value is returned correctly
        assertEquals("targetNamespace", wsDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, null);

        // Set targetNamespace using setter method
        wsDescriptor.setTargetNamespace("targetNamespace");

        // Verify that the set value is returned correctly
        assertEquals("targetNamespace", wsDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");

        // Verify that the set value is returned correctly
        assertEquals("typesNamespace", wsDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, null);

        // Set typesNamespace using setter method
        wsDescriptor.setTypesNamespace("typesNamespace");

        // Verify that the set value is returned correctly
        assertEquals("typesNamespace", wsDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        // Create an instance of WebServiceDescriptor
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, null);

        // Verify that hashCode is calculated correctly
        assertEquals(31, wsDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        // Create instance of WebServiceDescriptor with same serviceClazz
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(String.class, null, null);
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(String.class, null, null);

        // Verify that equals returns true for the same serviceClazz
        assertTrue(wsDescriptor1.equals(wsDescriptor2));

        // Create instance of WebServiceDescriptor with different serviceClazz
        WebServiceDescriptor wsDescriptor3 = new WebServiceDescriptor(Integer.class, null, null);

        // Verify that equals returns false for different serviceClazz
        assertFalse(wsDescriptor1.equals(wsDescriptor3));
    }
}