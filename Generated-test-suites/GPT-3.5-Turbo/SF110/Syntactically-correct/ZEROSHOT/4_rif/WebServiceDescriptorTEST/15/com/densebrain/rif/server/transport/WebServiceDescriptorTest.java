package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class expected = Class.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "targetNamespace", "typesNamespace");
        Class actual = descriptor.getServiceClazz();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expected = Class.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTargetNamespace() {
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, expected, "typesNamespace");
        String actual = descriptor.getTargetNamespace();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetTargetNamespace() {
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "oldTargetNamespace", "typesNamespace");
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTypesNamespace() {
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "targetNamespace", expected);
        String actual = descriptor.getTypesNamespace();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetTypesNamespace() {
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "targetNamespace", "oldTypesNamespace");
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = Class.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        int expected = 31 * ((serviceClazz == null) ? 0 : serviceClazz.hashCode());
        int actual = descriptor.hashCode();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_sameObject() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "targetNamespace", "typesNamespace");
        
        Assert.assertTrue(descriptor.equals(descriptor));
    }
    
    @Test
    public void testEquals_nullObject() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "targetNamespace", "typesNamespace");
        
        Assert.assertFalse(descriptor.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "targetNamespace", "typesNamespace");
        Object other = new Object();
        
        Assert.assertFalse(descriptor.equals(other));
    }
    
    @Test
    public void testEquals_differentServiceClazz() {
        Class serviceClazz = Class.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        
        Assert.assertFalse(descriptor.equals(other));
    }
    
    @Test
    public void testEquals_sameServiceClazz() {
        Class serviceClazz = Class.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor other = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        
        Assert.assertTrue(descriptor.equals(other));
    }
    
}