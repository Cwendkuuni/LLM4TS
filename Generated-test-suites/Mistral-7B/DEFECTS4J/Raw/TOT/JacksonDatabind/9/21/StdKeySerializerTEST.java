import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class StdKeySerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(date, gen, mapper.getSerializerProvider());
 JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
 assertEquals("\"\"", node.toString()); // Quotes are added around date strings in JSON

 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 String expectedJson = "\"\""; // Quotes around date string
 JsonNode expectedNode = JsonNodeFactory.instance.textNode(expectedJson);
 JsonNode actualNode = node.path("0");
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String value = "test";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(value, gen, mapper.getSerializerProvider());
 JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
 JsonNode actualNode = node.get("0");
 JsonNode expectedNode = JsonNodeFactory.instance.textNode("test");
 assertEquals(expectedNode, actualNode);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(null, gen, mapper.getSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonStringOrDate() throws JsonProcessingException {
 Object value = new Object();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(value, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
 JsonNode expectedSchema = JsonNodeFactory.instance.objectNode();
 expectedSchema.put("type", "string");
 JsonNode actualSchema = schema;
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(SerializerProvider.StandardSerializers.STRING_FORMAT);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 }
}