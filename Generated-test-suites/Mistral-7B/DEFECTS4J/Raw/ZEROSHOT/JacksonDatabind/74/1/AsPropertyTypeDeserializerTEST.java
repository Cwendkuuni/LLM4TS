package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class AsPropertyTypeDeserializerTest extends ObjectMapperTestBase {

 private static final String AS_PROPERTY_JSON = "{\"type\":\"AsPropertyType\",\"typePropertyName\":\"myType\",\"inclusion\":\"PROPERTY\",\"defaultImpl\":{\"className\":\"com.TestClass\",\"propertyName\":\"myProperty\"}}";

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws IOException {
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON);
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(p, context);
 assertTrue(result instanceof Map);
 assertEquals("{\"myType\":{\"myProperty\":1}}", mapper.writeValueAsString(result));
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON + ",\"typeId\":\"MyTypeId\"");
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(p, context);
 assertTrue(result instanceof MyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectWithInvalidPropertyName() throws IOException {
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON + ",\"typePropertyName\":\"invalidPropertyName\"");
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 deserializer.deserializeTypedFromObject(p, context);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, getTypeFactory().constructType(DefaultImplClass.class), As.PROPERTY);
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON);
 Object result = deserializer.deserializeTypedFromObject(p, context);
 assertTrue(result instanceof DefaultImplClass);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[{\"type\":\"AsPropertyType\",\"typePropertyName\":\"myType\",\"inclusion\":\"PROPERTY\",\"defaultImpl\":{\"className\":\"com.TestClass\",\"propertyName\":\"myProperty\"}}]");
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object[].class),
 getTypeIdResolver(), null, false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromAny(p, context);
 assertTrue(result.getClass().isArray());
 assertEquals(1, result.length);
 assertTrue(result[0] instanceof Map);
 assertEquals("{\"myType\":{\"myProperty\":1}}", mapper.writeValueAsString(result[0]));
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 JsonParser p = mapper.getFactory().createParser("123");
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Integer.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromScalar(p, context);
 assertEquals(123, result);
 }

 @Test
 public void testDeserializeTypedFromInvalidJson() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"invalidJson\":\"123\"}");
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Integer.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 try {
 deserializer.deserializeTypedFromObject(p, context);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeWithNullTokenBuffer() throws IOException {
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON);
 Object result = deserializer.deserializeTypedFromObject(p, context);
 assertTrue(result instanceof Map);
 }

 @Test
 public void testDeserializeWithTokenBuffer() throws IOException {
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON);
 TokenBuffer tb = new TokenBuffer(p, context);
 Object result = deserializer.deserializeTypedFromObject(p, context);
 ObjectMapper mapper2 = new ObjectMapper();
 ObjectReader reader = mapper2.readerForType(new TypeReference<Object>() {});
 Object readValue = reader.readValue(p);
 assertEquals(readValue, result);
 }

 @Test
 public void testDeserializeWithTokenBufferAndDeserialize() throws IOException {
 ObjectDeserializer deserializer = new AsPropertyTypeDeserializer(getTypeFactory().constructType(Object.class),
 getTypeIdResolver(), "myType", false, null, As.PROPERTY);
 JsonParser p = mapper.getFactory().createParser(AS_PROPERTY_JSON);
 TokenBuffer tb = new TokenBuffer(p, context);
 Object result = deserializer.deserializeTypedFromObject(p, context);
 ObjectMapper mapper2 = new ObjectMapper();
 ObjectReader reader = mapper2.readerForType(new TypeReference<Object>() {});
 Object deserialized = reader.readValue(tb);
 assertEquals(result, deserialized);
 }
}

class DefaultImplClass {
 // empty
}