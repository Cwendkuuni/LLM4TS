package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

/**
 * JUnit test file for StringArrayDeserializer class
 */
public class StringArrayDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void initialize() {
 StringArrayDeserializer.instance = new StringArrayDeserializer();
 }

 /**
 * Test for deserialize method with default element deserializer
 */
 @Test
 public void testDeserializeDefault() throws IOException {
 String[] expected = new String[]{"test1", "test2", "test3"};
 JsonParser jp = mapper.parser();
 jp.readValueAs(new TypeReference<String[]>() {
 });
 jp.nextToken(); // consume [
 jp.nextValue(); // consume "test1"
 jp.nextValue(); // consume "test2"
 jp.nextValue(); // consume "test3"
 jp.nextToken(); // consume ]
 String[] actual = StringArrayDeserializer.instance.deserialize(jp, new TestContext());
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for deserialize method with custom element deserializer
 */
 @Test
 public void testDeserializeCustom() throws IOException {
 JsonDeserializer<String> customDeserializer = new StdSerializer<String>(String.class) {};
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] expected = new String[]{"test1", "test2", "test3"};
 JsonParser jp = mapper.parser();
 jp.readValueAs(new TypeReference<String[]>() {
 });
 jp.nextToken(); // consume [
 jp.nextValue(); // consume "test1"
 jp.nextValue(); // consume "test2"
 jp.nextValue(); // consume "test3"
 jp.nextToken(); // consume ]
 String[] actual = deserializer.deserialize(jp, new TestContext());
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for deserialize method with empty array
 */
 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] expected = new String[0];
 JsonParser jp = mapper.parser();
 jp.nextToken(); // consume null
 JsonToken endArrayToken = jp.nextToken(); // consume ]
 assertEquals(JsonToken.END_ARRAY, endArrayToken);
 String[] actual = StringArrayDeserializer.instance.deserialize(jp, new TestContext());
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for deserialize method with single null value
 */
 @Test
 public void testDeserializeSingleNullValue() throws IOException {
 String[] expected = new String[]{null};
 JsonParser jp = mapper.parser();
 jp.nextToken(); // consume null
 JsonToken endArrayToken = jp.nextToken(); // consume ]
 assertEquals(JsonToken.END_ARRAY, endArrayToken);
 String[] actual = StringArrayDeserializer.instance.deserialize(jp, new TestContext());
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for deserialize method with custom null value
 */
 @Test
 public void testDeserializeCustomNullValue() throws IOException {
 JsonDeserializer<String> customDeserializer = new StdSerializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] expected = new String[]{null};
 JsonParser jp = mapper.parser();
 jp.nextToken(); // consume null
 JsonToken endArrayToken = jp.nextToken(); // consume ]
 assertEquals(JsonToken.END_ARRAY, endArrayToken);
 String[] actual = deserializer.deserialize(jp, new TestContext());
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for deserialize method with non-array input
 */
 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws IOException {
 JsonParser jp = mapper.parser();
 jp.nextToken(); // consume null
 jp.nextValue(); // consume 42
 StringArrayDeserializer.instance.deserialize(jp, new TestContext());
 }

 /**
 * Test for deserializeWithType method
 */
 @Test
 public void testDeserializeWithType() throws IOException {
 TypeReference<String[]> typeRef = new TypeReference<String[]>() {};
 String[] expected = new String[]{"test1", "test2", "test3"};
 JsonParser jp = mapper.parser();
 String[] actual = StringArrayDeserializer.instance.deserializeWithType(jp, new TestContext(), mapper.getTypeDeserializer
 ());
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for createContextual method with null valueDeserializer
 */
 @Test
 public void testCreateContextualNullValueDeserializer() {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 assertNull(deserializer.createContextual(null, null));
 }

 /**
 * Test for createContextual method with default valueDeserializer
 */
 @Test
 public void testCreateContextualDefaultValueDeserializer() {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonDeserializer<String> defaultDeserializer = new StdSerializer<String>(String.class) {};
 assertEquals(deserializer, deserializer.createContextual(new TestContext(), null));
 }

 /**
 * Test for createContextual method with custom valueDeserializer
 */
 @Test
 public void testCreateContextualCustomValueDeserializer() {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonDeserializer<String> customDeserializer = new StdSerializer<String>(String.class) {};
 StringArrayDeserializer customDeserializerWrapper = new StringArrayDeserializer(customDeserializer);
 assertEquals(customDeserializerWrapper, deserializer.createContextual(new TestContext(), null));
 }

}

 class TestContext extends DeserializationContext {
 @Override
 public ObjectBuffer leaseObjectBuffer() {
 return new ObjectBuffer();
 }
 }