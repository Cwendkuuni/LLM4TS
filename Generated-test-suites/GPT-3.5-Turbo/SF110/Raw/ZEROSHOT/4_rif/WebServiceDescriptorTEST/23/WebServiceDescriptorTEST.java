package com.densebrain.rif.server.transport;

import org.junit.*;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Arrange
        Class expected = null;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, null, null);
        
        // Act
        Class actual = descriptor.getServiceClazz();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        // Arrange
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        // Act
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        // Arrange
        String expected = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expected, null);
        
        // Act
        String actual = descriptor.getTargetNamespace();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        // Arrange
        String expected = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        // Act
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        // Arrange
        String expected = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, expected);
        
        // Act
        String actual = descriptor.getTypesNamespace();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        // Arrange
        String expected = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        
        // Act
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        // Arrange
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, null, null);
        
        // Act
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Arrange
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, null, null);
        
        // Act
        boolean equals = descriptor1.equals(descriptor2);
        
        // Assert
        assertTrue(equals);
    }
}