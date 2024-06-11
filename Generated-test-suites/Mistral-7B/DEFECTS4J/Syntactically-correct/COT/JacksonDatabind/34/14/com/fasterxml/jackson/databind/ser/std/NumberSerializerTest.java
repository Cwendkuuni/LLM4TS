import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

 private ObjectMapper objectMapper = JsonMapperFactory.getInstance();

 @Test
 public void testSerializeNumber() throws JsonGenerationException, IOException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 // Test serializing different types of Numbers
 Number number1 = 1;
 Number number2 = 1L;
 Number number3 = 1.0;
 Number number4 = 1.0F;
 Number number5 = BigInteger.valueOf(1);
 Number number6 = BigDecimal.valueOf(1);
 Number number7 = new Byte((byte) 1);
 Number number8 = new Short((short) 1);
 Number number9 = new BigDecimal("1.1");
 Number number10 = new BigInteger("11");

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(number1, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number2, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number3, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number4, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number5, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number6, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number7, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number8, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number9, jsonGenerator, new SerializerProvider());
 numberSerializer.serialize(number10, jsonGenerator, new SerializerProvider());

 jsonGenerator.flush();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullNumber() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 numberSerializer.serialize(null, null, null);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, IOException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("1234567890");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, IOException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigInteger, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 }

 @Test(expected = IOException.class)
 public void testSerializeInvalidNumber() throws IOException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String invalidNumber = "abc";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 try {
 numberSerializer.serialize(new Object(), jsonGenerator, new SerializerProvider());
 } catch (JsonProcessingException e) {
 fail("Expected IOException but got JsonProcessingException");
 }
 numberSerializer.serialize(new Object(), jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 JsonNode schema = numberSerializer.getSchema(new SerializerProvider(), typeFactory.constructType(Number.class));
 assertEquals("number", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 JsonFormatVisitorWrapper visitor = objectMapper.getFactory().getJsonFormatVisitor(false, null, null);
 numberSerializer.acceptJsonFormatVisitor(visitor, typeFactory.constructType(Number.class));
 }
}