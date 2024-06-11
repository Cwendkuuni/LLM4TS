package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://example.com/wsdl", "http://example.com/types");
    }

    @Test
    public void testGetServiceClazz() {
        assertEquals(MyService.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webServiceDescriptor.setServiceClazz(TestService.class);
        assertEquals(TestService.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertEquals("http://example.com/wsdl", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        webServiceDescriptor.setTargetNamespace("http://example.com/newwsdl");
        assertEquals("http://example.com/newwsdl", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertEquals("http://example.com/types", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        webServiceDescriptor.setTypesNamespace("http://example.com/newtypes");
        assertEquals("http://example.com/newtypes", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://example.com/wsdl", "http://example.com/types");
        assertEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor sameWebServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://example.com/wsdl", "http://example.com/types");
        WebServiceDescriptor differentWebServiceDescriptor = new WebServiceDescriptor(TestService.class, "http://example.com/wsdl", "http://example.com/types");
        assertTrue(webServiceDescriptor.equals(sameWebServiceDescriptor));
        assertFalse(webServiceDescriptor.equals(differentWebServiceDescriptor));
    }
}