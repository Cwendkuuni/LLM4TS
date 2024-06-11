package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {
  
  @Test
  public void testBuildWriter() {
    SerializationConfig config = new SerializationConfig();
    BeanDescription beanDesc = new BeanDescription();
    PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
    
    // Create dummy objects for testing
    SerializerProvider prov = new SerializerProvider();
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType declaredType = new JavaType();
    JsonSerializer<?> ser = new JsonSerializer<?>();
    TypeSerializer typeSer = new TypeSerializer();
    TypeSerializer contentTypeSer = new TypeSerializer();
    AnnotatedMember am = new AnnotatedMember();
    boolean defaultUseStaticTyping = true;
    
    try {
      // Call the method to be tested
      BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
      
      // Assertion statements
      assertNotNull(writer);
      assertEquals(propDef, writer.getPropDef());
      assertEquals(am, writer.getMember());
      
    } catch (JsonMappingException e) {
      // Exception handling
      e.printStackTrace();
    }
  }
  
  @Test
  public void testFindSerializationType() {
    PropertyBuilder propertyBuilder = new PropertyBuilder();
    AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
    boolean useStaticTyping = true;
    JavaType declaredType = new JavaType();
    
    try {
      // Call the method to be tested
      JavaType serializationType = propertyBuilder.findSerializationType(annotationIntrospector, useStaticTyping, declaredType);
      
      // Assertion statements
      assertNull(serializationType);
      
    } catch (JsonMappingException e) {
      // Exception handling
      e.printStackTrace();
    }
  }
  
  @Test
  public void testGetPropertyDefaultValue() {
    PropertyBuilder propertyBuilder = new PropertyBuilder();
    String name = "test";
    AnnotatedMember member = new AnnotatedMember();
    JavaType type = new JavaType();
    
    // Create dummy object for testing
    Object defaultBean = new Object();
    
    // Call the method to be tested
    Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type, defaultBean);
    
    // Assertion statements
    assertNull(defaultValue);
  }
  
  @Test
  public void testGetDefaultValue() {
    PropertyBuilder propertyBuilder = new PropertyBuilder();
    JavaType type = new JavaType();
    
    // Create dummy object for testing
    Class<?> cls = new Class<?>();
    
    // Call the method to be tested
    Object defaultValue = propertyBuilder.getDefaultValue(type, cls);
    
    // Assertion statements
    assertNull(defaultValue);
  }
}