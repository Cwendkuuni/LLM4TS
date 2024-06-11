package com.densebrain.rif.server.transport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "testTargetNamespace", "testTypesNamespace");
        assertEquals(TestService.class, descriptor.getServiceClazz());
        assertEquals("testTargetNamespace", descriptor.getTargetNamespace());
        assertEquals("testTypesNamespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(AnotherTestService.class);
        assertEquals(AnotherTestService.class, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        descriptor.setTargetNamespace("newTargetNamespace");
        assertEquals("newTargetNamespace", descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        descriptor.setTypesNamespace("newTypesNamespace");
        assertEquals("newTypesNamespace", descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(AnotherTestService.class, "targetNamespace", "typesNamespace");
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }

    static class TestService {}

    static class AnotherTestService {}

}