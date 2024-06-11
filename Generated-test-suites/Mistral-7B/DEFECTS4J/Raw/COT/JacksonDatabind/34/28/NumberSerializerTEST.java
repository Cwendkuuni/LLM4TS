import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest extends SerializerTestBase {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeInteger() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(Integer.class);
 Integer number = 123;
 String expected = "123";
 String actual = mapSerializeValue(serializer, number);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(Long.class);
 Long number = 123L;
 String expected = "123";
 String actual = mapSerializeValue(serializer, number);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeFloat() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(Float.class);
 Float number = 123.45f;
 String expected = "123.45";
 String actual = mapSerializeValue(serializer, number);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(Double.class);
 Double number = 123.45;
 String expected = "123.45";
 String actual = mapSerializeValue(serializer, number);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigDecimal() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
 BigDecimal number = new BigDecimal("123.45");
 String expected = "123.45";
 String actual = mapSerializeValue(serializer, number);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(BigInteger.class);
 BigInteger number = new BigInteger("123");
 String expected = "123";
 String actual = mapSerializeValue(serializer, number);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNonNumberType() throws IOException, JsonProcessingException {
 NumberSerializer serializer = new NumberSerializer(String.class);
 String number = "abc";
 mapSerializeValue(serializer, number);
 fail("Expected JsonProcessingException but it did not occur.");
 }

 @Test
 public void testDeserializeInteger() throws IOException, JsonProcessingException {
 Number deserializedNumber = mapParseValue("123", Number.class);
 assertEquals(123, deserializedNumber.intValue());
 }

 @Test
 public void testDeserializeLong() throws IOException, JsonProcessingException {
 Number deserializedNumber = mapParseValue("123", Long.class);
 assertEquals(123L, deserializedNumber.longValue());
 }

 @Test
 public void testDeserializeFloat() throws IOException, JsonProcessingException {
 Number deserializedNumber = mapParseValue("123.45", Float.class);
 assertEquals(123.45f, deserializedNumber.floatValue(), 0.001f);
 }

 @Test
 public void testDeserializeDouble() throws IOException, JsonProcessingException {
 Number deserializedNumber = mapParseValue("123.45", Double.class);
 assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);
 }

 @Test
 public void testDeserializeBigDecimal() throws IOException, JsonProcessingException {
 Number deserializedNumber = mapParseValue("123.45", BigDecimal.class);
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = (BigDecimal) deserializedNumber;
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeBigInteger() throws IOException, JsonProcessingException {
 Number deserializedNumber = mapParseValue("123", BigInteger.class);
 BigInteger expected = new BigInteger("123");
 BigInteger actual = (BigInteger) deserializedNumber;
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidNumberFormat() throws IOException, JsonProcessingException {
 mapParseValue("abc", Number.class);
 fail("Expected JsonMappingException but it did not occur.");
 }

 private String mapSerializeValue(NumberSerializer serializer, Number number) throws JsonProcessingException, IOException {
 StringWriter stringWriter = new StringWriter();
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
 serializer.serialize(number, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 return stringWriter.toString();
 }

 private Number mapParseValue(String jsonString, Class<? extends Number> clazz) throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 Number number = mapper.readValue(jsonString, clazz);
 return number;
 }
}