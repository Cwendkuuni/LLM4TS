import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, mapper.getSerializerProvider());
 generator.flush();
 // Expected output: "2022-12-31T12:34:56.789Z" (date format used by Jackson's default date serialization)
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String key = "testKey";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(key, generator, mapper.getSerializerProvider());
 generator.flush();
 // Expected output: "testKey"
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, generator, mapper.getSerializerProvider());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream());
 serializer.serialize(new Date(), generator, mapper.getSerializerProvider());
 generator.close(); // Intentionally not flushing the generator
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFactory factory = mapper.getFactory();
 JsonFormatVisitorWrapper visitor = factory.constructSchemaFactory().findSchema(Object.class.getName()).getFormatVisitor();
 serializer.acceptJsonFormatVisitor(visitor, mapper.constructType(Object.class));
 }
}