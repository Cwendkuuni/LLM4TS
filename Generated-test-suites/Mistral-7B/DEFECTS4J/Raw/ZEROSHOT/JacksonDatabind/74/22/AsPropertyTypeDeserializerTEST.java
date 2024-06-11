package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer DESERIALIZER = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String[].class), MAPPER.getTypeIdResolver(), "type", false,
 MAPPER.getTypeFactory().constructType(Integer.class), As.PROPERTY);

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer DESERIALIZER_WITH_NAME = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String[].class), MAPPER.getTypeIdResolver(), "type", false,
 MAPPER.getTypeFactory().constructType(Integer.class), As.PROPERTY, "myTypeProperty");

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer DESERIALIZER_WITH_DEFAULT = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String[].class), MAPPER.getTypeIdResolver(), "type", false,
 MAPPER.getTypeFactory().constructType(MyDefaultImpl.class), As.PROPERTY);

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer DESERIALIZER_WITH_TYPEIDVISIBLE = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String[].class), MAPPER.getTypeIdResolver(), "type", true,
 MAPPER.getTypeFactory().constructType(Integer.class), As.PROPERTY);

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer DESERIALIZER_WITH_TYPEIDINVISIBLE = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String[].class), MAPPER.getTypeIdResolver(), "type", false,
 MAPPER.getTypeFactory().constructType(Integer.class), As.PROPERTY);

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"type\": \"java.lang.Integer\", \"data\": 42}");
 Object result = DESERIALIZER.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyName() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"myTypeProperty\": \"java.lang.Integer\", \"data\": 42}");
 Object result = DESERIALIZER_WITH_NAME.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTokenBuffer() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"type\": \"java.lang.Integer\", \"data\": 42}");
 TokenBuffer tb = new TokenBuffer(jp, MAPPER.getDeserializationContext());
 Object result = DESERIALIZER.deserializeTypedForId(jp, MAPPER.getDeserializationContext(), tb);
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"type\": null}");
 Object result = DESERIALIZER_WITH_DEFAULT.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeIdVisible() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"type\": \"java.lang.Integer\", \"data\": 42}");
 Object result = DESERIALIZER_WITH_TYPEIDVISIBLE.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertEquals("java.lang.Integer", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeIdInvisible() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"data\": 42}");
 Object result = DESERIALIZER_WITH_TYPEIDINVISIBLE.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertEquals(Integer.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromObjectWithWrongType() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"type\": \"java.lang.String\", \"data\": 42}");
 Object result = DESERIALIZER.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithEmptyJson() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{}");
 Object result = DESERIALIZER.deserializeTypedFromObject(jp, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("[1, 2, 3]");
 Object result = DESERIALIZER.deserializeTypedFromArray(jp, MAPPER.getDeserializationContext());
 assertEquals(Arrays.asList(1, 2, 3), result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("42");
 Object result = DESERIALIZER.deserializeTypedFromScalar(jp, MAPPER.getDeserializationContext());
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("[1, 2, 3]");
 Object result = DESERIALIZER.deserializeTypedFromAny(jp, MAPPER.getDeserializationContext());
 assertEquals(Arrays.asList(1, 2, 3), result);
 }

 @Test
 public void testDeserializeTypedFromAnyWithNull() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("null");
 Object result = DESERIALIZER.deserializeTypedFromAny(jp, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromAnyWithWrongType() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{\"type\": \"java.lang.String\", \"data\": \"abc\"}");
 Object result = DESERIALIZER.deserializeTypedFromAny(jp, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromAnyWithEmptyJson() throws IOException {
 JsonParser jp = MAPPER.getFactory().createParser("{}");
 Object result = DESERIALIZER.deserializeTypedFromAny(jp, MAPPER.getDeserializationContext());
 assertNull(result);
 }
}