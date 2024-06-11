package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://www.example.com/namespace";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, "");
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://www.example.com/namespace";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "");
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", null);
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
    
    
}