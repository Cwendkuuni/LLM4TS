package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 @Test
 public void testDeserializationWithDefaultElementDeserializer() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(mockParser, mockContext);
 assertArrayEquals(new String[]{"test1", "test2"}, result);
 }

 @Test
 public void testDeserializationWithCustomElementDeserializer() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 JsonDeserializer<String> customDeserializer = createMockStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] result = deserializer.deserialize(mockParser, mockContext);
 assertArrayEquals(new String[]{"customTest1", "customTest2"}, result);
 }

 @Test
 public void testDeserializationWithNullElement() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(mockParser, mockContext);
 assertArrayEquals(new String[]{null}, result);
 }

 @Test
 public void testDeserializationWithEmptyArray() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(mockParser, mockContext);
 assertArrayEquals(new String[]{}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializationWithInvalidJson() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 mockParser.expectAndReturn("getCurrentToken()", JsonToken.START_OBJECT);
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializationWithInvalidJsonType() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 mockParser.expectAndReturn("getCurrentToken()", JsonToken.VALUE_NUMBER);
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserializationWithContextualization() throws IOException {
 JsonParser mockParser = createMockJsonParser();
 DeserializationContext mockContext = createMockDeserializationContext();
 JsonDeserializer<String> customDeserializer = createMockStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 BeanProperty property = createMockBeanProperty();
 String[] result = deserializer.createContextual(mockContext, property).deserialize(mockParser, mockContext, null);
 assertArrayEquals(new String[]{"customTest1", "customTest2"}, result);
 }

 private JsonParser createMockJsonParser() {
 // Implementation of the mock JsonParser for testing.
 return null;
 }

 private DeserializationContext createMockDeserializationContext() {
 // Implementation of the mock DeserializationContext for testing.
 return null;
 }

 private JsonDeserializer<String> createMockStringDeserializer() {
 // Implementation of the mock StringDeserializer for testing.
 return null;
 }

 private BeanProperty createMockBeanProperty() {
 // Implementation of the mock BeanProperty for testing.
 return null;
 }
}