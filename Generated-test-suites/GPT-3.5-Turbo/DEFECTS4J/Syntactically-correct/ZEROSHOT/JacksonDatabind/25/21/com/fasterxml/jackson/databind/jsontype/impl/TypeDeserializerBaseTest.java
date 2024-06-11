package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class TypeDeserializerBaseTest {

  @Test
  public void test_forProperty() {
    BeanProperty prop = mock(BeanProperty.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }

      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    TypeDeserializer result = typeDeserializerBase.forProperty(prop);
    assertEquals(null, result);
  }

  @Test
  public void test_getTypeName() {
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "typeName", false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }

      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    String result = typeDeserializerBase.getPropertyName();
    assertEquals("typeName", result);
  }

  @Test
  public void test_getTypeIdResolver() {
    TypeIdResolver idResolver = mock(TypeIdResolver.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }

      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
    assertEquals(idResolver, result);
  }
    
  @Test
  public void test_getDefaultImpl() {
    JavaType baseType = mock(JavaType.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }

      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    Class<?> result = typeDeserializerBase.getDefaultImpl();
    assertEquals(java.lang.Object.class, result);
  }
  
  @Test
  public void test_toString() {
    JavaType baseType = mock(JavaType.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }

      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    String result = typeDeserializerBase.toString();
    assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", result);
  }

  @Test
  public void test_findDeserializer() throws IOException {
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null ,null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }

      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, "typeId");
    assertEquals(null, result);
  }

  @Test
  public void test_findDefaultImplDeserializer() throws IOException {
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null ,null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
    assertEquals(NullifyingDeserializer.instance, result);
  }

  @Test
  public void test_deserializeWithNativeTypeId() throws IOException {
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null ,null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    assertEquals(null, result);
  }

  @Test
  public void test_deserializeWithNativeTypeIdWithType() throws IOException {
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null ,null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, null);
    assertEquals(null, result);
  }

  @Test(expected = IOException.class)
  public void test_handleUnknownTypeId() throws IOException {
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeIdResolver idResolver = mock(TypeIdResolver.class);
    JavaType baseType = mock(JavaType.class);
    TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver ,null, false, java.lang.Object.class) {
      @Override
      public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
      }
      @Override
      public JsonTypeInfo.As getTypeInclusion() {
        return null;
      }
    };
    typeDeserializerBase._handleUnknownTypeId(ctxt, "typeId", idResolver, baseType);
  }
}