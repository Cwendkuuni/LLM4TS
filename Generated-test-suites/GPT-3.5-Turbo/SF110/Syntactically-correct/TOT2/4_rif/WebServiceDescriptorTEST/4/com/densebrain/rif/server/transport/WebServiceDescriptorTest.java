package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class testClass = TestClass.class;
        String targetNamespace = "http://test.com";
        String typesNamespace = "http://types.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testClass, targetNamespace, typesNamespace);

        assertEquals(testClass, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class testClass1 = TestClass.class;
        Class testClass2 = TestClass2.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testClass1, "http://test.com", "http://types.com");

        descriptor.setServiceClazz(testClass2);

        assertEquals(testClass2, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");

        String newNamespace = "http://new.com";
        descriptor.setTargetNamespace(newNamespace);

        assertEquals(newNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");

        String newNamespace = "http://newtypes.com";
        descriptor.setTypesNamespace(newNamespace);

        assertEquals(newNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");

        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void testNotEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestClass.class, "http://test.com", "http://types.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestClass2.class, "http://test2.com", "http://types2.com");

        assertFalse(descriptor1.equals(descriptor2));
    }

    class TestClass {}
    class TestClass2 {}
}