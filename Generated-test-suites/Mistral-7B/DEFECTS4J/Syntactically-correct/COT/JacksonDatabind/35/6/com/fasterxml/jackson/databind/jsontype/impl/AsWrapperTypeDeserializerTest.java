package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = MAPPER.getFactory().createParser("{\"class\":\"com.test.MyClass\", \"value\":42}");
 Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());
 assertInstanceOf(MyClass.class, result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 Object[] result = deserializer.deserializeTypedFromArray(MAPPER.getFactory().createParser("[{\"class\":\"com.test.MyClass\", \"value\":42}]"), new TestContext());
 assertArrayEquals(new Object[]{MyClass.class, 42}, result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = MAPPER.getFactory().createParser("42");
 Object result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
 assertSame(Integer.TYPE, result.getClass());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 Object result = deserializer.deserializeTypedFromAny(MAPPER.getFactory().createParser("{\"value\":42}"), new TestContext());
 assertSame(Integer.TYPE, result.getClass());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = MAPPER.getFactory().createParser("{\"class\":\"com.test.MyClass\", \"value\":42}");
 Object result = deserializer._deserializeWithNativeTypeId(parser, new TestContext(), "com.test.MyClass");
 assertInstanceOf(MyClass.class, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = MAPPER.getFactory().createParser("{\"class\":\"InvalidClass\", \"value\":42}");
 deserializer._deserializeWithNativeTypeId(parser, new TestContext(), "com.test.MyClass");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = MAPPER.getFactory().createParser("{\"value\":42}");
 deserializer._deserialize(parser, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingEndObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = MAPPER.getFactory().createParser("{\"class\":\"com.test.MyClass\", \"value\":42}");
 deserializer._deserialize(parser, new TestContext());
 parser.nextToken(); // consume the "value"
 // this should fail with a wrong token exception
 deserializer._deserialize(parser, new TestContext());
 }
}