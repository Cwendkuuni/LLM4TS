package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 AsWrapperTypeDeserializer.Factory factory = new AsWrapperTypeDeserializer.Factory((JavaType) IntType.INSTANCE,
 new TypeIdResolver() {
 @Override
 public IdType idFromValueAndType(Object value, Type serializableType) {
 return new IdType();
 }
 }, "typeProperty", true, Integer.class);
 asWrapperTypeDeserializer = factory.create(objectMapper.getFactory().findValueType(IntType.class));
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 // Given
 String json = "{\"type\":\"java.lang.Integer\",\"value\":42}";
 JsonParser jsonParser = createParser(json);
 // When
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 // Then
 assertEquals(42, actual);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 // Given
 String json = "[{\"type\":\"java.lang.Integer\",\"value\":42}]";
 JsonParser jsonParser = createParser(json);
 // When
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, context);
 // Then
 assertEquals(42, actual);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 // Given
 String json = "42";
 JsonParser jsonParser = createParser(json);
 // When
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, context);
 // Then
 assertEquals(42, actual);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 // Given
 String json = "{\"type\":\"java.lang.String\",\"value\":\"fourty two\"}";
 JsonParser jsonParser = createParser(json);
 // When
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, context);
 // Then
 assertEquals("fourty two", actual);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 // Given
 String json = "{\"type\":\"java.lang.Integer\"}";
 JsonParser jsonParser = createParser(json);
 // When
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 // Then
 assertEquals(Integer.TYPE, actual.getClass());
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithoutStartObject() throws IOException {
 // Given
 String json = "42";
 JsonParser jsonParser = createParser(json);
 // When, Then
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithoutFieldName() throws IOException {
 // Given
 String json = "{\"type\":\"java.lang.Integer\",\"value\":42}";
 JsonParser jsonParser = createParser(json);
 jsonParser.nextToken(); // consume START_OBJECT
 // When, Then
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTypeId() throws IOException {
 // Given
 String json = "{\"type\":\"java.util.Date\",\"value\":42}";
 JsonParser jsonParser = createParser(json);
 // When, Then
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 }

 @Test
 public void testDeserializeMergingTypeId() throws IOException {
 // Given
 String json = "{\"type\":\"java.lang.Integer\",\"value\":42,\"typeProperty\":{\"type\":\"java.lang.String\",\"value\":\"StringType\"}}";
 JsonParser jsonParser = createParser(json);
 // When
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 // Then
 assertEquals(42, actual);
 // And
 assertEquals("StringType", actual.getClass().getDeclaredField("typeProperty").get(actual).toString());
 }
}