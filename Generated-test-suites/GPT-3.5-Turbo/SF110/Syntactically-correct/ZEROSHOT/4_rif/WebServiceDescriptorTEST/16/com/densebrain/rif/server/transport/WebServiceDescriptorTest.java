package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptorTest.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = WebServiceDescriptorTest.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        Class newServiceClazz = String.class;
        descriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://www.example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, targetNamespace, "");
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://www.example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, "", "");
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, "", typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, "", "");
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = WebServiceDescriptorTest.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = WebServiceDescriptorTest.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        assertEquals(descriptor1, descriptor2);
    }
}