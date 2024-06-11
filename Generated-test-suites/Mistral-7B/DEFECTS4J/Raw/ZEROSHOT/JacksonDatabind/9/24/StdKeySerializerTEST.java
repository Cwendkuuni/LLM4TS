import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();

 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, null);
 generator.flush();

 String output = System.out.getStackTrace().toString().split("at")[1].split("\\(")[0];
 assertEquals("\"$date\"", output);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String value = "testString";
 StdKeySerializer serializer = new StdKeySerializer();

 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(value, generator, null);
 generator.flush();

 String output = System.out.getStackTrace().toString().split("at")[1].split("\\(")[0];
 assertEquals("\"testString\"", output);
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);
 ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), typeHint);

 assertTrue(schemaNode.isTextual());
 assertEquals("string", schemaNode.getTextValue());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);
 ObjectNode rootNode = mapper.createObjectNode();
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getRootValueSchema(typeHint);

 serializer.acceptJsonFormatVisitor(visitor, typeHint);

 JsonNode schemaNode = visitor.getSchema();

 assertTrue(schemaNode.isObject());
 assertEquals("string", schemaNode.path("$id").getTextValue());
 assertTrue(schemaNode.has("title"));
 assertEquals("Key serializer for Java objects", schemaNode.path("title").getTextValue());
 }
}