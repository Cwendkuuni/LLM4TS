import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 private static final NumberSerializer numberSerializer = NumberSerializer.instance;

 @Test
 public void testSerialize_BigDecimal() throws IOException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider());
 String expectedJson = "\"123.45\"";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_BigInteger() throws IOException {
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(bigInteger, jsonGenerator, new SerializerProvider());
 String expectedJson = "12345678901234567890";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_Integer() throws IOException {
 Integer integer = 123;
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(integer, jsonGenerator, new SerializerProvider());
 String expectedJson = "123";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_Long() throws IOException {
 Long longValue = 1234567890123456789L;
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(longValue, jsonGenerator, new SerializerProvider());
 String expectedJson = "1234567890123456789";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_Double() throws IOException {
 Double doubleValue = 123.45;
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider());
 String expectedJson = "123.45";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_Float() throws IOException {
 Float floatValue = 123.45f;
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider());
 String expectedJson = "123.45"; // assuming JsonGenerator writes float as double
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_Byte() throws IOException {
 Byte byteValue = (byte) 123;
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(byteValue, jsonGenerator, new SerializerProvider());
 String expectedJson = "123";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test
 public void testSerialize_Short() throws IOException {
 Short shortValue = (short) 123;
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(shortValue, jsonGenerator, new SerializerProvider());
 String expectedJson = "123";
 assertEquals(expectedJson, jsonGenerator.getOutputText());
 }

 @Test(expected = IOException.class)
 public void testSerialize_Null() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 numberSerializer.serialize(null, jsonGenerator, new SerializerProvider());
 fail("Should throw IOException when trying to serialize null number");
 }

 @Test
 public void testGetSchema() {
 ObjectMapper objectMapper = new ObjectMapper();
 Schema schema = numberSerializer.getSchema(new SerializerProvider(), Number.class);
 String expectedSchema = "_number";
 assertEquals(expectedSchema, schema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor_IntegerFormat() {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.schemaForSerialization(Integer.class).acceptJsonFormatVisitor(new SerializerProvider());
 assertTrue(jsonFormatVisitorWrapper.isExpectedNumberFormat());
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitor_UnsupportedType() {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.schemaForSerialization(String.class).acceptJsonFormatVisitor(new SerializerProvider());
 fail("Should throw JsonMappingException when handling unsupported type");
 }

 private JsonGenerator createJsonGenerator() throws IOException {
 StringWriter writer = new StringWriter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer);
 return jsonGenerator;
 }

}