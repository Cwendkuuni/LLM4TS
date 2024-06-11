package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

   @Test
   public void testDeserializeTypedFromObject() throws IOException {
      // Arrange
      AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
      JsonParser p = null;
      DeserializationContext ctxt = null;
      
      // Act
      Object result = deserializer.deserializeTypedFromObject(p, ctxt);
      
      // Assert
      // Add assertions here
   }

   @Test
   public void test_deserializeTypedForId() throws IOException {
      // Arrange
      AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
      JsonParser p = null;
      DeserializationContext ctxt = null;
      TokenBuffer tb = null;
      
      // Act
      Object result = deserializer._deserializeTypedForId(p, ctxt, tb);
      
      // Assert
      // Add assertions here
   }

   @Test
   public void test_deserializeTypedUsingDefaultImpl() throws IOException {
      // Arrange
      AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
      JsonParser p = null;
      DeserializationContext ctxt = null;
      TokenBuffer tb = null;
      
      // Act
      Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
      
      // Assert
      // Add assertions here
   }

   @Test
   public void test_deserializeTypedFromAny() throws IOException {
      // Arrange
      AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
      JsonParser p = null;
      DeserializationContext ctxt = null;
      
      // Act
      Object result = deserializer.deserializeTypedFromAny(p, ctxt);
      
      // Assert
      // Add assertions here
   }
}