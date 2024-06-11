package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextualizer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringArrayDeserializerTest {

 private ObjectMapper mapper;
 private StringArrayDeserializer stringArrayDeserializer;

 @BeforeAll
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 stringArrayDeserializer = StringArrayDeserializer.instance;
 }

 @Test
 public void testDeserializeStandard() throws Exception {
 String[] expected = new String[]{"test1", "test2"};
 JsonParser jp = mapper.getFactory().createParser("[\"test1\", \"test2\"]");
 String[] result = stringArrayDeserializer.deserialize(jp, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeCustom() throws Exception {
 JsonDeserializer<String> elementDeserializer = mapper.findValueDeserializer(String.class);
 StringArrayDeserializer customStringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
 String[] expected = new String[]{"test1", "test2"};
 JsonParser jp = mapper.getFactory().createParser("[\"test1\", \"test2\"]");
 String[] result = customStringArrayDeserializer.deserialize(jp, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeNull() throws Exception {
 String[] expected = null;
 JsonParser jp = mapper.getFactory().createParser("null");
 String[] result = stringArrayDeserializer.deserialize(jp, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeEmptyString() throws Exception {
 String[] expected = new String[0];
 JsonParser jp = mapper.getFactory().createParser("[]");
 String[] result = stringArrayDeserializer.deserialize(jp, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws Exception {
 DeserializationContext ctxt = new DeserializationContext.RootContext();
 ctxt.setEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 String[] expected = null;
 JsonParser jp = mapper.getFactory().createParser("[]");
 String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeInvalidJson() throws Exception {
 JsonParser jp = mapper.getFactory().createParser("{ \"key\": \"value\" }");
 assertThrows(IOException.class, () -> stringArrayDeserializer.deserialize(jp, new DeserializationContext.RootContext()));
 }

 @Test
 public void testDeserializeInvalidJsonWithCustomDeserializer() throws Exception {
 JsonDeserializer<String> elementDeserializer = mapper.findValueDeserializer(String.class);
 StringArrayDeserializer customStringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
 JsonParser jp = mapper.getFactory().createParser("{ \"key\": \"value\" }");
 assertThrows(IOException.class, () -> customStringArrayDeserializer.deserialize(jp, new DeserializationContext.RootContext()));
 }

 @Test
 public void testDeserializeNullElementDeserializer() throws Exception {
 StringArrayDeserializer stringArrayDeserializerWithNullElementDeserializer = new StringArrayDeserializer(null);
 JsonParser jp = mapper.getFactory().createParser("[null]");
 assertThrows(IOException.class, () -> stringArrayDeserializerWithNullElementDeserializer.deserialize(jp, new DeserializationContext.RootContext()));
 }

 @Test
 public void testDeserializeEmptyContextualizer() throws Exception {
 TypeDeserializer emptyContextualizer = new EmptyContextualizer();
 StringArrayDeserializer stringArrayDeserializerWithEmptyContextualizer = new StringArrayDeserializer(emptyContextualizer);
 JsonParser jp = mapper.getFactory().createParser("[\"test1\", \"test2\"]");
 String[] result = stringArrayDeserializerWithEmptyContextualizer.deserialize(jp, new DeserializationContext.RootContext());
 assertArrayEquals(new String[]{"test1", "test2"}, result);
 }
}