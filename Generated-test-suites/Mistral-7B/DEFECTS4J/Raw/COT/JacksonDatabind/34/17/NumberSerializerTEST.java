import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.serializer.StandardSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

 private final static NumberSerializer numberSerializer = NumberSerializer.instance;

 @Test
 public void testSerializeInteger() throws IOException {
 int value = 123;
 String expectedJson = "123";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeLong() throws IOException {
 long value = 1234567890123L;
 String expectedJson = "1234567890123";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeFloat() throws IOException {
 float value = 123.45f;
 String expectedJson = "123.45";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeDouble() throws IOException {
 double value = 123.456789;
 String expectedJson = "123.456789";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 BigInteger value = BigInteger.valueOf(1234567890123456789L);
 String expectedJson = "1234567890123456789";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeBigDecimal() throws IOException {
 BigDecimal value = new BigDecimal("1234567890123456789.123456");
 String expectedJson = "1234567890123456789.123456";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "123";
 String expectedJson = "123";

 String actualJson = serializeAndGetJson(value);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeNull() throws IOException {
 Number value = null;
 String expectedJson = null; // null is not a valid JSON representation
 String actualJson = serializeAndGetJson(value);
 Assert.assertNull(actualJson);
 }

 @Test
 public void testGetSchemaInteger() throws JsonMappingException, IOException {
 JsonNode expectedSchema = new IntNode(123);
 JsonNode actualSchema = getSchema();

 Assert.assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testGetSchemaBigDecimal() throws JsonMappingException, IOException {
 JsonNode expectedSchema = new TextNode("number");
 JsonNode actualSchema = getSchema();

 Assert.assertEquals(expectedSchema.asText(), actualSchema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
 // This test case is not comprehensive as it doesn't cover all possible scenarios
 // but it's sufficient to verify that the serializer is registered correctly
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {};
 acceptJsonFormatVisitor(visitor);
 }

 private String serializeAndGetJson(Number number) throws IOException {
 StringWriter stringWriter = new StringWriter();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
 numberSerializer.serialize(number, jsonGenerator, new DefaultSerializerProvider());
 jsonGenerator.close();
 return stringWriter.toString();
 }

 private JsonNode getSchema() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.readTree(new ByteArrayInputStream(objectMapper.writeValueAsBytes(numberSerializer)));
 }
}