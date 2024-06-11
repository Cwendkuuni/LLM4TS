package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;
import org.mockito.Mockito;

@JacksonStdImpl
public class EnumSerializerTest {
  
    @Test
    public void testSerializeAsIndex() throws IOException {
      // Setup
      Class<Enum<?>> enumClass = Enum.class;
      SerializationConfig config = mock(SerializationConfig.class);
      BeanDescription beanDesc = mock(BeanDescription.class);
      JsonFormat.Value format = mock(JsonFormat.Value.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.getEnumClass()).thenReturn(enumClass);
      
      when(EnumValues.constructFromName(config, enumClass)).thenReturn(v);
      when(format.getShape()).thenReturn(Shape.ARRAY);
      
      // Test
      EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
      
      // Verify
      assertNotNull(serializer);
      assertTrue(serializer._serializeAsIndex);
    }
    
    @Test
    public void testSerializeAsString() throws IOException {
      // Setup
      Class<Enum<?>> enumClass = Enum.class;
      SerializationConfig config = mock(SerializationConfig.class);
      BeanDescription beanDesc = mock(BeanDescription.class);
      JsonFormat.Value format = mock(JsonFormat.Value.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.getEnumClass()).thenReturn(enumClass);
      
      when(EnumValues.constructFromName(config, enumClass)).thenReturn(v);
      when(format.getShape()).thenReturn(Shape.STRING);
      
      // Test
      EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
      
      // Verify
      assertNotNull(serializer);
      assertFalse(serializer._serializeAsIndex);
    }
    
    @Test
    public void testSerializeWithIndex() throws IOException {
      // Setup
      Class<Enum<?>> enumClass = Enum.class;
      SerializationConfig config = mock(SerializationConfig.class);
      BeanDescription beanDesc = mock(BeanDescription.class);
      JsonFormat.Value format = mock(JsonFormat.Value.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.getEnumClass()).thenReturn(enumClass);
      
      when(EnumValues.constructFromName(config, enumClass)).thenReturn(v);
      when(format.getShape()).thenReturn(Shape.AS_ARRAY);
      
      // Test
      EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
      
      // Verify
      assertNotNull(serializer);
      assertTrue(serializer._serializeAsIndex);
    }
    
    @Test
    public void testNoFormatAnnotation() throws IOException {
      // Setup
      Class<Enum<?>> enumClass = Enum.class;
      SerializationConfig config = mock(SerializationConfig.class);
      BeanDescription beanDesc = mock(BeanDescription.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.getEnumClass()).thenReturn(enumClass);
      
      when(EnumValues.constructFromName(config, enumClass)).thenReturn(v);
      
      // Test
      EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, null);
      
      // Verify
      assertNotNull(serializer);
      assertFalse(serializer._serializeAsIndex);
    }
    
    @Test
    public void testContextualSerializer() throws JsonMappingException {
      // Setup
      SerializerProvider serializers = mock(SerializerProvider.class);
      BeanProperty property = mock(BeanProperty.class);
      ContextualSerializer contextualSerializer = mock(ContextualSerializer.class);
      
      EnumValues v = mock(EnumValues.class);
      Boolean serializeAsIndex = true;
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      when(property.getType()).thenReturn(mock(JavaType.class));
      when(contextualSerializer.createContextual(serializers, property)).thenReturn(serializer);
      
      // Test
      JsonSerializer<?> result = serializer.createContextual(serializers, property);
      
      // Verify
      assertSame(result, serializer);
    }
    
    @Test
    public void testSerializeWithOrdinal() throws IOException {
      // Setup
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      SerializerProvider serializers = mock(SerializerProvider.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.serializedValueFor(en)).thenReturn("serializedValue");
      
      Boolean serializeAsIndex = true;
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      
      // Test
      serializer.serialize(en, gen, serializers);
      
      // Verify
      verify(gen).writeNumber(en.ordinal());
    }
    
    @Test
    public void testSerializeWithToString() throws IOException {
      // Setup
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      SerializerProvider serializers = mock(SerializerProvider.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.serializedValueFor(en)).thenReturn("serializedValue");
      
      Boolean serializeAsIndex = false;
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
      
      // Test
      serializer.serialize(en, gen, serializers);
      
      // Verify
      verify(gen).writeString(en.toString());
    }
    
    @Test
    public void testSerializeWithSerializedValue() throws IOException {
      // Setup
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      SerializerProvider serializers = mock(SerializerProvider.class);
      
      EnumValues v = mock(EnumValues.class);
      when(v.serializedValueFor(en)).thenReturn("serializedValue");
      
      Boolean serializeAsIndex = false;
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
      
      // Test
      serializer.serialize(en, gen, serializers);
      
      // Verify
      verify(gen).writeString("serializedValue");
    }
    
    @Test
    public void testGetSchemaWhenIndexed() {
      // Setup
      SerializerProvider provider = mock(SerializerProvider.class);
      Type typeHint = mock(Type.class);
      
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      Boolean serializeAsIndex = true;
      EnumValues v = mock(EnumValues.class);
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      
      // Test
      JsonNode result = serializer.getSchema(provider, typeHint);
      
      // Verify
      assertTrue(result.isObject());
      assertTrue(result.has("type"));
      assertEquals("integer", result.get("type").asText());
    }
    
    @Test
    public void testGetSchemaWhenNotIndexed() {
      // Setup
      SerializerProvider provider = mock(SerializerProvider.class);
      Type typeHint = mock(Type.class);
      
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      Boolean serializeAsIndex = false;
      EnumValues v = mock(EnumValues.class);
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      
      // Test
      JsonNode result = serializer.getSchema(provider, typeHint);
      
      // Verify
      assertTrue(result.isObject());
      assertTrue(result.has("type"));
      assertEquals("string", result.get("type").asText());
      assertTrue(result.has("enum"));
    }
    
    @Test
    public void testSchemaVisit() throws JsonMappingException {
      // Setup
      JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
      JavaType typeHint = mock(JavaType.class);
      JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
      
      Enum<?> en = mock(Enum.class);
      Boolean serializeAsIndex = false;
      EnumValues v = mock(EnumValues.class);
      
      EnumSerializer serializer = new EnumSerializer(v, serializeAsIndex);
      
      when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);
      when(v.enums()).thenReturn(new ArrayList<Enum<?>>());
      
      // Test
      serializer.acceptJsonFormatVisitor(visitor, typeHint);
      
      // Verify
      verify(stringVisitor).enumTypes(anySet());
    }

}