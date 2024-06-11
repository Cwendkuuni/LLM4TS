package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
 String[] result = deserializer.deserialize(jp, null);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().createValueDeserializer(String.class));
 JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
 String[] result = deserializer.deserialize(jp, null);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNonArray() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = mapper.getFactory().createParser("\"abc\"");
 deserializer.deserialize(jp, null);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullElement() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = mapper.getFactory().createParser("[]");
 deserializer.deserialize(jp, null);
 }

 @Test
 public void testDeserializeWithCustomDeserializerAndNullElement() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().createValueDeserializer(String.class));
 JsonParser jp = mapper.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jp, null);
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeWithEmptyArray() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = mapper.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jp, null);
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(String[].class);
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserializeWithType(mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]"), null, typeDeserializer);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 public void testCreateContextual() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory factory = mapper.getFactory();
 BeanProperty property = new BeanProperty("test", String[].class, factory);
 JsonDeserializer<?> result = deserializer.createContextual(factory.findContextualizationInfo(property), property);
 assertTrue(result instanceof StringArrayDeserializer);
 }

 @Test
 public void testCreateContextualWithNullElementDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().createValueDeserializer(String.class));
 JsonFactory factory = mapper.getFactory();
 BeanProperty property = new BeanProperty("test", String[].class, factory);
 JsonDeserializer<?> result = deserializer.createContextual(factory.findContextualizationInfo(property), property);
 assertNotNull(result);
}

 @Test
 public void testCreateContextualWithCustomDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().createValueDeserializer(String.class));
 JsonFactory factory = mapper.getFactory();
 BeanProperty property = new BeanProperty("test", String[].class, factory);
 JsonDeserializer<?> customDeserializer = mapper.getFactory().createValueDeserializer(Integer.class);
 JsonDeserializer<?> result = deserializer.createContextual(factory.findContextualizationInfo(property), property, customDeserializer);
 assertNotNull(result);
 assertTrue(result instanceof StringArrayDeserializer);
 assertTrue(result.getConfig() instanceof StringArrayDeserializer.Config);
 assertEquals(customDeserializer, ((StringArrayDeserializer.Config) result.getConfig())._elementDeserializer);
 }
}