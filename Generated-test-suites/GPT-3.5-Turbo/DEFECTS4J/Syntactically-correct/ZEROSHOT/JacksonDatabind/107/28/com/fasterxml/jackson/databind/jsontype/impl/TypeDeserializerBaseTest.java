package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

  @Test
  public void testBaseTypeName() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
      @Override
      public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
        return null;
      }

      @Override
      public TypeDeserializerBase forProperty(BeanProperty prop) {
        return null;
      }
    };
    String actual = typeDeserializerBase.baseTypeName();
    assertEquals(null, actual);
  }

  @Test
  public void testGetPropertyName() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "typePropertyName", true, null) {
      @Override
      public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
        return null;
      }

      @Override
      public TypeDeserializerBase forProperty(BeanProperty prop) {
        return null;
      }
    };
    String actual = typeDeserializerBase.getPropertyName();
    assertEquals("typePropertyName", actual);
  }

  @Test
  public void testGetTypeIdResolver() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
      @Override
      public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
        return null;
      }

      @Override
      public TypeDeserializerBase forProperty(BeanProperty prop) {
        return null;
      }
    };
    TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
    assertEquals(null, actual);
  }

  @Test
  public void testGetDefaultImpl() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
      @Override
      public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
        return null;
      }

      @Override
      public TypeDeserializerBase forProperty(BeanProperty prop) {
        return null;
      }
    };
    Class<?> actual = typeDeserializerBase.getDefaultImpl();
    assertEquals(null, actual);
  }

  @Test
  public void testBaseType() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
      @Override
      public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
        return null;
      }

      @Override
      public TypeDeserializerBase forProperty(BeanProperty prop) {
        return null;
      }
    };
    org.codehaus.jackson.annotate.JavaType actual = typeDeserializerBase.baseType();
    assertEquals(null, actual);
  }

  @Test
  public void testToString() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
      @Override
      public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
        return null;
      }

      @Override
      public TypeDeserializerBase forProperty(BeanProperty prop) {
        return null;
      }
    };
    String actual = typeDeserializerBase.toString();
    assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", actual);
  }
  
}