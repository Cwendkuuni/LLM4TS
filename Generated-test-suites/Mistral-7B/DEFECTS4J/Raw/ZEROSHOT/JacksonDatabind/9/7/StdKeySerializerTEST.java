import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, new SerializerProvider.StandardProvider());
 generator.flush();
 // Verify the output is the expected ISO 8601 date format
 assertEquals("\"2022-12-31T00:00:00.000Z\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 Object key = "testKey";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(key, generator, new SerializerProvider.StandardProvider());
 generator.flush();
 // Verify the output is the string value
 assertEquals("\"testKey\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, generator, new SerializerProvider.StandardProvider());
 generator.flush();
 // Verify the output is empty string
 assertEquals("\"\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectNode schema = serializer.getSchema(new SerializerProvider.StandardProvider(), JavaType.getCollectionType(Map.class, String.class));
 assertEquals("string", schema.get("type").asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializers().findValueSerializer(String.class);
 serializer.acceptJsonFormatVisitor(visitor, JavaType.getCollectionType(Map.class, String.class));
 assertEquals(JsonFormat.Value.stringFormat(""), visitor.getFormat());
 }
}