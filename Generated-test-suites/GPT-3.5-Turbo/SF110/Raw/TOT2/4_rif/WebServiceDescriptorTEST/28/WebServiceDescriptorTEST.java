package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
  
  @Test
  public void testGetServiceClazz() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Test the getServiceClazz method
    assertEquals(String.class, descriptor.getServiceClazz());
  }
  
  @Test
  public void testSetServiceClazz() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Set a new service class and test if it was set correctly
    descriptor.setServiceClazz(Integer.class);
    assertEquals(Integer.class, descriptor.getServiceClazz());
  }
  
  @Test
  public void testGetTargetNamespace() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Test the getTargetNamespace method
    assertEquals("http://example.com", descriptor.getTargetNamespace());
  }
  
  @Test
  public void testSetTargetNamespace() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Set a new target namespace and test if it was set correctly
    descriptor.setTargetNamespace("http://example.org");
    assertEquals("http://example.org", descriptor.getTargetNamespace());
  }
  
  @Test
  public void testGetTypesNamespace() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Test the getTypesNamespace method
    assertEquals("http://example.com/types", descriptor.getTypesNamespace());
  }
  
  @Test
  public void testSetTypesNamespace() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Set a new types namespace and test if it was set correctly
    descriptor.setTypesNamespace("http://example.org/types");
    assertEquals("http://example.org/types", descriptor.getTypesNamespace());
  }
  
  @Test
  public void testEquals() {
    // Create two WebServiceDescriptor objects with the same parameters
    WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Test if the two objects are equal
    assertTrue(descriptor1.equals(descriptor2));
  }
  
  @Test
  public void testHashCode() {
    // Create a WebServiceDescriptor object
    WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://example.com/types");
    
    // Expected hash code value
    int expectedHashCode = 31 * 1 + ((String.class == null) ? 0 : String.class.hashCode());
    
    // Test the hashCode method
    assertEquals(expectedHashCode, descriptor.hashCode());
  }
}