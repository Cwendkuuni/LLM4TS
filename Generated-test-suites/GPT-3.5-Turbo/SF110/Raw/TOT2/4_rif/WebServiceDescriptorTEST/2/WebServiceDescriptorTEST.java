package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "target", "types");
        assertEquals(expected, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setServiceClazz(expected);
        assertEquals(expected, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        assertEquals("target", descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setTargetNamespace("newTarget");
        assertEquals("newTarget", descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        assertEquals("types", descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setTypesNamespace("newTypes");
        assertEquals("newTypes", descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "target", "types");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "target", "types");
        assertTrue(descriptor1.equals(descriptor2));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        Object obj = new Object();
        assertFalse(descriptor.equals(obj));
    }
    
    @Test
    public void testEqualsNullClass() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, "target", "types");
        assertFalse(descriptor1.equals(descriptor2));
    }
    
    @Test
    public void testEqualsDifferentServiceClazz() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Integer.class, "target", "types");
        assertFalse(descriptor1.equals(descriptor2));
    }
}