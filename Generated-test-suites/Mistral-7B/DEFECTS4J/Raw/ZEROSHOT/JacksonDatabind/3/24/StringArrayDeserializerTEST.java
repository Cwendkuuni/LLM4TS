package com.example.test.jackson;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;
 private StringArrayDeserializer stringArrayDeserializer;
 private JsonDeserializer<String> stringDeserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringArrayDeserializer = StringArrayDeserializer.instance;
 stringDeserializer = mock(JsonDeserializer.class);
 }

 @Test
 public void testDeserializeNullElementDeserializer() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(objectMapper.getFactory().createParser("[null]"), new DeserializationContext(objectMapper));
 assertArrayEquals(new String[]{null}, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(objectMapper.getFactory().createParser("[]"), new DeserializationContext(objectMapper));
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(objectMapper.getFactory().createParser("[\"hello\"]"), new DeserializationContext(objectMapper));
 assertArrayEquals(new String[]{"hello"}, result);
 }

 @Test
 public void testDeserializeMultipleElementArray() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(objectMapper.getFactory().createParser("[\"hello\", \"world\"]"), new DeserializationContext(objectMapper));
 assertArrayEquals(new String[]{"hello", "world"}, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws IOException {
 when(stringDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class))).thenReturn("test");
 stringArrayDeserializer = new StringArrayDeserializer(stringDeserializer);
 String[] result = stringArrayDeserializer.deserialize(objectMapper.getFactory().createParser("[\"hello\", \"world\"]"), new DeserializationContext(objectMapper));
 assertArrayEquals(new String[]{"test", "test"}, result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(mock(JsonParser.class), mock(DeserializationContext.class))).thenReturn(new String[]{"test1", "test2"});
 String[] result = stringArrayDeserializer.deserializeWithType(objectMapper.getFactory().createParser("[\"test1\", \"test2\"]"), new DeserializationContext(objectMapper), typeDeserializer);
 assertArrayEquals(new String[]{"test1", "test2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 stringArrayDeserializer.deserialize(objectMapper.getFactory().createParser("{foo: \"bar\"}"), new DeserializationContext(objectMapper));
 }

}