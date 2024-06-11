package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBuilderTest {
  
    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        assertNull(propertyBuilder.getDefaultBean());
    }
  
    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        assertNull(propertyBuilder.getPropertyDefaultValue("propName", null, null));
    }
  
    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        assertNull(propertyBuilder.getDefaultValue(null));
    }
  
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
            fail("Did not throw expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected JsonMappingException
        }
    }
  
    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        try {
            propertyBuilder.findSerializationType(null, false, null);
            fail("Did not throw expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected JsonMappingException
        }
    }
  
    @Test
    public void testFindUnwrappingNameTransformer() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        assertNull(propertyBuilder.findUnwrappingNameTransformer(null));
    }
  
    @Test
    public void testFindNullSerializer() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        assertNull(propertyBuilder.findNullSerializer(null));
    }
  
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
      
        assertNotNull(propertyBuilder.getClassAnnotations());
    }
  
}