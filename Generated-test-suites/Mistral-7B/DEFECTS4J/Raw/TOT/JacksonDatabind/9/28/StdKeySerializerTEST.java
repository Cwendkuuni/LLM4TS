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

 JsonGenerator jgen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(date, jgen, null);
 jgen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(jgen.getOutputBufferAsByteArray());
 JsonParser parser = mapper.getFactory().createParser(inputStream);
 ObjectNode rootNode = mapper.readValue(parser, ObjectNode.class);
 String key = rootNode.get("@class").textValue();
 assertEquals("java.util.Date", key);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String value = "testString";
 StdKeySerializer serializer = new StdKeySerializer();

 JsonGenerator jgen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(value, jgen, null);
 jgen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(jgen.getOutputBufferAsByteArray());
 JsonParser parser = mapper.getFactory().createParser(inputStream);
 ObjectNode rootNode = mapper.readValue(parser, ObjectNode.class);
 String key = rootNode.get("name").textValue();
 assertEquals("testString", key);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator jgen = mapper.getFactory().createGenerator(outputStream, null);
 serializer.serialize(null, jgen, null);
 jgen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 JsonParser parser = mapper.getFactory().createParser(inputStream);
 parser.nextToken(); // consume starting [{}]
 String key = parser.getText();
 fail("Expected JsonProcessingException but did not receive one");
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
 assertEquals("string", schema.get("type").textValue());
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FORMATTER_STRINGS, Object.class);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 }
}