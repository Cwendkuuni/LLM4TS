package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 @Test
 public void testDeserializeEmptyArray() throws JsonProcessingException {
 JsonParser jp = createParser("[]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new TestingDeserializationContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeArrayWithNullAndValidStrings() throws JsonProcessingException {
 JsonParser jp = createParser("[null, \"hello\", \"world\", null]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new TestingDeserializationContext());
 assertArrayEquals(new String[]{"null", "hello", "world", null}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomElementDeserializer() throws JsonProcessingException {
 // Assuming we have a custom JsonDeserializer for String called CustomStringDeserializer
 JsonParser jp = createParser("[\"customString1\", \"customString2\"]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 String[] result = deserializer.deserialize(jp, new TestingDeserializationContext());
 assertArrayEquals(new String[]{"customString1", "customString2"}, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JsonParser jp = createParser("{ \"key\": \"value\" }");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 deserializer.deserialize(jp, new TestingDeserializationContext());
 fail("Expected JsonProcessingException but did not get one.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullElementDeserializer() throws JsonProcessingException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
 try {
 deserializer.deserialize(createParser("[]"), new TestingDeserializationContext());
 fail("Expected JsonMappingException but did not get one.");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeArrayWithEmptyString() throws JsonProcessingException {
 JsonParser jp = createParser("[\"\", \"hello\", \"world\"]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new TestingDeserializationContext());
 assertArrayEquals(new String[]{"", "hello", "world"}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomTypeDeserializer() throws JsonProcessingException {
 // Assuming we have a TypeDeserializer for String array called CustomTypeDeserializer
 JsonParser jp = createParser("[1, 2, 3]"); // This JSON should be deserialized to String[3]
 TestingTypeDeserializer customTypeDeserializer = new TestingTypeDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 Object result = deserializer.deserializeWithType(jp, new TestingDeserializationContext(), customTypeDeserializer);
 assertArrayEquals(new String[]{"1", "2", "3"}, result);
 }

 @Test
 private JsonParser createParser(String json) throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getFactory().createParser(json);
 }

 @Test
 private static void assertArrayEquals(Object expected, Object actual) {
 if (!expected.getClass().isArray() || actual.getClass().isArray() && expected.getClass().getComponentType() != actual.getClass().getComponentType()) {
 fail("Arrays have different types.");
 }
 Object[] expectedArray = (Object[]) expected;
 Object[] actualArray = (Object[]) actual;
 if (expectedArray.length != actualArray.length) {
 fail("Arrays have different lengths.");
 }
 for (int i = 0; i < expectedArray.length; i++) {
 if (!expectedArray[i].equals(actualArray[i])) {
 fail("Arrays have different values at index " + i + ": expected " + expectedArray[i] + ", but was " + actualArray[i]);
 }
 }
 }

 class TestingDeserializationContext extends DeserializationContext {
 @Override
 public ObjectBuffer leakObjects() {
 return null;
 }
 }

 class TestingTypeDeserializer implements TypeDeserializer {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new String[0];
 }
 }
}