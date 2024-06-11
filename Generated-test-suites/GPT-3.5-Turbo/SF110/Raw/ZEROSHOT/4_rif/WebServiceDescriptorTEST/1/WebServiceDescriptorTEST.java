package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        // Test case 1: Clazz is null
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        Assert.assertNull(descriptor.getServiceClazz());
        
        // Test case 2: Clazz is not null
        Class clazz = String.class;
        descriptor = new WebServiceDescriptor(clazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals(clazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        // Test case 1: Set Clazz to null
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(null);
        Assert.assertNull(descriptor.getServiceClazz());
        
        // Test case 2: Set Clazz to a valid Class
        Class clazz = Integer.class;
        descriptor.setServiceClazz(clazz);
        Assert.assertEquals(clazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        // Test case 1: Target Namespace is null
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, null, "typesNamespace");
        Assert.assertNull(descriptor.getTargetNamespace());
        
        // Test case 2: Target Namespace is not null
        String targetNamespace = "http://www.example.com/";
        descriptor = new WebServiceDescriptor(String.class, targetNamespace, "typesNamespace");
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        // Test case 1: Set Target Namespace to null
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTargetNamespace(null);
        Assert.assertNull(descriptor.getTargetNamespace());
        
        // Test case 2: Set Target Namespace to a valid String
        String targetNamespace = "http://www.example.com/";
        descriptor.setTargetNamespace(targetNamespace);
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        // Test case 1: Types Namespace is null
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", null);
        Assert.assertNull(descriptor.getTypesNamespace());
        
        // Test case 2: Types Namespace is not null
        String typesNamespace = "http://www.example.com/types";
        descriptor = new WebServiceDescriptor(String.class, "targetNamespace", typesNamespace);
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        // Test case 1: Set Types Namespace to null
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTypesNamespace(null);
        Assert.assertNull(descriptor.getTypesNamespace());
        
        // Test case 2: Set Types Namespace to a valid String
        String typesNamespace = "http://www.example.com/types";
        descriptor.setTypesNamespace(typesNamespace);
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class clazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, "targetNamespace", "typesNamespace");
        
        int hashCode = 31 * 1 + (clazz == null ? 0 : clazz.hashCode());
        
        Assert.assertEquals(hashCode, descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "targetNamespace", "typesNamespace");
        
        // Test case 1: Same object reference
        Assert.assertTrue(descriptor1.equals(descriptor1));
        
        // Test case 2: Null object
        Assert.assertFalse(descriptor1.equals(null));
        
        // Test case 3: Different class
        Assert.assertFalse(descriptor1.equals("not a descriptor"));
        
        // Test case 4: Same class, same contents
        Assert.assertTrue(descriptor1.equals(descriptor2));
        
        // Test case 5: Same class, different contents
        descriptor1.setTargetNamespace("differentTargetNamespace");
        Assert.assertFalse(descriptor1.equals(descriptor2));
    }
}