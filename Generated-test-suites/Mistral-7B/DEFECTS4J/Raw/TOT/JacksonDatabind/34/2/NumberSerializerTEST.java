import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(bigDecimal);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(bigDecimal, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 BigInteger bigInteger = new BigInteger("9876543210987654321");
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(bigInteger);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(bigInteger, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeInt() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 int intValue = 123;
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(intValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(intValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 long longValue = 9876543210987654321L;
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(longValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(longValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 double doubleValue = 123.456;
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(doubleValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(doubleValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 float floatValue = 123.456f;
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(floatValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(floatValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 byte byteValue = 127;
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(byteValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(byteValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 short shortValue = 127;
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(shortValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(shortValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 String stringValue = "123.456";
 JsonNode expectedJsonNode = objectWriter.writeValueAsTree(stringValue);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(stringValue, jsonGenerator, new DefaultSerializerProvider());
 JsonNode actualJsonNode = jsonGenerator.getOutputContent();

 assertEquals(expectedJsonNode.toString(), actualJsonNode.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNull() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 numberSerializer.serialize(null, jsonGenerator, new DefaultSerializerProvider());
 }
}