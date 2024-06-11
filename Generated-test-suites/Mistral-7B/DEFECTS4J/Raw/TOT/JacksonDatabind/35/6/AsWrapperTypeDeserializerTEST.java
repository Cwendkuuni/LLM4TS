package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

 private final static Class<AsWrapperTypeDeserializer> TYPE_DESERIALIZER_CLASS = AsWrapperTypeDeserializer.class;
 private final static JavaType TYPE_DESERIALIZER_TYPE = TypeFactory.defaultInstance().constructType(TYPE_DESERIALIZER_CLASS);

 @Test
 public void testDeserializingWrapperObjectWithValidTypeId() throws IOException {
 final ObjectMapper mapper = new ObjectMapper();

 final TypeIdResolver idResolver = mapper.getTypeIdResolver();
 final AsWrapperTypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(TYPE_DESERIALIZER_TYPE, idResolver, "_typeProperty", true, String.class);

 final String jsonString = "{\"typeId\":\"java.lang.String\",\"value\":\"Hello World\"}";
 final Object actual = mapper.readValue(jsonString, typeDeserializer.getType());
 final String expected = "Hello World";

 assertEquals("Expected String value: ", expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializingWrapperObjectWithoutTypeId() throws IOException {
 final ObjectMapper mapper = new ObjectMapper();

 final TypeIdResolver idResolver = mapper.getTypeIdResolver();
 final AsWrapperTypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(TYPE_DESERIALIZER_TYPE, idResolver, "_typeProperty", true, String.class);

 final String jsonString = "{\"value\":\"Hello World\"}";
 mapper.readValue(jsonString, typeDeserializer.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializingWrapperObjectWithInvalidTypeId() throws IOException {
 final ObjectMapper mapper = new ObjectMapper();

 final TypeIdResolver idResolver = mapper.getTypeIdResolver();
 final AsWrapperTypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(TYPE_DESERIALIZER_TYPE, idResolver, "_typeProperty", true, String.class);

 final String jsonString = "{\"typeId\":\"java.util.List\",\"value\":\"Hello World\"}";
 mapper.readValue(jsonString, typeDeserializer.getType());
 }

 @Test
 public void testDeserializingWrapperObjectWithTypeIdAndTypeIdVisible() throws IOException {
 final ObjectMapper mapper = new ObjectMapper();

 final TypeIdResolver idResolver = mapper.getTypeIdResolver();
 final AsWrapperTypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(TYPE_DESERIALIZER_TYPE, idResolver, "_typeProperty", true, String.class);

 final String jsonString = "{\"typeId\":{\"typeId\":\"java.lang.String\",\"value\":\"Hello World\"},\"value\":\"Hello World\"}";
 final Object actual = mapper.readValue(jsonString, typeDeserializer.getType());
 final String expected = "Hello World";

 assertEquals("Expected String value: ", expected, actual);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializingWrapperObjectWithMissingEndObject() throws IOException {
 final ObjectMapper mapper = new ObjectMapper();

 final TypeIdResolver idResolver = mapper.getTypeIdResolver();
 final AsWrapperTypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(TYPE_DESERIALIZER_TYPE, idResolver, "_typeProperty", true, String.class);

 final String jsonString = "{\"typeId\":\"java.lang.String\",\"value\":\"Hello World\"";
 mapper.readValue(jsonString, typeDeserializer.getType());
 }
}