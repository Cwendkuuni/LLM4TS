package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private static final Class<AsPropertyTypeDeserializer> type = AsPropertyTypeDeserializer.class;
 private static final AsPropertyTypeDeserializer serializer = new AsPropertyTypeDeserializer(
 null, null, "type", true, null, As.PROPERTY);

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeTypedFromObject() throws Exception {
 JsonParser p = createParser("{\"type\":\"String\",\"data\":\"test\"}");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals("test", result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 JsonParser p = createParser("{\"type\":\"String\",\"_class\":\"java.lang.String\"}");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals("java.lang.String", result.getClass().getName());
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithMissingProperty() throws Exception {
 JsonParser p = createParser("{\"data\":\"test\"}");
 try {
 serializer.deserializeTypedFromObject(p, context());
 fail("Expected WrongTokenException");
 } catch (JsonProcessingException e) {
 assertEquals("missing property 'type' that is to contain type id  (for class com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer)", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithMissingPropertyAndDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, null, "type", true, String.class, As.PROPERTY);
 JsonParser p = createParser("{\"data\":\"test\"}");
 Object result = deserializer.deserializeTypedFromObject(p, context());
 assertEquals("test", result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithTypeIdVisible() throws Exception {
 JsonParser p = createParser("{\"type\":{\"class\":\"java.lang.String\",\"type\":\"String\"},\"data\":\"test\"}");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals("test", result);
 assertEquals("java.lang.String", result.getClass().getName());
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithTypeIdVisibleAndDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, null, "type", true, String.class, As.PROPERTY);
 JsonParser p = createParser("{\"data\":\"test\"}");
 Object result = deserializer.deserializeTypedFromObject(p, context());
 assertEquals("test", result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithPropertyNameMismatch() throws Exception {
 JsonParser p = createParser("{\"typeProperty\":\"String\",\"data\":\"test\"}");
 try {
 serializer.deserializeTypedFromObject(p, context());
 fail("Expected WrongTokenException");
 } catch (JsonProcessingException e) {
 assertEquals("missing property 'type' that is to contain type id  (for class com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer)", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithTypeIdNotVisible() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, null, "type", false, String.class, As.PROPERTY);
 JsonParser p = createParser("{\"type\":\"String\",\"data\":\"test\"}");
 Object result = deserializer.deserializeTypedFromObject(p, context());
 assertEquals("test", result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithArrayWrapper() throws Exception {
 JsonParser p = createParser("[\"String\",{\"data\":\"test\"}]");
 Object result = serializer.deserializeTypedFromAny(p, context());
 assertEquals("test", result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithEmptyArrayWrapper() throws Exception {
 JsonParser p = createParser("[\"String\"]");
 Object result = serializer.deserializeTypedFromAny(p, context());
 assertNull(result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithNullValue() throws Exception {
 JsonParser p = createParser("null");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertNull(result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithBooleanValue() throws Exception {
 JsonParser p = createParser("true");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertTrue(result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithIntValue() throws Exception {
 JsonParser p = createParser("42");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals(42, result.intValue());
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithDoubleValue() throws Exception {
 JsonParser p = createParser("3.14159265359");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals(3.14159265359, result.doubleValue(), 0.000001);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithStringValue() throws Exception {
 JsonParser p = createParser("\"test\"");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals("test", result);
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDeserializeWithComplexStructure() throws Exception {
 JsonParser p = createParser("{" +
 "\"type\":{\"class\":\"java.lang.String\",\"type\":\"String\"}," +
 "\"data\":{" +
 "\"name\":\"John Doe\"," +
 "\"age\":42," +
 "\"address\":{" +
 "\"street\":\"123 Main St\"," +
 "\"city\":\"Anytown\"," +
 "\"state\":\"NY\"," +
 "\"zip\":\"12345\"" +
 "}}" +
 "}");
 Object result = serializer.deserializeTypedFromObject(p, context());
 assertEquals("John Doe", result.toString());
 }

}