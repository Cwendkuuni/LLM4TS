import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, bigDecimal);
 String json = generator.getOutputStr();
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 BigInteger bigInteger = new BigInteger("1234567890");
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, bigInteger);
 String json = generator.getOutputStr();
 assertEquals("1234567890", json);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 Long longValue = 123L;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, longValue);
 String json = generator.getOutputStr();
 assertEquals("123", json);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 Float floatValue = 123F;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, floatValue);
 String json = generator.getOutputStr();
 assertEquals("123.0", json);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 Double doubleValue = 123.45;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, doubleValue);
 String json = generator.getOutputStr();
 assertEquals("123.45", json);
 }

 @Test
 public void testSerializeInt() throws JsonProcessingException {
 Integer intValue = 123;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, intValue);
 String json = generator.getOutputStr();
 assertEquals("123", json);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 Byte byteValue = (byte) 123;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, byteValue);
 String json = generator.getOutputStr();
 assertEquals("123", json);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 Short shortValue = (short) 123;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 mapper.writeValue(generator, shortValue);
 String json = generator.getOutputStr();
 assertEquals("123", json);
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializeNull() throws JsonProcessingException {
 Number nullNumber = null;
 JsonGenerator generator = mapper.getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 try {
 mapper.writeValue(generator, nullNumber);
 } catch (JsonGenerationException e) {
 // expected: JsonGenerationException is thrown, but IllegalArgumentException should be the actual exception
 JsonGenerator g = (JsonGenerator) e.getOriginalSource();
 fail("Expected NumberSerializer to throw IllegalArgumentException but it threw JsonGenerationException.");
 }
 fail("Expected NullPointerException but got IllegalStateException.");
 }

 @Test
 public void testDeserializeBigDecimal() throws IOException, JsonProcessingException {
 String json = "123.456";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals(new BigDecimal("123.456"), readValue);
 }

 @Test
 public void testDeserializeBigInteger() throws IOException, JsonProcessingException {
 String json = "1234567890";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals(new BigInteger("1234567890"), readValue);
 }

 @Test
 public void testDeserializeLong() throws IOException, JsonProcessingException {
 String json = "123";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals(123L, readValue);
 }

 @Test
 public void testDeserializeFloat() throws IOException, JsonProcessingException {
 String json = "123.0";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals(123F, readValue);
 }

 @Test
 public void testDeserializeDouble() throws IOException, JsonProcessingException {
 String json = "123.45";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals(123.45, readValue, 0.001);
 }

 @Test
 public void testDeserializeInt() throws IOException, JsonProcessingException {
 String json = "123";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals(123, readValue);
 }

 @Test
 public void testDeserializeByte() throws IOException, JsonProcessingException {
 String json = "123";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals((byte) 123, readValue);
 }

 @Test
 public void testDeserializeShort() throws IOException, JsonProcessingException {
 String json = "123";
 Object readValue = mapper.readValue(json, Number.class);
 assertEquals((short) 123, readValue);
 }
}