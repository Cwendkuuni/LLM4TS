package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testTypeDeserializerBase() throws Exception {
 // Test for construction
 TypeDeserializerBase deserializerBase = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String.class),
 new MapTypeIdResolver(),
 "typePropertyName",
 true,
 String.class
 );
 assertNotNull(deserializerBase);
 assertEquals("TypeDeserializerBase", deserializerBase.getClass().getName());
 assertEquals("String", deserializerBase.baseTypeName());
 assertEquals("typePropertyName", deserializerBase.getPropertyName());
 // Test for forProperty
 BeanProperty property = new BeanProperty("propertyName", String.class);
 TypeDeserializer forProperty = deserializerBase.forProperty(property);
 assertEquals("TypeDeserializerBase", forProperty.getClass().getName());
 assertEquals(deserializerBase, forProperty.getTypeDeserializer());
 assertEquals("String", forProperty.getBaseType().getRawClass().getName());
 // Test for getTypeInclusion
 assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, deserializerBase.getTypeInclusion());
 // Test for findDeserializer
 JavaType javaType = MAPPER.getTypeFactory().constructType(Map.class);
 JsonDeserializer<Object> deserializer = deserializerBase.findDeserializer(
 MAPPER.getDeserializationContext(),
 MapperFactory.getDefaultInstance().constructType(javaType.getComponentType(0)).getName()
 );
 assertEquals(Map.class, deserializer.getValueType());
 // Test for findDefaultImplDeserializer
 deserializerBase = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String[].class),
 new MapTypeIdResolver(),
 "typePropertyName",
 true,
 String[].class
 );
 deserializerBase.setDefaultImplDeserializer(
 MAPPER.getDeserializationContext().findValueDeserializer(String[].class)
 );
 JsonDeserializer<Object> defaultImplDeserializer = deserializerBase.findDefaultImplDeserializer(MAPper.getDeserializationContext());
 assertEquals(String[].class, defaultImplDeserializer.getValueType());
 // Test for deserializeWithNativeTypeId
 String testJson = "{\"typeId\":\"java.util.Map\",\"value\":{\"key\":\"testKey\",\"value\":\"testValue\"}}";
 Object readValue = deserializerBase.deserializeWithNativeTypeId(
 MAPPER.getFactory().createParser(new ByteArrayInputStream(testJson.getBytes())),
 MAPPER.getDeserializationContext()
 );
 assertTrue(readValue instanceof Map);
 Map map = (Map) readValue;
 assertEquals("testKey", map.get("key"));
 assertEquals("testValue", map.get("value"));
 // Test for handleUnknownTypeId
 deserializerBase = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String.class),
 new MapTypeIdResolver(),
 "typePropertyName",
 true,
 Object.class
 );
 try {
 deserializerBase.handleUnknownTypeId(MAPper.getDeserializationContext(), null, null, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 try {
 deserializerBase.handleUnknownTypeId(MAPper.getDeserializationContext(), "unknownTypeId", null, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 try {
 deserializerBase.handleUnknownTypeId(MAPper.getDeserializationContext(), "java.util.Map", null, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 try {
 deserializerBase.handleUnknownTypeId(MAPper.getDeserializationContext(), "java.util.Map", new MapTypeIdResolver(), null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 try {
 deserializerBase.handleUnknownTypeId(MAPper.getDeserializationContext(), "java.util.Map", null, MapperFactory.getDefaultInstance().constructType(String.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 /**
 * Test class for MapTypeIdResolver.
 */
 static class MapTypeIdResolver extends TypeIdResolverBase {
 @Override
 public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
 if (typeId.equals("java.util.Map")) {
 return MAPper.getDefaultInstance().constructType(Map.class);
 }
 return null;
 }
 }
}