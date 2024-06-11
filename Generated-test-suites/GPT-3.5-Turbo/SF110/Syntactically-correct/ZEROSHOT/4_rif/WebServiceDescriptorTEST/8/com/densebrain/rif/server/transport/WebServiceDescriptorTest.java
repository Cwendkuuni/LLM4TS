package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Given
        Class expectedServiceClazz = Object.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedServiceClazz, "", "");

        // When
        Class actualServiceClazz = descriptor.getServiceClazz();

        // Then
        Assert.assertEquals(expectedServiceClazz, actualServiceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        // Given
        Class expectedServiceClazz = Object.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor();

        // When
        descriptor.setServiceClazz(expectedServiceClazz);
        Class actualServiceClazz = descriptor.getServiceClazz();

        // Then
        Assert.assertEquals(expectedServiceClazz, actualServiceClazz);
    }

    @Test
    public void testGetTargetNamespace() {
        // Given
        String expectedTargetNamespace = "testNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expectedTargetNamespace, null);

        // When
        String actualTargetNamespace = descriptor.getTargetNamespace();

        // Then
        Assert.assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testSetTargetNamespace() {
        // Given
        String expectedTargetNamespace = "testNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor();

        // When
        descriptor.setTargetNamespace(expectedTargetNamespace);
        String actualTargetNamespace = descriptor.getTargetNamespace();

        // Then
        Assert.assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testGetTypesNamespace() {
        // Given
        String expectedTypesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, expectedTypesNamespace);

        // When
        String actualTypesNamespace = descriptor.getTypesNamespace();

        // Then
        Assert.assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testSetTypesNamespace() {
        // Given
        String expectedTypesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor();

        // When
        descriptor.setTypesNamespace(expectedTypesNamespace);
        String actualTypesNamespace = descriptor.getTypesNamespace();

        // Then
        Assert.assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testHashCode() {
        // Given
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, "", "");

        // When
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        // Then
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Given
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, "", "");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(Object.class, "", "");

        // Then
        Assert.assertTrue(descriptor1.equals(descriptor2));
        Assert.assertFalse(descriptor1.equals(descriptor3));
    }
}