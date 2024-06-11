package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testGetServiceClazz() {
        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        assertEquals(String.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        webServiceDescriptor.setTargetNamespace("http://example.com");
        assertEquals("http://example.com", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        webServiceDescriptor.setTypesNamespace("http://example.com/types");
        assertEquals("http://example.com/types", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = webServiceDescriptor.getServiceClazz() != null ? webServiceDescriptor.getServiceClazz().hashCode() : 0;
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor other = new WebServiceDescriptor(null, null, null);
        assertTrue(webServiceDescriptor.equals(other));

        other = new WebServiceDescriptor(String.class, null, null);
        assertFalse(webServiceDescriptor.equals(other));

        other = new WebServiceDescriptor(null, null, null);
        assertFalse(webServiceDescriptor.equals(other));

        other = new WebServiceDescriptor(null, null, null);
        assertFalse(webServiceDescriptor.equals(null));
    }
}