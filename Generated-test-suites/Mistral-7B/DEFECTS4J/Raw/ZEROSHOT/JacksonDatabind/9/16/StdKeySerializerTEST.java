import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

class StdKeySerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 JsonGenerator gen = MAPPER.getFactory()
 .createGenerator(new StringWriter());
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(date, gen, null);
 JsonParser parser = MAPPER.getFactory().createParser(gen.getOutputStream());
 ObjectNode node = parser.readValueAs(ObjectNode.class);
 assertEquals("\"@jsontype:java.util.Date\"", node.get("@class").asText());
 }

 @Test
 void testSerializeString() throws JsonProcessingException {
 String key = "testKey";
 JsonGenerator gen = MAPPER.getFactory()
 .createGenerator(new StringWriter());
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(key, gen, null);
 JsonParser parser = MAPPER.getFactory().createParser(gen.getOutputStream());
 ObjectNode node = parser.readValueAs(ObjectNode.class);
 assertEquals("testKey", node.get("@class").asText());
 assertEquals(key, node.get("testKey").asText());
 }

 @Test
 void testGetSchema() throws JsonProcessingException {
 JsonSchema schema = new StdKeySerializer().getSchema(MAPPER.getSerializerProvider(), Object.class);
 assertEquals("string", schema.describeConstantly().getSchemaName());
 }

 @Test
 void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(MAPPER.getSerializerProvider(), false);
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 assertEquals("STRING", visitor.getFormatName());
 }
}