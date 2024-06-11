import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(date, gen, null);
 gen.flush();

 String expected = "\"2023-03-28T00:00:00.000Z\""; // actual date will depend on current time
 String actual = mapper.readerForUpdating(new ObjectNode(mapper.createObjectNode())).get("@class").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String key = "testKey";
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(key, gen, null);
 gen.flush();

 String expected = "\"" + key + "\"";
 String actual = mapper.readerForUpdating(new ObjectNode(mapper.createObjectNode())).get("@class").asText();
 assertEquals(expected, actual);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(null, gen, null);
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonNode schema = mapper.readValue("{\"type\":\"string\"}", JsonNode.class);
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode actual = serializer.getSchema(mapper.getSerializerProvider(), Object.class.getType());
 assertEquals(schema, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 JsonFormatVisitorWrapper visitor = mapper.schemaGenerator().findSchemaForValueType(Object.class);
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
 ObjectNode expected = mapper.createObjectNode();
 expected.put("type", "string");
 ObjectNode actual = rootNode;
 visitor.visitSerializerProperties(serializer, actual);
 assertEquals(expected, actual);
 }
}