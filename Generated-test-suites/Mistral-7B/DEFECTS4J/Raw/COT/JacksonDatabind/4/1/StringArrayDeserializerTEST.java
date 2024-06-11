package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeStandard() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectBuffer buffer = mapper.getFactory().getObjectBuffer();
 Object[] chunk = buffer.resetAndStart();
 JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");

 String[] result = deserializer.deserialize(jp, null);
 assertEquals(3, result.length);
 assertEquals("abc", result[0]);
 assertEquals("def", result[1]);
 assertEquals("ghi", result[2]);

 // Test with empty array
 jp = mapper.getFactory().createParser("[]");
 result = deserializer.deserialize(jp, null);
 assertEquals(0, result.length);

 // Test with null array
 jp = mapper.getFactory().createParser("null");
 result = deserializer.deserialize(jp, null);
 assertNull(result);

 // Test with single value
 jp = mapper.getFactory().createParser("\"abc\"");
 result = deserializer.deserialize(jp, null);
 assertEquals(1, result.length);
 assertEquals("abc", result[0]);

 buffer.release();
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new StdDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getText() + "_custom";
 }
 });
 ObjectBuffer buffer = mapper.getFactory().getObjectBuffer();
 Object[] chunk = buffer.resetAndStart();
 JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");

 String[] result = deserializer.deserialize(jp, null);
 assertEquals(3, result.length);
 assertEquals("abc_custom", result[0]);
 assertEquals("def_custom", result[1]);
 assertEquals("ghi_custom", result[2]);

 buffer.release();
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = mapper.getFactory().createParser("{");
 deserializer.deserialize(jp, null);
 }

 @Test(expected = IOException.class)
 public void testDeserializeMissingEndArrayToken() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = mapper.getFactory().createParser("[\"" + "abc" + "\"");
 deserializer.deserialize(jp, null);
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeNullContextualDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
 ObjectBuffer buffer = mapper.getFactory().getObjectBuffer();
 Object[] chunk = buffer.resetAndStart();
 JsonParser jp = mapper.getFactory().createParser("[\"" + "abc" + "\"]");
 // This should throw NPE
 deserializer.deserialize(jp, null);

 buffer.release();
 }
}