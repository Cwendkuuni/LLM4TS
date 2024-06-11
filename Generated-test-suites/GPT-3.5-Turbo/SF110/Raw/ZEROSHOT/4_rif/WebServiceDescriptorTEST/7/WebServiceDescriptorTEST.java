package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testgetServiceClazz() {
        // Setup
        Class expected = null;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "", "");

        // Execute
        Class actual = descriptor.getServiceClazz();

        // Verify
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testsetServiceClazz() {
        // Setup
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");

        // Execute
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();

        // Verify
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testgetTargetNamespace() {
        // Setup
        String expected = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expected, "");

        // Execute
        String actual = descriptor.getTargetNamespace();

        // Verify
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testsetTargetNamespace() {
        // Setup
        String expected = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "");

        // Execute
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();

        // Verify
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testgetTypesNamespace() {
        // Setup
        String expected = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", expected);

        // Execute
        String actual = descriptor.getTypesNamespace();

        // Verify
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testsetTypesNamespace() {
        // Setup
        String expected = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", null);

        // Execute
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();

        // Verify
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testhashCode() {
        // Setup
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");

        // Execute
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        // Verify
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testequals() {
        // Setup
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");

        // Execute
        boolean equals = descriptor1.equals(descriptor2);

        // Verify
        Assert.assertTrue(equals);
    }
}