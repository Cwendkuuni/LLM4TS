package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

 private static final StdKeySerializer serializer = new StdKeySerializer();

 @Test
 public void testSerializeDate() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = provider.getGenerator();
 Date date = new Date();
 serializer.serialize(date, jgen, provider);
 jgen.flush(); // or jgen.close();
 String output = jgen.getOutputBuffer().toString();
 assertEquals("\"$date\"", output);
 }

 @Test
 public void testSerializeString() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = provider.getGenerator();
 String str = "testString";
 serializer.serialize(str, jgen, provider);
 jgen.flush(); // or jgen.close();
 String output = jgen.getOutputBuffer().toString();
 assertEquals("\"testString\"", output);
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = serializer.getSchema(new SerializerProvider(), Object.class);
 assertEquals(JsonNodeType.OBJECT, schema.getType());
 assertEquals("{\"type\":\"string\"}", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 assertTrue(visitor.hasStringFormat());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = provider.getGenerator();
 serializer.serialize(null, jgen, provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonStringOrDate() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = provider.getGenerator();
 int intValue = 1;
 serializer.serialize(intValue, jgen, provider);
 }

}