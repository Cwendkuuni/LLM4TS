package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 @Test
 public void testEmptyArray() throws IOException {
 JsonParser jp = createParser("[]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new DeserializationContext(), null);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testNullArray() throws IOException {
 JsonParser jp = createParser("null");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new DeserializationContext(), null);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testSingleElementArray() throws IOException {
 JsonParser jp = createParser("[\"Hello\"]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new DeserializationContext(), null);
 assertEquals("Hello", result[0]);
 }

 @Test
 public void testMultipleElementsArray() throws IOException {
 JsonParser jp = createParser("[\"Hello\", \"World\", null]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new DeserializationContext(), null);
 assertEquals("Hello", result[0]);
 assertEquals("World", result[1]);
 assertNull(result[2]);
 }

 @Test
 public void testCustomElementDeserializer() throws IOException {
 JsonParser jp = createParser("[{\"name\": \"Alice\"}, {\"name\": \"Bob\"}]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new NameDeserializer());
 String[] result = deserializer.deserialize(jp, new DeserializationContext(), null);
 assertEquals(2, result.length);
 assertEquals("Alice", result[0]);
 assertEquals("Bob", result[1]);
 }

 @Test
 public void testEmptyStringAsNull() throws IOException {
 JsonParser jp = createParser("[\"\", \"Hello\"]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(jp, new DeserializationContext(), null);
 assertArrayEquals(new String[]{"", "Hello"}, result);

 JsonParser jp2 = createParser("[\"\"]");
 StringArrayDeserializer deserializer2 = new StringArrayDeserializer();
 String[] result2 = deserializer2.deserialize(jp2, new DeserializationContext(), null);
 assertNull(result2);
 }

 @Test
 public void testNonStringElement() throws IOException {
 JsonParser jp = createParser("[1, 2, 3]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializer.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnexpectedEndOfStream() throws IOException {
 JsonParser jp = createParser("[\"Hello\"");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializer.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnexpectedCharacter() throws IOException {
 JsonParser jp = createParser("[\"Hello\", 1]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializer.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnexpectedNullValue() throws IOException {
 JsonParser jp = createParser("[null]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializera.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnexpectedStartArrayToken() throws IOException {
 JsonParser jp = createParser("[[\"Hello\"");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializer.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnexpectedEndArrayToken() throws IOException {
 JsonParser jp = createParser("[\"Hello\"");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializer.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnexpectedValueSeparator() throws IOException {
 JsonParser jp = createParser("[\":\"Hello\", \"World\"]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 try {
 deserializer.deserialize(jp, new DeserializationContext(), null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 private static JsonParser createParser(String json) throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getFactory().createParser(mapper.readTree(json).getRawValue());
 }

 private static void assertArrayEquals(Object[] expected, Object[] actual) {
 if (!expected.length == actual.length) {
 fail("Arrays have different lengths.");
 }
 for (int i = 0; i < expected.length; i++) {
 if (!expected[i].equals(actual[i])) {
 fail("Unexpected value at index " + i + ": expected [" + printArray(expected) + "], but was [" + printArray(actual) + "].");
 }
 }
 }

 private static String printArray(Object[] array) {
 StringBuilder sb = new StringBuilder("[");
 for (int i = 0; i < array.length; i++) {
 if (i > 0) {
 sb.append(", ");
 }
 sb.append('"').append(array[i].toString()).append('"');
 }
 sb.append("]");
 return sb.toString();
 }

}