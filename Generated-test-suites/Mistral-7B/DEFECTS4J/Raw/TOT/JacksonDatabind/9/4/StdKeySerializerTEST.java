package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link StdKeySerializer}
 */
public class StdKeySerializerTest extends BaseMapTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Before
 public void setup() throws JsonMappingException {
 MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 /**
 * Test serializing a Date object using default serialization
 */
 @Test
 public void testSerializeDate() throws JsonProcessingException, JsonGenerationException {
 Date date = new Date();
 JsonGenerator gen = MAPPER.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(date, gen, new SerializerProvider() {
 @Override
 public void defaultSerializeDateKey(Date date, JsonGenerator sg) throws IOException {
 sg.writeNumber(date.getTime());
 }
 });
 Map<String, Date> map = new HashMap<>();
 map.put("key", date);
 String json = MAPPER.writeValueAsString(map);
 assertEquals("{\"key\":\"{" + date.getTime() + "}\"}", json);
 }

 /**
 * Test serializing a String
 */
 @Test
 public void testSerializeString() throws JsonProcessingException, JsonGenerationException {
 String str = "test";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = MAPPER.getFactory().createGenerator(System.out);
 serializer.serialize(str, gen, new SerializerProvider());
 Map<String, String> map = new HashMap<>();
 map.put("key", str);
 String json = MAPPER.writeValueAsString(map);
 assertEquals("{\"key\":\"" + str + "\"}", json);
 }

 /**
 * Test serializing an invalid Object type
 */
 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidObjectType() throws JsonProcessingException, JsonGenerationException {
 Object obj = new Object();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = MAPPER.getFactory().createGenerator(System.out);
 serializer.serialize(obj, gen, new SerializerProvider());
 }

 /**
 * Test getSchema method
 */
 @Test
 public void testGetSchema() throws JsonMappingException {
 JavaType schemaType = MAPPER.getTypeFactory().constructType("map <string, string>");
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = serializer.getSchema(new SerializerProvider(), schemaType);
 assertEquals("string", schema.asText());
 }

 /**
 * Test acceptJsonFormatVisitor method
 */
 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = factory.createJsonFormatVisitor(null, null, serializer);
 JavaType mapType = mapper.getTypeFactory().constructType("map <string, String>");
 serializer.acceptJsonFormatVisitor(visitor, mapType);
 assertEquals("String serialization format", visitor.getFormatName());
 }
}