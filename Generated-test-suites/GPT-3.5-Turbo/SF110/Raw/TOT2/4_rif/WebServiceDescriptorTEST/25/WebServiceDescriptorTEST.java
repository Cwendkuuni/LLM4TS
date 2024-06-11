package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class clazz = DummyService.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://types.example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, targetNamespace, typesNamespace);
        
        assertEquals(clazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class clazz = DummyService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setServiceClazz(clazz);
        
        assertEquals(clazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://types.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class clazz = DummyService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class clazz = DummyService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");
        
        assertTrue(descriptor1.equals(descriptor2));
    }

    public class DummyService {
    }
}