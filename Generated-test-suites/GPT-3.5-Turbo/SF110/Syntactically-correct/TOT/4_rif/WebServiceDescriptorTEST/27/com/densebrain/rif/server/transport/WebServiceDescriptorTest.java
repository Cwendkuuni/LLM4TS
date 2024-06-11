package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Arrange
        Class expectedClazz = Object.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedClazz, "", "");
        
        // Act
        Class actualClazz = descriptor.getServiceClazz();
        
        // Assert
        Assert.assertEquals(expectedClazz, actualClazz);
    }
    
    @Test
    public void testSetServiceClazz() {
        // Arrange
        Class expectedClazz = Object.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        // Act
        descriptor.setServiceClazz(expectedClazz);
        Class actualClazz = descriptor.getServiceClazz();
        
        // Assert
        Assert.assertEquals(expectedClazz, actualClazz);
    }
    
    @Test
    public void testGetTargetNamespace() {
        // Arrange
        String expectedNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expectedNamespace, "");
        
        // Act
        String actualNamespace = descriptor.getTargetNamespace();
        
        // Assert
        Assert.assertEquals(expectedNamespace, actualNamespace);
    }
    
    @Test
    public void testSetTargetNamespace() {
        // Arrange
        String expectedNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "");
        
        // Act
        descriptor.setTargetNamespace(expectedNamespace);
        String actualNamespace = descriptor.getTargetNamespace();
        
        // Assert
        Assert.assertEquals(expectedNamespace, actualNamespace);
    }
    
    @Test
    public void testGetTypesNamespace() {
        // Arrange
        String expectedNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", expectedNamespace);
        
        // Act
        String actualNamespace = descriptor.getTypesNamespace();
        
        // Assert
        Assert.assertEquals(expectedNamespace, actualNamespace);
    }
    
    @Test
    public void testSetTypesNamespace() {
        // Arrange
        String expectedNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", null);
        
        // Act
        descriptor.setTypesNamespace(expectedNamespace);
        String actualNamespace = descriptor.getTypesNamespace();
        
        // Assert
        Assert.assertEquals(expectedNamespace, actualNamespace);
    }
    
    @Test
    public void testHashCode() {
        // Arrange
        Class clazz1 = Object.class;
        Class clazz2 = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz1, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz2, "", "");
        
        // Act
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        
        // Assert
        Assert.assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        // Arrange
        Class clazz1 = Object.class;
        Class clazz2 = clazz1;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz1, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz2, "", "");
        
        // Act
        boolean equals = descriptor1.equals(descriptor2);
        
        // Assert
        Assert.assertTrue(equals);
    }
}