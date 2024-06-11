package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer.
 */
@JacksonAnnotationProcessingTestCase
public class StringArrayDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Before
 public void setUp() throws Exception {
 // Set up ObjectMapper
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser("[]"), new TestContext());
 assertEquals(new String[0], result);
 }

 @Test
 public void testDeserializeWithNullElement() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser("[]"), new TestContext());
 assertNull(result[0]);
 }

 @Test
 public void testDeserializeWithNullElementDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(nullDeserializer());
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser("['null']"), new TestContext());
 assertNull(result[0]);
 }

 @Test
 public void testDeserializeWithSingleStringElement() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser("['Hello']"), new TestContext());
 assertEquals("Hello", result[0]);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws Exception {
 String[] expected = new String[]{"expected1", "expected2"};
 JsonDeserializer<String> elementDeserializer = new CustomStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser("['expected1', 'expected2']"), new TestContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeWithEmptyStringAsNull() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 ObjectReader reader = mapper.readerForArrayOf(String.class);
 String[] result = deserializer.deserialize(reader.readValueAsArray(MAPPER.getFactory().createParser("['']")), new TestContext());
 assertNull(result[0]);
 }

 @Test
 public void testDeserializeWithNonArray() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String input = "{\"name\": \"John\", \"age\": 30}";
 JsonParser parser = MAPPER.getFactory().createParser(input);
 try {
 deserializer.deserialize(parser, new TestContext());
 fail("Expected MappingException");
 } catch (MappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithNonArray_acceptEmptyAsArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String input = "Hello";
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser(input), new TestContext());
 assertEquals(new String[]{"Hello"}, result);
 }

 @Test
 public void testDeserializeWithNonArray_customDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomNullDeserializer());
 String input = "null";
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser(input), new TestContext());
 assertNull(result[0]);
 }

 @Test
 public void testContextualization() throws Exception {
 String input = "[\"expected1\", \"expected2\"]";
 BeanProperty property = new BeanProperty("testProperty", String[].class);
 StringArrayDeserializer deserializer1 = StringArrayDeserializer.instance;
 JsonDeserializer<String> deserializer2 = new CustomStringDeserializer();
 StringArrayDeserializer deserializer3 = new StringArrayDeserializer(deserializer2);
 StringArrayDeserializer deserializer4 = new StringArrayDeserializer(deserializer2);
 deserializer4.activateDefaultContextualization();
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<?> result1 = deserializer1.createContextual(ctxt, property);
 JsonDeserializer<?> result2 = deserializer2.createContextual(ctxt, property);
 JsonDeserializer<?> result3 = deserializer3.createContextual(ctxt, property);
 JsonDeserializer<?> result4 = deserializer4.createContextual(ctxt, property);
 assertEquals(deserializer1.getClass(), result1.getClass());
 assertEquals(deserializer2.getClass(), result2.getClass());
 assertEquals(deserializer3.getClass(), result3.getClass());
 assertEquals(StringArrayDeserializer.class, result4.getClass());
 }

 private static JsonDeserializer<?> nullDeserializer() {
 return new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 }

 private static class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getText();
 }
 }

 private static class CustomNullDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 }

 private static class TestContext extends DeserializationContext {
 // Empty for now
 }

}