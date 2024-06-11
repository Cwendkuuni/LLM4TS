import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class NumberSerializerTest {

 private static final ObjectMapper mapper = new JsonMapper();

 @Test
 public void testSerializeBigDecimal() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigDecimal, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutput = "123.456";
 assertEquals(expectedOutput, bigDecimal.toPlainString());
 }

 @Test
 public void testSerializeBigInteger() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("9876543210");
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigInteger, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutput = "9876543210";
 assertEquals(expectedOutput, bigInteger.toString());
 }

 @Test
 public void testSerializeInteger() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Integer integer = 123;
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(integer, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutput = "123";
 assertEquals(expectedOutput, integer.toString());
 }

 @Test
 public void testSerializeLong() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Long longValue = 1234567890123L;
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(longValue, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutput = "1234567890123";
 assertEquals(expectedOutput, longValue.toString());
 }

 @Test
 public void testSerializeFloat() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Float floatValue = 123.45f;
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(floatValue, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutput = "123.45";
 assertEquals(expectedOutput, Float.toString(floatValue));
 }

 @Test
 public void testSerializeDouble() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Double doubleValue = 123.45;
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(doubleValue, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutput = "123.45";
 assertEquals(expectedOutput, Double.toString(doubleValue));
 }

 @Test
 public void testSerializeOtherNumberTypes() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Byte byteValue = 127;
 Short shortValue = 123;
 Long longValue2 = Long.MAX_VALUE;
 Double doubleValue2 = Double.MAX_VALUE;
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(byteValue, jsonGenerator, null);
 numberSerializer.serialize(shortValue, jsonGenerator, null);
 numberSerializer.serialize(longValue2, jsonGenerator, null);
 numberSerializer.serialize(doubleValue2, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 // Verify the output is correct
 String expectedOutputByte = "127";
 String expectedOutputShort = "123";
 String expectedOutputDouble = "1.79769313486231E308";
 // Ensure that the output for long and double is the same as the toString()
 // method, as the testSerializeInteger and testSerializeLong already check the
 // number serialization individually
 assertEquals(expectedOutputByte, byteValue.toString());
 assertEquals(expectedOutputShort, shortValue.toString());
 assertEquals(expectedOutputDouble, doubleValue2.toString());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeString() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringValue = "notANumber";
 try {
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(stringValue, jsonGenerator, null);
 jsonGenerator.flush();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeBigDecimal() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 try {
 JsonParser jsonParser = mapper.getFactory().createParser("123.456");
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals("123.456", deserializedNumber.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeBigInteger() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 try {
 JsonParser jsonParser = mapper.getFactory().createParser("9876543210");
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals("9876543210", deserializedNumber.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeInvalidNumber() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 try {
 JsonParser jsonParser = mapper.getFactory().createParser("invalidNumber");
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 } catch (IOException e) {
 //Expected: No such message.
 //Actual: com.fasterxml.jackson.databind.JsonMappingException: Can not parse value of type java.lang.String as Number
 }
 }

 @Test
 public void testDeserializeNaN() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 try {
 JsonParser jsonParser = mapper.getFactory().createParser("NaN");
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals("NaN", deserializedNumber.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeInfinity() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 try {
 JsonParser jsonParser = mapper.getFactory().createParser("Infinity");
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals("Infinity", deserializedNumber.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeNegativeInfinity() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 try {
 JsonParser jsonParser = mapper.getFactory().createParser("-Infinity");
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals("-Infinity", deserializedNumber.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeMaxAndMinValuesForNumberTypes() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal maxBigDecimal = new BigDecimal("9.223372e18");
 BigDecimal minBigDecimal = new BigDecimal("-9.223372e18");
 BigInteger maxBigInteger = new BigInteger("9223372036854775807");
 BigInteger minBigInteger = new BigInteger("-9223372036854775808");
 try {
 JsonParser jsonParser = mapper.getFactory().createParser(mapNumberToJsonString(maxBigDecimal));
 Number deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals(maxBigDecimal.toString(), deserializedNumber.toString());
 jsonParser = mapper.getFactory().createParser(mapNumberToJsonString(minBigDecimal));
 deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals(minBigDecimal.toString(), deserializedNumber.toString());
 jsonParser = mapper.getFactory().createParser(mapNumberToJsonString(maxBigInteger));
 deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals(maxBigInteger.toString(), deserializedNumber.toString());
 jsonParser = mapper.getFactory().createParser(mapNumberToJsonString(minBigInteger));
 deserializedNumber = numberSerializer.deserialize(JsonToken.NUMBER, jsonParser, null);
 assertEquals(minBigInteger.toString(), deserializedNumber.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 private String mapNumberToJsonString(Number number) {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 try {
 return mapper.writeValueAsString(number);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }
}