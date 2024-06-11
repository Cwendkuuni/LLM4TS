package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class testClass = TestService.class;
        String targetNamespace = "http://testnamespace.com";
        String typesNamespace = "http://testtypesnamespace.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testClass, targetNamespace, typesNamespace);
        
        assertEquals(testClass, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class testClass = TestService.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setServiceClazz(testClass);
        
        assertEquals(testClass, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://testnamespace.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://testtypesnamespace.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class testClass = TestService.class;
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(testClass, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(testClass, "", "");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class testClass = TestService.class;
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(testClass, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(testClass, "", "");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}

class TestService {}