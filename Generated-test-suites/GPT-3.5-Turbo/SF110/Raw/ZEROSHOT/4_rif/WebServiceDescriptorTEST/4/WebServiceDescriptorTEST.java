package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Test get serviceClazz
        Class expectedClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedClazz, "", "");
        Class actualClazz = descriptor.getServiceClazz();
        assertEquals(expectedClazz, actualClazz);
    }

    @Test
    public void testSetServiceClazz() {
        // Test set serviceClazz
        Class expectedClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "", "");
        descriptor.setServiceClazz(expectedClazz);
        Class actualClazz = descriptor.getServiceClazz();
        assertEquals(expectedClazz, actualClazz);
    }

    @Test
    public void testGetTargetNamespace() {
        // Test get targetNamespace
        String expectedTargetNamespace = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expectedTargetNamespace, "");
        String actualTargetNamespace = descriptor.getTargetNamespace();
        assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testSetTargetNamespace() {
        // Test set targetNamespace
        String expectedTargetNamespace = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTargetNamespace(expectedTargetNamespace);
        String actualTargetNamespace = descriptor.getTargetNamespace();
        assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testGetTypesNamespace() {
        // Test get typesNamespace
        String expectedTypesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", expectedTypesNamespace);
        String actualTypesNamespace = descriptor.getTypesNamespace();
        assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testSetTypesNamespace() {
        // Test set typesNamespace
        String expectedTypesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTypesNamespace(expectedTypesNamespace);
        String actualTypesNamespace = descriptor.getTypesNamespace();
        assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testHashCode() {
        // Test hashCode
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Test equals
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");
        boolean equals = descriptor1.equals(descriptor2);
        assertTrue(equals);
    }

}