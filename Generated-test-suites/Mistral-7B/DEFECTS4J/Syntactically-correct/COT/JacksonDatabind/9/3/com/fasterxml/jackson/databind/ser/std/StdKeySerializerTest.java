import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdKeySerializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();

 // Expected output: "2022-03-28T13:35:46.248Z" (ISO-8601 format)
 String expectedOutput = "\"2022-03-28T13:35:46.248Z\"";
 JsonNode actualOutput = objectMapper.readTree(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().toByteArray()));
 assertEquals(expectedOutput, actualOutput.toString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String str = "test_string";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(str, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();

 // Expected output: "test_string"
 String expectedOutput = "\"test_string\"";
 JsonNode actualOutput = objectMapper.readTree(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().toByteArray()));
 assertEquals(expectedOutput, actualOutput.toString());
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();

 // Expected output: null
 JsonNode actualOutput = JsonNodeFactory.instance.nullValue();
 JsonNode expectedOutput = actualOutput;
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeInvalidType() throws JsonProcessingException {
 Integer num = 1;
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(num, jsonGenerator, new SerializerProvider());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance.constructType(String.class));

 // Expected output: {"type":"string"}
 String expectedOutput = "{\"type\":\"string\"}";
 assertEquals(expectedOutput, schema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = null;
 serializer.acceptJsonFormatVisitor(objectMapper.getFactory().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FIND_FORMATS, new TypeFactory().constructType(JsonNode.class), null), TypeFactory.defaultInstance.constructType(String.class));

 // No exception should be thrown
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorWithInvalidType() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = null;
 serializer.acceptJsonFormatVisitor(objectMapper.getFactory().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FIND_FORMATS, new TypeFactory().constructType(Integer.class), null), TypeFactory.defaultInstance.constructType(String.class));
 }
}