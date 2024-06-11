package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 JsonParser jp = new JsonFactory().createParser("[]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(jp, new TestContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeStringArrayWithNulls() throws IOException {
 JsonParser jp = new JsonFactory().createParser("[null, \"hello\", null]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(jp, new TestContext());
 assertArrayEquals(new String[]{"null", "hello", null}, result);
 }

 @Test
 public void testDeserializeStringArrayWithCustomDeserializer() throws IOException {
 JsonParser jp = new JsonFactory().createParser("[{\"num\":1},{\"num\":2}]");
 ObjectMapper mapper = new ObjectMapper();
 StringArrayDeserializer deserializer = mapper.getTypeFactory().constructValueType(String[].class, MyCustomDeserializer.class);
 String[] result = deserializer.deserialize(jp, new TestContext());
 assertArrayEquals(new String[]{"1", "2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser jp = new JsonFactory().createParser("{invalidJson}");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 deserializer.deserialize(jp, new TestContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeNullInput() throws IOException {
 JsonParser jp = new JsonFactory().createParser(null);
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 deserializer.deserialize(jp, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 JsonParser jp = new JsonFactory().createParser("[1, 2]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 deserializer.deserialize(jp, new TestContext());
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectBuffer leaseObjectBuffer() {
 return new ObjectBuffer(10);
 }

 @Override
 public void returnObjectBuffer(ObjectBuffer buffer) {}
}

class MyCustomDeserializer extends StdSerializer<Object> {
 protected MyCustomDeserializer() {
 this(null);
 }

 protected MyCustomDeserializer(Class<Object> t) {
 super(t);
 }

 @Override
 public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return ctxt.constructValue("{\"num\": " + jp.getValueAsInt() + "}");
 }
}