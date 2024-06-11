package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyArray;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

 private ObjectMapper mapper;
 private JsonFactory factory;
 private StringArrayDeserializer deserializer;
 private JsonDeserializer<String> elementDeserializer;
 private TypeDeserializer emptyArray;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 factory = mapper.getFactory();
 deserializer = StringArrayDeserializer.instance;
 elementDeserializer = mapper.findValueDeserializer(String.class);
 emptyArray = mapper.getTypeFactory().constructValueTypeDeserializer(new EmptyArray(), String[].class);
 }

 // Test for deserialize method with default deserializer
 @Test
 public void testDeserializeWithDefaultDeserializer() throws Exception {
 String json = "[\"hello\", \"world\"]";
 JsonParser jp = factory.createParser(json);
 String[] result = deserializer.deserialize(jp, new DeserializationContext(mapper, new Object(), null));
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 // Test for deserialize method with custom element deserializer
 @Test
 public void testDeserializeWithCustomElementDeserializer() throws Exception {
 JsonDeserializer<String> customElementDeserializer = mapper.findValueDeserializer(String.class);
 deserializer = new StringArrayDeserializer(customElementDeserializer);
 String json = "[\"hello\", \"world\"]";
 JsonParser jp = factory.createParser(json);
 String[] result = deserializer.deserialize(jp, new DeserializationContext(mapper, new Object(), null));
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 // Test for deserialize method when JsonParser is not at START_ARRAY token
 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidToken() throws Exception {
 String json = "{\"name\": \"John\", \"age\": 30}";
 JsonParser jp = factory.createParser(json);
 deserializer.deserialize(jp, new DeserializationContext(mapper, new Object(), null));
 }

 // Test for deserialize method when JsonParser encounters VALUE_NULL token
 @Test
 public void testDeserializeWithNullValue() throws Exception {
 String json = "[null]";
 JsonParser jp = factory.createParser(json);
 String[] result = deserializer.deserialize(jp, new DeserializationContext(mapper, new Object(), null));
 assertNull(result);
 }

 // Test for deserialize method when JsonParser encounters VALUE_NULL and non-null elements
 @Test
 public void testDeserializeWithNullAndNonNullValues() throws Exception {
 String json = "[null, \"hello\", \"world\"]";
 JsonParser jp = factory.createParser(json);
 String[] result = deserializer.deserialize(jp, new DeserializationContext(mapper, new Object(), null));
 assertArrayEquals(new String[] {null, "hello", "world"}, result);
 }

 // Test for deserializeCustom method
 @Test
 public void testDeserializeCustom() throws Exception {
 String json = "[\"hello\", \"world\"]";
 JsonParser jp = factory.createParser(json);
 ObjectBuffer buffer = new ObjectBuffer(16);
 String[] result = deserializer._deserializeCustom(jp, new DeserializationContext(mapper, new Object(), buffer)).toFinishedArray(buffer, String[].class);
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 // Test for deserializeWithType method
 @Test
 public void testDeserializeWithType() throws Exception {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(new EmptyArray(), String[].class);
 JsonParser jp = factory.createParser("[\"hello\", \"world\"]");
 Object result = deserializer.deserializeWithType(jp, new DeserializationContext(mapper, new Object(), typeDeserializer), null);
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 // Test for createContextual method with no value deserializer assigned
 @Test
 public void testCreateContextualWithNoValueDeserializerAssigned() throws Exception {
 assertNull(deserializer.createContextual(new DeserializationContext(mapper, new Object(), null), null));
 }

 // Test for createContextual method with a value deserializer assigned
 @Test
 public void testCreateContextualWithValueDeserializerAssigned() throws Exception {
 JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
 deserializer.createContextual(new DeserializationContext(mapper, new Object(), null), null);
 JsonDeserializer<String> result = (JsonDeserializer<String>) deserializer.createContextual(new DeserializationContext(mapper, new Object(), null), null);
 assertNotNull(result);
 assertSame(valueDeserializer, result);
 }

 // Test for createContextual method with a content converter assigned
 @Test
 public void testCreateContextualWithContentConverterAssigned() throws Exception {
 JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
 deserializer.createContextual(new DeserializationContext(mapper, new Object(), null), null);
 JsonDeserializer<Integer> contentConverter = mapper.findValueDeserializer(Integer.class);
 deserializer = deserializer.createContextual(new DeserializationContext(mapper, new Object(), null), null);
 JsonDeserializer<Integer> result = (JsonDeserializer<Integer>) deserializer.createContextual(new DeserializationContext(mapper, new Object(), null), null);
 assertNotNull(result);
 assertNotSame(valueDeserializer, result);
 assertSame(contentConverter, result);
 }

 // Test for isDefaultDeserializer method
 @Test
 public void testIsDefaultDeserializer() throws Exception {
 assertFalse(deserializer.isDefaultDeserializer(elementDeserializer));
 assertFalse(deserializer.isDefaultDeserializer(emptyArray));
 }
}