package com.example.stringarraydeserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringArrayDeserializer Test")
public class StringArrayDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Nested
 @DisplayName("Tests for deserialize method")
 class DeserializeTests {

 @Test
 @DisplayName("Test empty array")
 void testEmptyArray() throws JsonProcessingException {
 String[] expected = new String[0];
 String[] actual = StringArrayDeserializer.instance.deserialize(mapper.parser(), new DeserializationContext(mapper, null));
 assertArrayEquals(expected, actual);
 }

 @Test
 @DisplayName("Test null input")
 void testNullInput() throws JsonProcessingException {
 String[] expected = null;
 String[] actual = StringArrayDeserializer.instance.deserialize(mapper.parser(), new DeserializationContext(mapper, null));
 assertArrayEquals(expected, actual);
 }

 @Test
 @DisplayName("Test empty string as null")
 void testEmptyStringAsNull() throws JsonProcessingException {
 JsonParser parser = mapper.parser();
 parser.nextValue("[]");
 parser.nextValue("null");
 String[] actual = StringArrayDeserializer.instance.deserialize(parser, new DeserializationContext(mapper, null));
 assertArrayEquals(new String[0], actual);
 }

 @Test
 @DisplayName("Test valid array")
 void testValidArray() throws JsonProcessingException {
 JsonParser parser = mapper.parser();
 parser.nextValue("[\"hello\", \"world\"]");
 String[] actual = StringArrayDeserializer.instance.deserialize(parser, new DeserializationContext(mapper, null));
 String[] expected = new String[]{"hello", "world"};
 assertArrayEquals(expected, actual);
 }

 @Test
 @DisplayName("Test array with null values")
 void testArrayWithNullValues() throws JsonProcessingException {
 JsonParser parser = mapper.parser();
 parser.nextValue("[\"hello\", null, \"world\"]");
 String[] actual = StringArrayDeserializer.instance.deserialize(parser, new DeserializationContext(mapper, null));
 String[] expected = new String[]{"hello", null, "world"};
 assertArrayEquals(expected, actual);
 }
 }

 @Nested
 @DisplayName("Tests for _deserializeCustom method")
 class DeserializeCustomTests {

 @Test
 @DisplayName("Test empty array")
 void testEmptyArray_deserializeCustom() throws JsonProcessingException {
 String[] expected = new String[0];
 ObjectDeserializer customDeserializer = new ObjectDeserializer(mapper, String[].class);
 customDeserializer.setContentDeserializer(new StringDeserializer(mapper));
 JsonParser parser = mapper.parser();
 parser.nextValue("[null]");
 String[] actual = customDeserializer.deserialize(parser, new DeserializationContext(mapper, null));
 assertArrayEquals(expected, actual);
 }

 @Test
 @DisplayName("Test valid array")
 void testValidArray_deserializeCustom() throws JsonProcessingException {
 ObjectDeserializer customDeserializer = new ObjectDeserializer(mapper, String[].class);
 customDeserializer.setContentDeserializer(new StringDeserializer(mapper));
 JsonParser parser = mapper.parser();
 parser.nextValue("[\"hello\", \"world\"]");
 String[] actual = customDeserializer.deserialize(parser, new DeserializationContext(mapper, null));
 String[] expected = new String[]{"hello", "world"};
 assertArrayEquals(expected, actual);
 }
 }

 @Test
 @DisplayName("Test deserializeWithType method")
 void testDeserializeWithType() throws JsonProcessingException {
 ObjectMapper objMapper = new ObjectMapper();
 ObjectDeserializer deserializer = objMapper.getTypeFactory().constructValueDeserializer(String[].class, objMapper);
 JsonParser parser = objMapper.parser();
 parser.nextValue("[\"hello\", \"world\"]");
 String[] actual = (String[]) deserializer.deserializeWithType(parser, new DeserializationContext(objMapper, null), new ArrayType(String.class, null));
 String[] expected = new String[]{"hello", "world"};
 assertArrayEquals(expected, actual);
 }

 @Test
 @DisplayName("Test handleNonArray method")
 void testHandleNonArray() throws JsonProcessingException {
 JsonParser parser = mapper.parser();
 parser.nextValue("hello");
 String[] actual = StringArrayDeserializer.instance.handleNonArray(parser, new DeserializationContext(mapper, null));
 assertEquals(new String[]{"hello"}, actual);
 }

 @Test
 @DisplayName("Test createContextual method")
 void testCreateContextual() throws JsonProcessingException {
 JsonParser parser = mapper.parser();
 parser.nextValue("[null]");
 DeserializationContext ctxt = new DeserializationContext(mapper, new StandardDeserializationContext());
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(ctxt, new BeanProperty("fieldName", String[].class));
 Object[] actual = contextualDeserializer.deserialize(parser, ctxt);
 assertArrayEquals(new String[0], actual);
 }
 }