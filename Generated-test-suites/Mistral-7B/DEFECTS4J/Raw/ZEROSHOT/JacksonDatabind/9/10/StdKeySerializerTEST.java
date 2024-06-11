package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class StdKeySerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws JsonProcessingException, IOException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 serializer.serialize(date, gen, provider);
 gen.flush();
 String result = gen.getOutputContent();
 assertEquals("\"${date-time}\"", result); // Replace "${date-time}" with the actual date-time string
 }

 @Test
 public void testSerializeString() throws JsonProcessingException, IOException {
 StdKeySerializer serializer = new StdKeySerializer();
 Object value = "test-string";
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 serializer.serialize(value, gen, provider);
 gen.flush();
 String result = gen.getOutputContent();
 assertEquals("\"test-string\"", result);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), mapper.getTypeFactory().constructType(String.class));
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultFormatVisitor(mapper.getTypeFactory().constructType(String.class), null, null, null);
 serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(String.class));
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException, IOException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 serializer.serialize(null, gen, provider);
 gen.flush();
 String result = gen.getOutputContent();
 assertEquals("null", result);
 }

 @Test
 public void testSerializeObject() throws JsonProcessingException, IOException {
 Object value = new Object();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 typeSerializer.writeTypePrefixForScalar(value.getClass(), gen, provider);
 serializer.serialize(value, gen, provider);
 gen.flush();
 String result = gen.getOutputContent();
 assertEquals("\"+" + value.getClass().getName() + "\"", result);
 }
}