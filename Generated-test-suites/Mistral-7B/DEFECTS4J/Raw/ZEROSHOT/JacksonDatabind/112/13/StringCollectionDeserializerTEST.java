package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.JsonDeserializer.None;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {

 private ObjectMapper objectMapper;
 private JsonFactory jsonFactory;
 private JsonParser jsonParser;
 private StringCollectionDeserializer deserializer;
 private TypeDeserializer typeDeserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 jsonParser = jsonFactory.createParser("[]");
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(ArrayList.class, String.class), null, null, null, null, null);
 typeDeserializer = mock(TypeDeserializer.class);
 }

 @Test
 public void testEmptyArray() throws IOException, JsonProcessingException {
 Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws IOException, JsonProcessingException {
 String testValue = "test";
 JsonDeserializer<String> customDeserializer = new None.Standard();
 when(typeDeserializer.deserializeTypedFromArray(jsonParser, objectMapper.getDeserializationContext())).thenReturn( Collections.singletonList(testValue) );
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(ArrayList.class, String.class), null, null, null, null, null, typeDeserializer);
 Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(Collections.singletonList(testValue), result);
 }

 @Test
 public void testDeserializeWithValueDeserializer() throws IOException, JsonProcessingException {
 String testValue = "test";
 JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
 when(valueDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext())).thenReturn(testValue);
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(ArrayList.class, String.class), valueDeserializer, null, null, null, null, null);
 Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(Collections.singletonList(testValue), result);
 }

 @Test
 public void testDeserializeWithNullValue() throws IOException, JsonProcessingException {
 JsonParser jsonParserWithNull = new BaseJsonParser(jsonFactory);
 jsonParserWithNull.nextValue();
 jsonParserWithNull.nextToken();
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(ArrayList.class, String.class), null, null, null, null, null, null);
 Collection<String> result = deserializer.deserialize(jsonParserWithNull, objectMapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithStringValue() throws IOException, JsonProcessingException {
 String testValue = "test";
 Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(Collections.singletonList(testValue), result);
 }

 @Test
 public void testDeserializeWithValueDeserializerAndNullValues()
 throws IOException, JsonProcessingException {
 JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
 when(valueDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext())).thenReturn("test");
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(ArrayList.class, String.class), valueDeserializer, null, null, null, null, null);
 BaseJsonParser jsonParserWithNulls = new BaseJsonParser(jsonFactory);
 jsonParserWithNulls.nextValue();
 jsonParserWithNulls.nextToken();
 jsonParserWithNulls.nextToken();
 jsonParserWithNulls.nextValue();
 jsonParserWithNulls.nextToken();
 Collection<String> result = deserializer.deserialize(jsonParserWithNulls, objectMapper.getDeserializationContext());
 assertEquals(Collections.singletonList("test"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJson() throws IOException, JsonProcessingException {
 Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 }
}