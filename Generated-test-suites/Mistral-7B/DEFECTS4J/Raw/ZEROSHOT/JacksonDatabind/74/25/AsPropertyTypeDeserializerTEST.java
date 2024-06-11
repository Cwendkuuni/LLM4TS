package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsPropertyTypeDeserializerTest extends BaseMapTest {

 private static final JavaType INT_TYPE = getClassType(int.class);
 private static final JavaType STRING_TYPE = getClassType(String.class);
 private static final JavaType LIST_INT_TYPE = ObjectMapper.getDefaultInstance().getTypeFactory()
         .constructCollectionType(List.class, INT_TYPE);
 private static final JavaType LIST_STRING_TYPE = ObjectMapper.getDefaultInstance().getTypeFactory()
         .constructCollectionType(List.class, STRING_TYPE);
 private static final JavaType MAP_INT_STRING_TYPE = ObjectMapper.getDefaultInstance().getTypeFactory()
         .constructMapType(Map.class, INT_TYPE, STRING_TYPE);

 @Test
 public void testDeserializeObjectWithPropertyTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "{ \"type\": {\"@class\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{\"name\":\"typePropertyName\", \"typeIdVisible\":true},"
 + "\"baseType\":\"java.lang.String\", \"typePropertyName\":\"typeId\"},"
 + "\"value\":\"testStringValue\" }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Object readValue = mapper.readValue(p, deserializer);
 assertEquals("testStringValue", readValue);
 }

 @Test
 public void testDeserializeObjectWithNativeTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"@type\":\"java.lang.String\"}");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Object readValue = mapper.readValue(p, deserializer);
 assertEquals("java.lang.String", readValue.getClass().getName());
 }

 @Test
 public void testDeserializeObjectWithMissingPropertyTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"value\":\"testStringValue\"}");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 assertNull(deserializer.deserialize(p, mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeObjectWithExtraProperties() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "{ \"type\": {"
 + "\"@class\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{"
 + "\"name\":\"typePropertyName\","
 + "\"typeIdVisible\":true,"
 + "\"typeId\":\"java.lang.String\""
 + "},"
 + "\"baseType\":\"java.lang.String\","
 + "\"typePropertyName\":\"typeId\","
 + "\"value\":\"testStringValue\""
 + "},"
 + "\"extraProperty\":\"testValue\" }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Object readValue = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals("testStringValue", readValue);
 assertEquals(null, readValue.get("extraProperty"));
 }

 @Test
 public void testDeserializeArrayWithTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "[{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{"
 + "\"name\":\"typePropertyName\","
 + "\"typeIdVisible\":true"
 + "},"
 + "\"_class\":\"java.lang.Integer\","
 + "\"value\":42 }]");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer[].class);
 assertEquals(42, deserializer.getValue(deserializer.nextValue()).intValue());
 }

 @Test
 public void testDeserializeArrayWithMissingTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[42]");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer[].class);
 assertNull(deserializer.nextValue());
 }

 @Test
 public void testDeserializeArrayWithExtraProperties() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "[{"
 + "\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{"
 + "\"name\":\"typePropertyName\""
 + "},"
 + "\"_class\":\"java.lang.Integer\","
 + "\"value\":42,"
 + "\"extraProperty\":\"testValue\" }]");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer[].class);
 assertEquals(42, deserializer.getValue(deserializer.nextValue()).intValue());
 assertEquals(null, deserializer.nextValue().get("extraProperty"));
 }

 @Test
 public void testDeserializeMapWithTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "{ \"type\": {"
 + "\"@class\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{"
 + "\"name\":\"typePropertyName\","
 + "\"typeIdVisible\":true"
 + "},"
 + "\"baseType\":\"java.util.Map<java.lang.Integer,java.lang.String>\","
 + "\"typePropertyName\":\"typeId\""
 + "},"
 + "\"value\":{"
 + "\"1\":\"testStringValue1\","
 + "\"2\":\"testStringValue2\""
 + "} }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Map<Integer, String> readValue = deserializer.getValue(deserializer.nextValue());
 assertEquals("testStringValue1", readValue.get(1));
 assertEquals("testStringValue2", readValue.get(2));
 }

 @Test
 public void testDeserializeMapWithMissingTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{ \"1\":\"testStringValue1\", \"2\":\"testStringValue2\" }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 assertNull(deserializer.nextValue());
 }

 @Test
 public void testDeserializeMapWithExtraProperties() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "{ "
 + "\"type\": {"
 + "\"@class\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{"
 + "\"name\":\"typePropertyName\""
 + "},"
 + "\"baseType\":\"java.util.Map<java.lang.Integer,java.lang.String>\","
 + "\"typePropertyName\":\"typeId\""
 + "},"
 + "\"value\":{"
 + "\"1\":\"testStringValue1\","
 + "\"2\":\"testStringValue2\""
 + "},"
 + "\"extraProperty\":\"testValue\" }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Map<Integer, String> readValue = deserializer.getValue(deserializer.nextValue());
 assertEquals("testStringValue1", readValue.get(1));
 assertEquals("testStringValue2", readValue.get(2));
 assertEquals(null, deserializer.nextValue().get("extraProperty"));
 }

 @Test
 public void testDeserializeObjectWithDefaultImpl() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{ \"value\":42 }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Object readValue = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(42, readValue.intValue());
 }

 @Test
 public void testDeserializeArrayWithDefaultImpl() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[42]");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer[].class);
 assertEquals(42, deserializer.getValue(deserializer.nextValue()).intValue());
 }

 @Test
 public void testDeserializeMapWithDefaultImpl() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{ \"value\":{ \"1\":\"testStringValue1\", \"2\":\"testStringValue2\" } }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 Map<Integer, String> readValue = deserializer.getValue(deserializer.nextValue());
 assertEquals("testStringValue1", readValue.get(1));
 assertEquals("testStringValue2", readValue.get(2));
 }

 @Test
 public void testDeserializeObjectWithPropertyTypeIdAndNativeTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser(
 "{ \"type\": {"
 + "\"@class\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\","
 + "\"_inclusion\":\"PROPERTY\","
 + "\"_property\":{"
 + "\"name\":\"typePropertyName\""
 + "},"
 + "\"baseType\":\"java.lang.String\","
 + "\"typePropertyName\":\"typeId\","
 + "\"@type\":\"java.lang.Integer\" },"
 + "\"value\":\"testStringValue\" }");
 ObjectDeserializer deserializer = mapper.readValue(p, AsPropertyTypeDeserializer.class);
 assertEquals(Integer.class, deserializer.getType(mapper.getDeserializationContext()).getRawClass());
 Object readValue = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(Integer.class, readValue.getClass().getComponentType());
 assertEquals(42, ((Number) readValue).intValue());
 }

}