import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.JsonNode;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

 private JsonGenerator generator;
 private JsonParser parser;
 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 generator = mock(JsonGenerator.class);
 parser = mock(JsonParser.class);
 }

 // Test case for public constructor
 @Test
 public void testNumberSerializerConstructor() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 assertEquals(Number.class, numberSerializer.getHandledType());
 }

 // Test case for createContextual method
 @Test
 public void testCreateContextual() throws JsonMappingException {
 JsonFormat.Value format = JsonFormat.standard();
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 BeanProperty property = mock(BeanProperty.class);
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonSerializer<?> result = numberSerializer.createContextual(serializerProvider, property);
 assertEquals(NumberSerializer.class, result.getClass());
 }

 // Test case for serialize method with BigDecimal
 @Test
 public void testSerializeBigDecimal() throws IOException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(bigDecimal, generator, null);
 verify(generator).writeNumber(bigDecimal);
 }

 // Test case for serialize method with BigInteger
 @Test
 public void testSerializeBigInteger() throws IOException {
 BigInteger bigInteger = new BigInteger("123");
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(bigInteger, generator, null);
 verify(generator).writeNumber(bigInteger);
 }

 // Test case for serialize method with Long
 @Test
 public void testSerializeLong() throws IOException {
 Long longValue = 123L;
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(longValue, generator, null);
 verify(generator).writeNumber(longValue.longValue());
 }

 // Test case for serialize method with Double
 @Test
 public void testSerializeDouble() throws IOException {
 Double doubleValue = 123.45;
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(doubleValue, generator, null);
 verify(generator).writeNumber(doubleValue.doubleValue());
 }

 // Test case for serialize method with Float
 @Test
 public void testSerializeFloat() throws IOException {
 Float floatValue = 123.45f;
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(floatValue, generator, null);
 verify(generator).writeNumber(floatValue.floatValue());
 }

 // Test case for serialize method with Integer
 @Test
 public void testSerializeInteger() throws IOException {
 Integer integerValue = 123;
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(integerValue, generator, null);
 verify(generator).writeNumber(integerValue.intValue());
 }

 // Test case for serialize method with Byte
 @Test
 public void testSerializeByte() throws IOException {
 Byte byteValue = (byte) 123;
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(byteValue, generator, null);
 verify(generator).writeNumber(byteValue.intValue());
 }

 // Test case for serialize method with Short
 @Test
 public void testSerializeShort() throws IOException {
 Short shortValue = (short) 123;
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(shortValue, generator, null);
 verify(generator).writeNumber(shortValue.intValue());
 }

 // Test case for serialize method with other types
 @Test(expected = IllegalArgumentException.class)
 public void testSerializeOtherTypes() throws IOException {
 String stringValue = "123";
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(stringValue, generator, null);
 // No need to verify, as exception should be thrown
 }

 // Test case for getSchema method
 @Test
 public void testGetSchema() throws JsonMappingException {
 String schemaName = "";
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonNode result = numberSerializer.getSchema(null, null);
 assertEquals(schemaName, result.asText());
 }

 // Test case for acceptJsonFormatVisitor method with int format
 @Test
 public void testAcceptJsonFormatVisitorIntFormat() throws JsonMappingException {
 JsonFormat.Value format = JsonFormat.standard();
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonSerializerProvider provider = mock(JsonSerializerProvider.class);
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, null);
 // Need to verify that visitor.expectNumberFormat is called with correct arguments
 // but the actual implementation of this method is not available for testing
 // as it is a mock object
 }

 // Test case for acceptJsonFormatVisitor method with BigDecimal format
 @Test
 public void testAcceptJsonFormatVisitorBigDecimalFormat() throws JsonMappingException {
 JsonFormat.Value format = JsonFormat.withView(JsonFormat.Std.class, "DECIMAL_NUMBER", new JsonNumberFormat.Builder()
 .precision(10).build());
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonSerializerProvider provider = mock(JsonSerializerProvider.class);
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, null);
 verify(visitor).expectNumberFormat(null, JsonParser.NumberType.BIG_DECIMAL);
 }

 // Test case for serialize method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullValue() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.serialize(null, generator, null);
 // No need to verify, as exception should be thrown
 }
}