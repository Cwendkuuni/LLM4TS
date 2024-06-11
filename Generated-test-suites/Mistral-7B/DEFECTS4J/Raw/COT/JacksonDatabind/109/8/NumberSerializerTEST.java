import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.JsonNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NumberSerializer Tests")
public class NumberSerializerTest {

 private static final NumberSerializer NUMBER_SERIALIZER = new NumberSerializer(Number.class);

 @Test
 @DisplayName("Test serialize method with Integer")
 void testSerializeInteger() throws JsonProcessingException {
 Integer integer = 123;
 String actual = serializeAndReadBack(integer);
 assertEquals("123", actual);
 }

 @Test
 @DisplayName("Test serialize method with Long")
 void testSerializeLong() throws JsonProcessingException {
 Long longValue = 456L;
 String actual = serializeAndReadBack(longValue);
 assertEquals("456", actual);
 }

 @Test
 @DisplayName("Test serialize method with Float")
 void testSerializeFloat() throws JsonProcessingException {
 Float floatValue = 123.45f;
 String actual = serializeAndReadBack(floatValue);
 assertEquals("123.45", actual);
 }

 @Test
 @DisplayName("Test serialize method with Double")
 void testSerializeDouble() throws JsonProcessingException {
 Double doubleValue = 123.45;
 String actual = serializeAndReadBack(doubleValue);
 assertEquals("123.45", actual);
 }

 @Test
 @DisplayName("Test serialize method with Byte")
 void testSerializeByte() throws JsonProcessingException {
 Byte byteValue = 127;
 String actual = serializeAndReadBack(byteValue);
 assertEquals("127", actual);
 }

 @Test
 @DisplayName("Test serialize method with Short")
 void testSerializeShort() throws JsonProcessingException {
 Short shortValue = 123;
 String actual = serializeAndReadBack(shortValue);
 assertEquals("123", actual);
 }

 @Test
 @DisplayName("Test serialize method with BigInteger")
 void testSerializeBigInteger() throws JsonProcessingException {
 BigInteger bigInteger = BigInteger.valueOf(1234567890123L);
 String actual = serializeAndReadBack(bigInteger);
 assertEquals("1234567890123", actual);
 }

 @Test
 @DisplayName("Test serialize method with BigDecimal")
 void testSerializeBigDecimal() throws JsonProcessingException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 String actual = serializeAndReadBack(bigDecimal);
 assertEquals("123.45", actual);
 }

 @Test
 @DisplayName("Test serialize method with null value")
 void testSerializeNull() throws JsonProcessingException {
 String actual = serializeAndReadBack(null);
 assertNull(actual);
 }

 @Test
 @DisplayName("Test serialize method with an object of unsupported type")
 void testSerializeUnsupportedType() throws JsonProcessingException {
 String actual = serializeAndReadBack(new Object());
 assertNotEquals("", actual);
 }

 @Test
 @DisplayName("Test createContextual method with int format")
 void testCreateContextualIntFormat() throws JsonMappingException {
 JsonFormat intFormat = new JsonFormat(JsonFormat.Feature.WRITE_NUMBERS_AS_STRINGS);
 assertEquals(ToStringSerializer.instance, NUMBER_SERIALIZER.createContextual(new Object(), new BeanProperty("test"), intFormat.getClass()));
 }

 @Test
 @DisplayName("Test createContextual method with string format")
 void testCreateContextualStringFormat() throws JsonMappingException {
 JsonFormat stringFormat = new JsonFormat(JsonFormat.Feature.WRITE_NUMBER_WITH_COMMAS);
 assertEquals(NUMBER_SERIALIZER, NUMBER_SERIALIZER.createContextual(new Object(), new BeanProperty("test"), stringFormat.getClass()));
 }

 @Test
 @DisplayName("Test getSchema method")
 void testGetSchema() throws JsonMappingException {
 JsonNode schema = NUMBER_SERIALIZER.getSchema(new Object(), Number.class);
 assertEquals("number", schema.asText());
 }

 private String serializeAndReadBack(Object value) throws JsonProcessingException {
 StringWriter stringWriter = new StringWriter();
 JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(stringWriter);
 NUMBER_SERIALIZER.serialize(value, jsonGenerator, new ObjectMapper().getSerializationConfig());
 jsonGenerator.flush();
 jsonGenerator.close();
 return stringWriter.toString();
 }

}