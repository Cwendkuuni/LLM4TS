package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TypeDeserializerBaseTest {
  
  private TypeDeserializerBase typeDeserializer;

  @Test
  public void testBaseTypeName() {
    typeDeserializer = new TypeDeserializerBase() {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
  
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    String expected = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
    assertEquals(expected, typeDeserializer.baseTypeName());
  }
  
  @Test
  public void testGetPropertyName() {
    typeDeserializer = new TypeDeserializerBase() {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
  
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    String expected = "_typePropertyName";
    assertEquals(expected, typeDeserializer.getPropertyName());
  }
  
  @Test
  public void testGetTypeIdResolver() {
    typeDeserializer = new TypeDeserializerBase() {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
  
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    TypeIdResolver expected = null;
    assertEquals(expected, typeDeserializer.getTypeIdResolver());
  }
  
  @Test
  public void testGetDefaultImpl() {
    typeDeserializer = new TypeDeserializerBase() {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
  
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    Class<?> expected = null;
    assertEquals(expected, typeDeserializer.getDefaultImpl());
  }
  
  @Test
  public void testBaseType() {
    typeDeserializer = new TypeDeserializerBase() {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
  
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    JavaType expected = null;
    assertEquals(expected, typeDeserializer.baseType());
  }

}