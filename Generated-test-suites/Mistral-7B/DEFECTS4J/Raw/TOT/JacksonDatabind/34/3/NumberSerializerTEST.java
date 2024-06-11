import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
 String expectedJson = "\"123.45\"";
 ObjectReader reader = mapper.readerFor(String.class);
 JsonNode actualJson = reader.readTree(jsonGenerator.getOutputBuffer().toString());
 assertEquals(expectedJson, actualJson.asText());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
 String expectedJson = "123456789012345678901234567890";
 ObjectReader reader = mapper.readerFor(String.class);
 JsonNode actualJson = reader.readTree(jsonGenerator.getOutputBuffer().toString());
 assertEquals(expectedJson, actualJson.asText());
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException, JsonGenerationException {
 Integer integer = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(integer, jsonGenerator, mapper.getSerializerProvider());
 String expectedJson = "123";
 ObjectReader reader = mapper.readerFor(String.class);
 JsonNode actualJson = reader.readTree(jsonGenerator.getOutputBuffer().toString());
 assertEquals(expectedJson, actualJson.asText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeUnknownType() throws JsonProcessingException, JsonGenerationException {
 Object unknownType = new Object();
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(unknownType, jsonGenerator, mapper.getSerializerProvider());
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonProcessingException {
 String jsonString = "\"123.45\"";
 ObjectReader reader = mapper.readerFor(Number.class);
 Number deserialized = reader.readValue(jsonString);
 assertEquals(new BigDecimal("123.45"), deserialized);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonProcessingException {
 String jsonString = "123456789012345678901234567890";
 ObjectReader reader = mapper.readerFor(Number.class);
 Number deserialized = reader.readValue(jsonString);
 assertEquals(new BigInteger("123456789012345678901234567890"), deserialized);
 }

 @Test
 public void testDeserializeInteger() throws JsonProcessingException {
 String jsonString = "123";
 ObjectReader reader = mapper.readerFor(Number.class);
 Number deserialized = reader.readValue(jsonString);
 assertEquals(123, deserialized.intValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownType() throws JsonProcessingException {
 String jsonString = "unknownType";
 ObjectReader reader = mapper.readerFor(Number.class);
 reader.readValueAs(String.class, jsonString);
 }
}