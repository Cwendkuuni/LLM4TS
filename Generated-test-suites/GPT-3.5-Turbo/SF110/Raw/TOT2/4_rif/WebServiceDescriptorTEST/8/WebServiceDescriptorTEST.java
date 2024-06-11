package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private Class testServiceClazz = TestService.class;
    private String testTargetNamespace = "http://test.com";
    private String testTypesNamespace = "http://types.com";

    @Test
    public void testConstructor() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testServiceClazz, testTargetNamespace, testTypesNamespace);
        assertEquals(testServiceClazz, descriptor.getServiceClazz());
        assertEquals(testTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(testTypesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testServiceClazz, testTargetNamespace, testTypesNamespace);
        Class newServiceClazz = AnotherTestService.class;
        descriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testServiceClazz, testTargetNamespace, testTypesNamespace);
        String newTargetNamespace = "http://newtest.com";
        descriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testServiceClazz, testTargetNamespace, testTypesNamespace);
        String newTypesNamespace = "http://newtypes.com";
        descriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testServiceClazz, testTargetNamespace, testTypesNamespace);
        int hashCode = 31 * 1 + ((testServiceClazz == null) ? 0 : testServiceClazz.hashCode());
        assertEquals(hashCode, descriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(testServiceClazz, testTargetNamespace, testTypesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(AnotherTestService.class, testTargetNamespace, testTypesNamespace);
        assertFalse(descriptor1.equals(null));
        assertFalse(descriptor1.equals(5));
        assertFalse(descriptor1.equals(descriptor2));
        assertTrue(descriptor1.equals(descriptor1));
    }
}