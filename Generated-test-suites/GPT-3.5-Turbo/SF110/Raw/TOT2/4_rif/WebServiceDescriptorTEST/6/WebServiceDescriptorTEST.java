package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(serviceClazz, "targetNS", "typesNS");
        assertEquals(serviceClazz, wsDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "targetNS", "typesNS");
        Class serviceClazz = Integer.class;
        wsDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, wsDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "targetNS";
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, targetNamespace, "typesNS");
        assertEquals(targetNamespace, wsDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, null, "typesNS");
        String targetNamespace = "targetNS";
        wsDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, wsDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNS";
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "targetNS", typesNamespace);
        assertEquals(typesNamespace, wsDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "targetNS", null);
        String typesNamespace = "typesNS";
        wsDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, wsDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(null, "targetNS", "typesNS");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(null, "targetNS", "typesNS");

        assertEquals(wsDescriptor1.hashCode(), wsDescriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(null, "targetNS", "typesNS");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(null, "targetNS", "typesNS");

        assertTrue(wsDescriptor1.equals(wsDescriptor2));
    }

}