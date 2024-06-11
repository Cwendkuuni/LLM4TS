package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testGetServiceClazz() {
        assertNotNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();
        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertNotNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "test";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertNotNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "test";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * (webServiceDescriptor.getServiceClazz() == null ? 0 : webServiceDescriptor.getServiceClazz().hashCode());
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor otherWebServiceDescriptor = new WebServiceDescriptor(null, null, null);
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        assertFalse(webServiceDescriptor.equals(null));
        assertFalse(webServiceDescriptor.equals(new Object()));
        assertFalse(webServiceDescriptor.equals(otherWebServiceDescriptor));
    }
}