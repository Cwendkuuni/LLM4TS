package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class testClazz = TestClass.class;
        String testTargetNamespace = "http://test/targetNamespace";
        String testTypesNamespace = "http://test/typesNamespace";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testClazz, testTargetNamespace, testTypesNamespace);
        
        assertEquals(testClazz, descriptor.getServiceClazz());
        assertEquals(testTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(testTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class testClazz = TestClass.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setServiceClazz(testClazz);
        
        assertEquals(testClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String testTargetNamespace = "http://test/targetNamespace";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTargetNamespace(testTargetNamespace);
        
        assertEquals(testTargetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String testTypesNamespace = "http://test/typesNamespace";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTypesNamespace(testTypesNamespace);
        
        assertEquals(testTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class testClazz = TestClass.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testClazz, "", "");
        
        int expectedHashCode = (31 * 1) + testClazz.hashCode();
        
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestClass.class, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestClass.class, "", "");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}

class TestClass {}