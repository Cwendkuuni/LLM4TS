import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

 private static final ObjectMapper MAPPER = JsonMapperFactory.getMapper();

 @Test
 public void testSerializeDate() throws Exception {
 Object unterMinuteDate = new Date(System.currentTimeMillis() - 1000);
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = MAPPER.getSerializerProvider();
 JsonGenerator jgen = new TestJsonGenerator();

 serializer.serialize(unterMinuteDate, jgen, provider);
 jgen.flush();
 String serializedDate = jgen.getOutputBuffer().toString();

 assertTrue(serializedDate.startsWith("\"\"") && serializedDate.endsWith("\":null")); // Check for empty string as key for Date
 }

 @Test
 public void testSerializeString() throws Exception {
 ObjectNode testNode = MAPPER.createObjectNode();
 testNode.put("key", "value");
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = MAPPER.getSerializerProvider();
 JsonGenerator jgen = new TestJsonGenerator();

 serializer.serialize("test", jgen, provider);
 jgen.flush();
 String serializedString = jgen.getOutputBuffer().toString();

 assertEquals("{\"key\":\"test\"}", serializedString);
 }

 @Test
 public void testGetSchema() throws Exception {
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectNode schema = serializer.getSchema(MAPPER.getSerializerProvider(), Object.class);

 assertEquals("string", schema.get("type").asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectNode schema = MAPPER.createObjectNode();
 JsonFormatVisitorWrapper visitor = MAPPER.acceptJsonFormatVisitor(schema);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);

 assertEquals("string", schema.get("type").asText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNull() throws Exception {
 ObjectNode testNode = MAPPER.createObjectNode();
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = MAPPER.getSerializerProvider();
 JsonGenerator jgen = new TestJsonGenerator();

 serializer.serialize(null, jgen, provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonSerializableObject() throws Exception {
 Object testObject = new Object() {
 @Override
 public String toString() {
 throw new RuntimeException("toString() method not implemented");
 }
 };
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = MAPPER.getSerializerProvider();
 JsonGenerator jgen = new TestJsonGenerator();

 serializer.serialize(testObject, jgen, provider);
 }

}

 class TestJsonGenerator extends JsonGenerator {
 @Override
 public void writeFieldName(String fieldName) throws IOException {
 super.writeStartField(fieldName);
 }

 @Override
 public void writeStartObject() throws IOException {
 super.writeStartObject();
 }

 @Override
 public void writeStartArray() throws IOException {
 super.writeStartArray();
 }

 @Override
 public void writeEndObject() throws IOException {
 super.writeEndObject();
 }

 @Override
 public void writeEndArray() throws IOException {
 super.writeEndArray();
 }

 @Override
 public void flush() throws IOException {
 super.flush();
 }
 }