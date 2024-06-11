package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsWrapperTypeDeserializerTest extends BaseTest {

 private static final AsWrapperTypeDeserializer serializer = new AsWrapperTypeDeserializer(
 new SimpleType("testClass"), null, null, false, Object.class);

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 Object result = serializer.deserializeTypedFromObject(createParser("{\"type\":\"testClass\", \"data\":123}"), context);
 assertEquals("123", result.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 Object result = serializer.deserializeTypedFromArray(createParser("[{\"type\":\"testClass\", \"data\":123}]"), context);
 assertEquals("123", result.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 Object result = serializer.deserializeTypedFromScalar(createParser("{\"type\":\"testClass\", \"data\":123}"), context);
 assertEquals("123", result.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 Object result = serializer.deserializeTypedFromAny(createParser("{\"type\":\"testClass\", \"data\":123}"), context);
 assertEquals("123", result.toString());
 }

 @Test(expected = IOException.class)
 public void testInvalidJsonStructure() throws Exception {
 Object result = serializer.deserializeTypedFromObject(createParser("{\"type\":\"testClass\"}"), context);
 fail("Expected IOException: need JSON Object to contain As.WRAPPER_OBJECT type information for class testClass");
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws Exception {
 Object result = serializer.deserializeTypedFromObject(createParser("{\"data\":123}"), context);
 fail("Expected IOException: need JSON String that contains type id (for subtype of testClass)");
 }

 @Test(expected = IOException.class)
 public void testMissingEndObject() throws Exception {
 Object result = serializer.deserializeTypedFromObject(createParser("{\"type\":\"testClass\", \"data\":123}"), context);
 createParser("{\"type\":\"testClass\", \"data\":123}").nextToken(); // consume closing }
 fail("Expected IOException: expected closing END_OBJECT after type information and deserialized value");
 }

 @Test(expected = ClassCastException.class)
 public void testDeserializeWithInvalidTypeId() throws Exception {
 Object result = serializer.deserializeTypedFromObject(createParser("{\"type\":\"invalidType\", \"data\":123}"), context);
 fail("Expected ClassCastException: deserializer for type [B, class com.example.invalidType] not found");
 }

 private JsonParser createParser(String json) throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getFactory().createParser(json);
 }

}