import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 Date date = new Date();

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), getSerializerProvider());
 serializer.serialize(date, jsonGenerator, new SerializerProvider().builder().build());
 JsonNode actual = jsonGenerator.getOutputContent();
 String expected = "\"%Y-%m-%dT%H:%M:%S.%LZ\""; // ISO 8601 date format

 assertTrue(actual.isTextual());
 assertEquals(expected, actual.asText());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 String value = "testValue";

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), getSerializerProvider());
 serializer.serialize(value, jsonGenerator, new SerializerProvider().builder().build());
 JsonNode actual = jsonGenerator.getOutputContent();
 String expected = "\"testValue\"";

 assertTrue(actual.isTextual());
 assertEquals(expected, actual.asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 Object value = null;

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), getSerializerProvider());
 serializer.serialize(value, jsonGenerator, new SerializerProvider().builder().build());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonStringOrDate() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 int value = 123;

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), getSerializerProvider());
 serializer.serialize(value, jsonGenerator, new SerializerProvider().builder().build());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = ObjectMapper.getTypeFactory().constructType(String.class);

 JsonNode actual = serializer.getSchema(objectMapper.getSerializerProvider(), typeHint);
 String expected = "{\"type\":\"string\"}";

 assertEquals(expected, actual.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = ObjectMapper.getTypeFactory().constructType(String.class);
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);

 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 }

 private SerializerProvider getSerializerProvider() {
 return new SerializerProvider.StdSerializerProvider();
 }

}