package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserialize() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext());
 Assert.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext());
 Assert.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext());
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("null");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext());
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullElement() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\", null, \"ghi\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext());
 Assert.assertArrayEquals(new String[]{"abc", null, "ghi"}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper.DefaultTyping typeFinder = objectMapper.getTypeFactory();
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"\"");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext());
 Object[] expected = new Object[0];
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
 deserializer.deserialize(jsonParser, new DeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"abc\"");
 deserializer.deserialize(jsonParser, new DeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeUnexpectedToken() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"key\": \"value\" }");
 deserializer.deserialize(jsonParser, new DeserializationContext());
 }

 @Test
 public void testDeserializeCustomType() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
 TypeReference<String[]> typeRef = new TypeReference<String[]>() {};
 String[] result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(), null);
 Assert.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
 TypeDeserializer typeDeserializer = new SimpleTypeDeserializer();
 String[] result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
 Assert.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }
}

 class SimpleTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ctxt.constructValue(new TypeReference<String[]>() {}.getType(), p);
 }
}