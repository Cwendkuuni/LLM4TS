import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeInteger() throws IOException {
NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = 123;
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 String expectedOutput = "123";
 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializeLong() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = 9876543210L;
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 String expectedOutput = "9876543210";
 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializeFloat() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = 3.14f;
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 String expectedOutput = "3.14";
 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializeDouble() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = 123.456;
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 String expectedOutput = "123.456";
 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializeBigDecimal() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = new BigDecimal("123.456");
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 String expectedOutput = "123.456";
 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = new BigInteger("9876543210");
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 String expectedOutput = "9876543210";
 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullNumber() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(null, jsonGenerator, objectMapper.getSerializationConfig());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNonNumberType() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Number testNumber = new String("not a number");
 numberSerializer.serialize(testNumber, jsonGenerator, objectMapper.getSerializationConfig());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Schema schema = numberSerializer.getSchema(objectMapper.getSerializationConfig(), Number.class);
 String expectedType = _isInt ? "integer" : "number";
 String actualType = schema.getType().getRawClass().getSimpleName();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null; // assume this visitor supports integers
 numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorNonNumberType() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null; // assume this visitor doesn't support numbers
 numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
 }
}