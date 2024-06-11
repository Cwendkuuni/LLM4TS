package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsPropertyTypeDeserializerTest extends BaseMapTest {

 private AsPropertyTypeDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 JavaType javaType = mapper.getTypeFactory().constructType(SimpleClass.class);
 deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "typeProperty", false, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"typeProperty\":\"SimpleClassWithTypeId\", \"data\":42}");

 SimpleClass result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(SimpleClassWithTypeId.class, result.getClass());
 assertEquals(42, result.getData());
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"data\":42}");

 SimpleClass result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(SimpleClass.class, result.getClass());
 assertEquals(42, result.getData());
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeIdAndPropertyNotPresent() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"data\":42}");

 try {
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected JacksonException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyNameDifferentThanExpected() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"anotherProperty\":42}");

 try {
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected JacksonException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeIdAndCustomDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClassWithCustomDeserializer.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"typeProperty\":\"SimpleClassWithCustomDeserializer\", \"data\":\"customData\"}");

 SimpleClassWithCustomDeserializer result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(SimpleClassWithCustomDeserializer.class, result.getClass());
 assertEquals("customData", result.getData());
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.SimpleClass\", \"data\":42}");

 SimpleClass result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(SimpleClass.class, result.getClass());
 assertEquals(42, result.getData());
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"data\":[1, 2, 3]}");

 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context, null);
 assertNotNull(result);
 assertInstanceof(List.class, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImplAndTokenBuffer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"data\":[1, 2, 3]}");

 TokenBuffer tb = new TokenBuffer(parser, context);
 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context, tb);
 assertNotNull(result);
 assertInstanceof(List.class, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImplAndNoTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("{\"data\":42}");

 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context, null);
 assertNotNull(result);
 assertEquals(SimpleClass.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("[\"SimpleClass\", 42]");

 SimpleClass result = deserializer.deserializeTypedFromArray(parser, context);
 assertEquals(SimpleClass.class, result.getClass());
 assertEquals(42, result.getData());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(SimpleClass.class);
 JsonParser parser = reader.getParser();
 parser.setValue("42");

 SimpleClass result = deserializer.deserializeTypedFromScalar(parser, context);
 assertEquals(SimpleClass.class, result.getClass());
 assertEquals(42, result.getData());
 }

}

class SimpleClass {
 private int data;

 public int getData() {
 return data;
 }
}

class SimpleClassWithCustomDeserializer {
 private String data;

 public String getData() {
 return data;
 }
}

class SimpleClassWithTypeId extends SimpleClass {
 @JsonTypeInfo(use=As.PROPERTY, property="typeProperty")
 public String getTypePropertyName() {
 return "SimpleClassWithTypeId";
 }
}