import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider provider = mapper.getSerializerProvider();
 serializer.serialize(date, generator, provider);
 ObjectNode result = mapper.readTree(generator.getOutputStream());
 assertEquals("\"date\"", result.get("@class"), result.toString());
 assertEquals("\"date\"", result.get("name").asText());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String str = "test";
 StdKeySerializer serializer = new StdKeySerializer();

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider provider = mapper.getSerializerProvider();
 serializer.serialize(str, generator, provider);
 ObjectNode result = mapper.readTree(generator.getOutputStream());
 assertEquals("\"test\"", result.get("name").asText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider provider = mapper.getSerializerProvider();
 serializer.serialize(null, generator, provider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(Map.class);

 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), typeHint);
 assertEquals("string", schema.get("type").asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(Map.class.getName());

 ObjectNode rootNode = mapper.createObjectNode();
 JsonFormatVisitorWrapper visitor = mapper.schemaGenerator().findPropertySchema(rootNode, "test", typeHint);
 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 assertEquals("string", visitor.getSchemaName());
 }
}