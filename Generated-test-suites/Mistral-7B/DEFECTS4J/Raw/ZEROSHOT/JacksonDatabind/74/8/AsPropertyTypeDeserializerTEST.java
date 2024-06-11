package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsPropertyTypeDeserializerTest {

 private static final String TYPE_PROPERTY_NAME = "type";
 private static final String TYPE_ID = "MyType";
 private static final String JSON_WITH_TYPE_PROPERTY = "{\"type\":\"" + TYPE_ID + "\",\"value\":123}";
 private static final String JSON_WITH_FIELD_NAME = "{\"fieldName\":\"value\",\"type\":\"MyType\"}";
 private static final String JSON_WITH_ARRAY_WRAPPER = "[{\"type\":\"MyType\",\"value\":123}]";
 private static final String JSON_WITH_NATURAL_VALUE = "{\"value\":123}";

 private ObjectMapper objectMapper;
 private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

 @BeforeAll
 void setUp() throws IOException {
 objectMapper = new ObjectMapper();
 JavaType javaType = objectMapper.getTypeFactory().constructType(Object.class);
 TypeIdResolver typeIdResolver = objectMapper.getTypeFactory();
 asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, TYPE_PROPERTY_NAME, true, null, As.PROPERTY);
 }

 @Test
 void testDeserializeTypedFromObjectWithTypeId() throws IOException {
 JsonParser jp = objectMapper.getFactory().createParser(JSON_WITH_TYPE_PROPERTY);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jp, objectMapper.getDeserializationContext());
 assertInstanceOf(String.class, result);
 assertEquals(TYPE_ID, result.toString());
 }

 @Test
 void testDeserializeTypedFromObjectWithTypePropertyName() throws IOException {
 JsonParser jp = objectMapper.getFactory().createParser(JSON_WITH_FIELD_NAME);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jp, objectMapper.getDeserializationContext());
 assertInstanceOf(String.class, result);
 assertEquals(TYPE_ID, result.toString());
 }

 @Test
 void testDeserializeTypedUsingDefaultImpl() throws IOException {
 JsonParser jp = objectMapper.getFactory().createParser(JSON_WITH_NATURAL_VALUE);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jp, objectMapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 void testDeserializeTypedWithArrayWrapper() throws IOException {
 JsonParser jp = objectMapper.getFactory().createParser(JSON_WITH_ARRAY_WRAPPER);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jp, objectMapper.getDeserializationContext());
 assertInstanceOf(List.class, result);
 assertEquals(1, ((List) result).size());
 assertInstanceOf(String.class, ((List) result).get(0));
 assertEquals(TYPE_ID, ((Map) ((List) result).get(0)).get("type").toString());
 }

 @Test
 void testDeserializeTypedFromAnyWithTypeId() throws IOException {
 JsonParser jp = objectMapper.getFactory().createParser(JSON_WITH_TYPE_PROPERTY);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jp, objectMapper.getDeserializationContext());
 assertInstanceOf(String.class, result);
 assertEquals(TYPE_ID, result.toString());
 }

 @Test
 void testDeserializeTypedFromAnyWithNaturalValue() throws IOException {
 JsonParser jp = objectMapper.getFactory().createParser(JSON_WITH_NATURAL_VALUE);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jp, objectMapper.getDeserializationContext());
 assertNull(result);
 }
}