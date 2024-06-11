package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "targetNamespace", "typesNamespace");
        Class actual = descriptor.getServiceClazz();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, expected, "typesNamespace");
        String actual = descriptor.getTargetNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        String expected = "newTargetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", expected);
        String actual = descriptor.getTypesNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        String expected = "newTypesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(Integer.class, "targetNamespace", "typesNamespace");
        Assert.assertTrue(descriptor1.equals(descriptor2));
        Assert.assertFalse(descriptor1.equals(descriptor3));
    }
}