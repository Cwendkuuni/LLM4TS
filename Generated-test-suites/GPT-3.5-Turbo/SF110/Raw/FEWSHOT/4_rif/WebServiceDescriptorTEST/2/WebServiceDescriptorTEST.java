package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Class newServiceClazz = String.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        String newTargetNamespace = "http://www.example.com/new";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        String newTypesNamespace = "http://www.example.com/new/types";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        int expectedHashCode = 31 * 1 + ((serviceClazz == null) ? 0 : serviceClazz.hashCode());

        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = null;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(true, webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}