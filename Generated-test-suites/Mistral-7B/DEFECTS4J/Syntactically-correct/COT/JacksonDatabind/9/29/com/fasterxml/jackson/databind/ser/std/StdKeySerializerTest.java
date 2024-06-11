import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 Date date = new Date();
 SimpleObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
 JsonGenerator jgen = writer.getGenerator();
 serializer.serialize(date, jgen, new SerializerProvider());
 ObjectNode jsonNode = writer.writeValueAsTree(date);
 String expected = "\"$date\""; // Quotation marks are important here
 String actual = jsonNode.get("$id").getTextValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 String value = "test";
 SimpleObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
 JsonGenerator jgen = writer.getGenerator();
 serializer.serialize(value, jgen, new SerializerProvider());
 ObjectNode jsonNode = writer.writeValueAsTree(value);
 String expected = "\"" + value + "\"";
 String actual = jsonNode.get("$id").getTextValue();
 assertEquals(expected, actual);
 }

 @Test (expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
 JsonGenerator jgen = writer.getGenerator();
 serializer.serialize(null, jgen, new SerializerProvider());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonSchema schema = serializer.getSchema(new SerializerProvider(), Object.class);
 String expected = "string";
 String actual = schema.getType().getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = new JsonDeserializer.StdJsonFormatVisitor(null);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 String expected = "string";
 String actual = visitor.getFormatName();
 assertEquals(expected, actual);
 }

}