package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
  
  @Test
  public void testGetServiceClazz() {
    Class expectedServiceClazz = null;
    WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedServiceClazz, "targetNamespace", "typesNamespace");
    
    Class actualServiceClazz = descriptor.getServiceClazz();
    
    assertEquals(expectedServiceClazz, actualServiceClazz);
  }
  
  @Test
  public void testSetServiceClazz() {
    Class expectedServiceClazz = String.class;
    WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    
    descriptor.setServiceClazz(expectedServiceClazz);
    Class actualServiceClazz = descriptor.getServiceClazz();
    
    assertEquals(expectedServiceClazz, actualServiceClazz);
  }
  
  @Test
  public void testGetTargetNamespace() {
    String expectedTargetNamespace = "targetNamespace";
    WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expectedTargetNamespace, "typesNamespace");
    
    String actualTargetNamespace = descriptor.getTargetNamespace();
    
    assertEquals(expectedTargetNamespace, actualTargetNamespace);
  }
  
  @Test
  public void testSetTargetNamespace() {
    String expectedTargetNamespace = "newTargetNamespace";
    WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    
    descriptor.setTargetNamespace(expectedTargetNamespace);
    String actualTargetNamespace = descriptor.getTargetNamespace();
    
    assertEquals(expectedTargetNamespace, actualTargetNamespace);
  }
  
  @Test
  public void testGetTypesNamespace() {
    String expectedTypesNamespace = "typesNamespace";
    WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", expectedTypesNamespace);
    
    String actualTypesNamespace = descriptor.getTypesNamespace();
    
    assertEquals(expectedTypesNamespace, actualTypesNamespace);
  }
  
  @Test
  public void testSetTypesNamespace() {
    String expectedTypesNamespace = "newTypesNamespace";
    WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    
    descriptor.setTypesNamespace(expectedTypesNamespace);
    String actualTypesNamespace = descriptor.getTypesNamespace();
    
    assertEquals(expectedTypesNamespace, actualTypesNamespace);
  }
  
  @Test
  public void testHashCode() {
    Class serviceClazz = String.class;
    WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
    WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
    
    int hashCode1 = descriptor1.hashCode();
    int hashCode2 = descriptor2.hashCode();
    
    assertEquals(hashCode1, hashCode2);
  }
  
  @Test
  public void testEquals() {
    Class serviceClazz = String.class;
    WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
    WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
    
    boolean equals = descriptor1.equals(descriptor2);
    
    assertTrue(equals);
  }
}