import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerUtil;

public class StdKeySerializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerialize_NullValue() throws JsonGenerationException, JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 Object nullValue = null;

 SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(System.out, objectMapper.getSerializationConfig());

 serializer.serialize(nullValue, jgen, provider);
 jgen.close();

 assertTrue(provider.isEnabled(SerializerProvider.Feature.AUTO_CLOSE_TARGET));
 }

 @Test
 public void testSerialize_DateValue() throws JsonGenerationException, JsonMappingException {
 Date dateValue = new Date();
 StdKeySerializer serializer = new StdKeySerializer();

 SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(System.out, objectMapper.getSerializationConfig());

 serializer.serialize(dateValue, jgen, provider);
 jgen.close();

 assertTrue(provider.isEnabled(SerializerProvider.Feature.AUTO_CLOSE_TARGET));
 }

 @Test
 public void testSerialize_StringValue() throws JsonGenerationException, JsonMappingException {
 String stringValue = "test";
 StdKeySerializer serializer = new StdKeySerializer();

 SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(System.out, objectMapper.getSerializationConfig());

 serializer.serialize(stringValue, jgen, provider);
 jgen.close();

 assertTrue(provider.isEnabled(SerializerProvider.Feature.AUTO_CLOSE_TARGET));
 }

 @Test(expected = IOException.class)
 public void testSerialize_IOException() throws JsonGenerationException, JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 IOException ioException = new IOException("Test IOException");
 Object ioExceptionValue = ioException;

 SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(System.out, objectMapper.getSerializationConfig());

 try {
 serializer.serialize(ioExceptionValue, jgen, provider);
 } catch (IOException e) {
 fail("Expected IOException, but got: " + e.getMessage());
 }

 jgen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerialize_JsonMappingException() throws JsonGenerationException, JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");
 Object jsonMappingExceptionValue = jsonMappingException;

 SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(System.out, objectMapper.getSerializationConfig());

 try {
 serializer.serialize(jsonMappingExceptionValue, jgen, provider);
 } catch (JsonGenerationException e) {
 fail("Expected JsonMappingException, but got: " + e.getMessage());
 }

 jgen.close();
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();

 JsonNode schema = serializer.getSchema(objectMapper.getSerializationConfig().getDefaultSerializerProvider(), Object.class);
 assertEquals("string", schema.get("type").getTextValue());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType javaType = TypeSerializerUtil.constructType(JsonNode.class, "string");

 JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().getDefaultSerializerProvider().getSerializers().findValueSerializer(javaType);
 serializer.acceptJsonFormatVisitor(visitor, javaType);
 }
}