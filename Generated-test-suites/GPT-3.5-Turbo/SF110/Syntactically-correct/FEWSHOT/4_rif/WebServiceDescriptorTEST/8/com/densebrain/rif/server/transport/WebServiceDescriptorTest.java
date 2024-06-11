package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructorAndGetters() {
        Class serviceClass = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClass, targetNamespace, typesNamespace);

        assertEquals(serviceClass, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://www.example.com/target", "http://www.example.com/types");
        
        Class newServiceClass = Integer.class;
        String newTargetNamespace = "http://www.newexample.com/target";
        String newTypesNamespace = "http://www.newexample.com/types";
        
        descriptor.setServiceClazz(newServiceClass);
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newServiceClass, descriptor.getServiceClazz());
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClass = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClass, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClass, targetNamespace, typesNamespace);

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClass = String.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClass, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClass, targetNamespace, typesNamespace);

        assertEquals(descriptor1, descriptor2);
    } 
}