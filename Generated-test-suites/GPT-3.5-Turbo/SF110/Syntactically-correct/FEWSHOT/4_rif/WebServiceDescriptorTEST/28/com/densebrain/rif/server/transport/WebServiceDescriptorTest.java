package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz1 = String.class;
        Class serviceClazz2 = Integer.class;

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz1, "", "");

        descriptor.setServiceClazz(serviceClazz2);

        assertEquals(serviceClazz2, descriptor.getServiceClazz());
        assertNotEquals(serviceClazz1, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace1 = "http://www.example1.com";
        String targetNamespace2 = "http://www.example2.com";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace1, "");

        descriptor.setTargetNamespace(targetNamespace2);

        assertEquals(targetNamespace2, descriptor.getTargetNamespace());
        assertNotEquals(targetNamespace1, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace1 = "http://www.example1.com/types";
        String typesNamespace2 = "http://www.example2.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", typesNamespace1);

        descriptor.setTypesNamespace(typesNamespace2);

        assertEquals(typesNamespace2, descriptor.getTypesNamespace());
        assertNotEquals(typesNamespace1, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        WebServiceDescriptor duplicateDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(descriptor.hashCode(), duplicateDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = String.class;
        Class serviceClazz2 = Integer.class;

        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertEquals(descriptor1, descriptor2);
        assertNotEquals(descriptor1, descriptor3);
    }
}