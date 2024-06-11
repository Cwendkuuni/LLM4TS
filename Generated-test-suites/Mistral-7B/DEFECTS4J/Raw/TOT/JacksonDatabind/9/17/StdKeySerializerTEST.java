import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 Date date = new Date();

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider provider = mapper.getSerializerProvider();
 provider.setDefaultPrettyPrinter(new DefaultPrettyPrinter());

 serializer.serialize(date, gen, provider);
 String result = gen.getOutputContentAsString();
 String expected = "\"\""; // Empty string as per JSON.stringify(new Date())

 assertEquals(expected, result);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 String value = "test";

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider provider = mapper.getSerializerProvider();

 serializer.serialize(value, gen, provider);
 String result = gen.getOutputContentAsString();
 String expected = "\"test\"";

 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 Object value = null;

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider provider = mapper.getSerializerProvider();

 serializer.serialize(value, gen, provider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(String.class);

 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), typeHint);
 String expected = "{\"type\":\"string\"}";

 assertEquals(expected, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(String.class);

 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(SerializerProvider.Std, typeHint);
 serializer.acceptJsonFormatVisitor(visitor, typeHint);

 assertTrue(visitor.isAccepted());
 }
}