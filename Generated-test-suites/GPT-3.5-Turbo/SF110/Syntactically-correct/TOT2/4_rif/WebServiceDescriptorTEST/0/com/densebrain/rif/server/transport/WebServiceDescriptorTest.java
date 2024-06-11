package com.densebrain.rif.server.transport;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructor() {
        Class clazz = String.class;
        String targetNamespace = "http://test.com";
        String typesNamespace = "http://types.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, targetNamespace, typesNamespace);
        
        assertEquals(clazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class clazz = Integer.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://types.com");
        descriptor.setServiceClazz(clazz);
        
        assertEquals(clazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://newtarget.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://types.com");
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://newtypes.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://types.com");
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://types.com");
        
        int hash = 31 * 1 + "http://example.com".hashCode();
        
        assertEquals(hash, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "http://example.com", "http://types.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "http://example.com", "http://types.com");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}