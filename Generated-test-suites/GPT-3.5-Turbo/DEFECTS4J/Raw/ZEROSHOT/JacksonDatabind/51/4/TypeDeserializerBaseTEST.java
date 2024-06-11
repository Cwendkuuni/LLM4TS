package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import junit.framework.TestCase;

public class TypeDeserializerBaseTest extends TestCase {

   public void testForProperty() {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertNull(typeDeserializerBase.forProperty(null));
   }

   public void testGetPropertyName() {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertEquals("", typeDeserializerBase.getPropertyName());
   }

   public void testGetTypeIdResolver() {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertNull(typeDeserializerBase.getTypeIdResolver());
   }
   
   public void testGetDefaultImpl() {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertNull(typeDeserializerBase.getDefaultImpl());   
   }

   public void testToString() {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", typeDeserializerBase.toString());
   }
   
   public void testDeserializeWithNativeTypeId() throws IOException {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, null));
   }
   
   public void testDeserializeWithNativeTypeIdWithTypeid() throws IOException {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, null, null));
   }
   
   public void testHandleUnknownTypeId() throws IOException {
       TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
           @Override
           public TypeDeserializer forProperty(BeanProperty prop) {
               return null;
           }
           @Override
           public JsonTypeInfo.As getTypeInclusion() {
               return null;
           }
       };
       assertNull(typeDeserializerBase._handleUnknownTypeId(null, null, null, null));
   }

}