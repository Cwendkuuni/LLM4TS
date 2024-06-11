import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNodeType;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;
import java.util.Date;

public class StdKeySerializerTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = new JsonFactory(provider).createGenerator();

 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(date, jgen, provider);
 jgen.flush();
 String result = jgen.getOutputStr();

 assertEquals("\"\"", result); // Default serialization of Date
 }

 @Test
 public void testSerializeString() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = new JsonFactory(provider).createGenerator();

 String str = "test";
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(str, jgen, provider);
 jgen.flush();
 String result = jgen.getOutputStr();

 assertEquals("\"test\"", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 JsonGenerator jgen = new JsonFactory(provider).createGenerator();

 StdKeySerializer serializer = new StdKeySerializer();
 serializer.serialize(null, jgen, provider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 SerializerProvider provider = new SerializerProvider();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType javaType = JavaType.construct(JsonNodeType.STRING_TYPE);
 JsonNode expectedSchema = createSchemaNode("string");

 JsonNode result = serializer.getSchema(provider, javaType);
 assertEquals(expectedSchema, result);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFactory factory = new JsonFactory();
 JsonFormatVisitorWrapper visitor = factory.getPolymorphicSerializerProvider().getSerializerProvider().getAllSerializerProviders()
 .next().getActiveRootValueSerializerProvider().getSerializers().nextValueSerializer();

 StdKeySerializer serializer = new StdKeySerializer();
 serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(JsonNodeType.STRING_TYPE));
 }
}