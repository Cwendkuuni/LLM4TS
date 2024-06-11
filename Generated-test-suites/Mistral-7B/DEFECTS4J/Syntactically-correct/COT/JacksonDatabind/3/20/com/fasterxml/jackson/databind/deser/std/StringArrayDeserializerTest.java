package com.example.stringarraydeserializertest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class StringArrayDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeDefault() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = mapper.getFactory().createParser("[\"apple\",\"banana\",\"orange\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new SimpleStringDeserializer());
 JsonParser jsonParser = mapper.getFactory().createParser("[\"apple\",\"banana\",\"orange\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJsonFormat() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = mapper.getFactory().createParser("{ \"key\": \"value\" }");
 deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullElement() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = mapper.getFactory().createParser("[\"apple\", null, \"orange\"]");
 deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 }

 @Test
 public void testDeserializeEmptyJsonArray() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = mapper.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws Exception {
 ObjectMapper mapperWithFeature = new ObjectMapper();
 mapperWithFeature.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = mapperWithFeature.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapperWithFeature, JsonFactory.instance, null));
 assertArrayEquals(new String[]{}, result);
 assertNull(result[0]);
 }

 @Test
 public void testDeserializeNullInput() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = null;
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 assertNull(result);
 }

 @Test
 public void testDeserializeCustomDeserializerWithNullValues() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new SimpleStringDeserializer());
 JsonParser jsonParser = mapper.getFactory().createParser("[\"apple\", null, \"orange\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 assertArrayEquals(new String[]{"apple", null, "orange"}, result);
 }

 @Test
 public void testDeserializeEmptyCustomDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new EmptyArrayDeserializer());
 JsonParser jsonParser = mapper.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 assertArrayEquals(new String[]{}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithEmptyCustomDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new EmptyArrayDeserializer());
 JsonParser jsonParser = mapper.getFactory().createParser("[\"]");
 deserializer.deserialize(jsonParser, new DeserializationContext(mapper, JsonFactory.instance, null));
 }

 /**
 * Custom deserializers
 */
 public static class SimpleStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
 return jsonParser.getText();
 }
 }

 public static class EmptyArrayDeserializer extends StdDeserializer<String[]> {
 @Override
 public String[] deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
 return new String[0];
 }
 }
}