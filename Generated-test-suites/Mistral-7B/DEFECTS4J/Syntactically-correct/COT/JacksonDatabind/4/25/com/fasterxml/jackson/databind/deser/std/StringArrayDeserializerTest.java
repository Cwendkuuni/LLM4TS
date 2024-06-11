package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringArrayDeserializer Test")
public class StringArrayDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Nested
 @DisplayName("Testing deserialize Method")
 class DeserializeTest {

 @DisplayName("Given valid JSON Array of Strings, when deserialize method is called, then correct String Array should be returned")
 @Test
 void testValidJsonArray() throws JsonProcessingException {
 String json = "[\"Hello\", \"World\"]";
 String[] expected = {"Hello", "World"};
 String[] actual = objectMapper.readValue(json, StringArrayDeserializer.class);
 assertArrayEquals(expected, actual);
 }

 @DisplayName("Given empty JSON Array, when deserialize method is called, then empty String Array should be returned")
 @Test
 void testEmptyJsonArray() throws JsonProcessingException {
 String json = "[]";
 String[] actual = objectMapper.readValue(json, StringArrayDeserializer.class);
 assertArrayEquals(new String[0], actual);
 }

 @DisplayName("Given JSON Array with null values, when deserialize method is called, then correct String Array with null values should be returned")
 @Test
 void testJsonArrayWithNullValues() throws JsonProcessingException {
 String json = "[null, null]";
 String[] expected = {null, null};
 String[] actual = objectMapper.readValue(json, StringArrayDeserializer.class);
 assertArrayEquals(expected, actual);
 }

 @DisplayName("Given JSON Array with non-String values, when deserialize method is called, then MappingException should be thrown")
 @Test
 void testJsonArrayWithNonStringValues() throws JsonProcessingException {
 String json = "[1, 2, 3]";
 assertThrows(MappingException.class, () -> objectMapper.readValue(json, StringArrayDeserializer.class));
 }
 }

 @Nested
 @DisplayName("Testing deserializeCustom Method")
 class DeserializeCustomTest {

 @DisplayName("Given valid JSON Array of Strings with custom value deserializer, when deserializeCustom method is called, then correct String Array should be returned")
 @Test
 void testValidJsonArrayWithCustomDeserializer() throws JsonProcessingException {
 String json = "[{\"value\":\"Hello\"}, {\"value\":\"World\"}]";
 String[] expected = {"Hello", "World"};
 JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
 };
 String[] actual = objectMapper.readValue(json, new StringArrayDeserializer(customDeserializer).getClass());
 assertArrayEquals(expected, actual);
 }
 }

 @Nested
 @DisplayName("Testing deserializeWithType Method")
 class DeserializeWithTypeTest {

 @DisplayName("Given valid JSON Array of Strings with generic type, when deserializeWithType method is called, then correct TypeReference should be returned")
 @Test
 void testValidJsonArrayWithGenericType() throws JsonProcessingException {
 String json = "[\"Hello\", \"World\"]";
 TypeReference<String[]> typeReference = new TypeReference<String[]>() {};
 String[] actual = objectMapper.readValue(json, StringArrayDeserializer.class);
 assertEquals(typeReference.getType(), actual.getClass());
 }
 }

 @Nested
 @DisplayName("Testing handleNonArray Method")
 class HandleNonArrayTest {

 @DisplayName("Given empty JSON String, when handleNonArray method is called, then null should be returned")
 @Test
 void testEmptyJsonString() throws JsonProcessingException {
 String json = "";
 String[] actual = objectMapper.findValue(json, StringArrayDeserializer.class);
 assertNull(actual);
 }

 @DisplayName("Given JSON String representing single String value, when handleNonArray method is called and acceptSingleValueAsArray feature is enabled, then correct String Array with single element should be returned")
 @Test
 void testJsonStringWithSingleValueAndAcceptSingleValueAsArrayEnabled() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
 String json = "\"Hello\"";
 String[] actual = mapper.readValue(json, StringArrayDeserializer.class);
 assertEquals(new String[]{"Hello"}, actual);
 }

 @DisplayName("Given JSON String representing single String value, when handleNonArray method is called and acceptSingleValueAsArray feature is disabled, then MappingException should be thrown")
 @Test
 void testJsonStringWithSingleValueAndAcceptSingleValueAsArrayDisabled() throws JsonProcessingException {
 String json = "\"Hello\"";
 assertThrows(MappingException.class, () -> objectMapper.readValue(json, StringArrayDeserializer.class));
 }
 }

 @Nested
 @DisplayName("Testing createContextual Method")
 class CreateContextualTest {

 @DisplayName("Given configured ObjectMapper with custom value deserializer for String type, when createContextual method is called, then correct StringArrayDeserializer instance with custom deserializer should be returned")
 @Test
 void testCreateContextualWithCustomDeserializer() throws JsonProcessingException {
 JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule().setSerializer(String.class, new StringSerializer())
 .setDeserializer(String.class, customDeserializer));
 StringArrayDeserializer actual = mapper.findValue("[]", StringArrayDeserializer.class);
 assertNotNull(actual);
 JsonDeserializer<?> deserializer = actual.getDeserializerForProperty(null, null);
 assertEquals(customDeserializer, deserializer);
 }
 }
}