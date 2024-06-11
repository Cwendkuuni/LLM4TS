import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public class NumberSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 // Test serializing different types of Numbers
 Number number1 = 1;
 Number number2 = 1L;
 Number number3 = 1.0f;
 Number number4 = 1.0;
 Number number5 = new BigDecimal("1.23");
 Number number6 = new BigDecimal("12345678901234567890");
 Number number7 = new BigInteger("12345678901234567890");

 SerializerProvider provider = MAPper.getDefaultSerializerProvider();
 JsonGenerator jsonGenerator = provider.getFactory().createGenerator(System.out);
 numberSerializer.serialize(number1, jsonGenerator, provider);
 numberSerializer.serialize(number2, jsonGenerator, provider);
 numberSerializer.serialize(number3, jsonGenerator, provider);
 numberSerializer.serialize(number4, jsonGenerator, provider);
 numberSerializer.serialize(number5, jsonGenerator, provider);
 numberSerializer.serialize(number6, jsonGenerator, provider);
 numberSerializer.serialize(number7, jsonGenerator, provider);

 jsonGenerator.flush();
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("1.23");

 SerializerProvider provider = MAPper.getDefaultSerializerProvider();
 JsonGenerator jsonGenerator = provider.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigDecimal, jsonGenerator, provider);

 jsonGenerator.flush();
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");

 SerializerProvider provider = MAPper.getDefaultSerializerProvider();
 JsonGenerator jsonGenerator = provider.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigInteger, jsonGenerator, provider);

 jsonGenerator.flush();
 }

 @Test
 public void testSerializeOtherTypesOfNumbers() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 byte byteNumber = 1;
 short shortNumber = 1;
 double doubleNumber = 1.0;
 float floatNumber = 1.0f;

 SerializerProvider provider = MAPper.getDefaultSerializerProvider();
 JsonGenerator jsonGenerator = provider.getFactory().createGenerator(System.out);
 numberSerializer.serialize(byteNumber, jsonGenerator, provider);
 numberSerializer.serialize(shortNumber, jsonGenerator, provider);
 numberSerializer.serialize(doubleNumber, jsonGenerator, provider);
 numberSerializer.serialize(floatNumber, jsonGenerator, provider);

 jsonGenerator.flush();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringValue = "Not a Number";

 SerializerProvider provider = MAPper.getDefaultSerializerProvider();
 JsonGenerator jsonGenerator = provider.getFactory().createGenerator(System.out);

 try {
 numberSerializer.serialize(stringValue, jsonGenerator, provider);
 } catch (JsonMappingException e) {
 // expected
 }

 jsonGenerator.flush();
 }

 @Test
 public void testGetSchema() throws JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonNode schema = numberSerializer.getSchema(MAPper.getDefaultSerializerProvider(), Number.class);

 // Test schema for int
 assertTrue(schema.isInteger());
 // Test schema for float
 assertTrue(schema.isNumber());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 JsonFormatVisitorWrapper visitor = null; // Need to mock this to test properly
 // Test int schema
 numberSerializer.acceptJsonFormatVisitor(visitor, Integer.class.getType());
 // Test float schema
 numberSerializer.acceptJsonFormatVisitor(visitor, BigDecimal.class.getType());
 }
}