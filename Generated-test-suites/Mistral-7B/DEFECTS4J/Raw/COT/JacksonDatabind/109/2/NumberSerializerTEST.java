import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws Exception {
 Number number = 123;
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
 mapper.writeValue(generator, number);
 // Verify the serialized number is correct
 JsonParser parser = mapper.getFactory().createParser(generator.asString());
 assertEquals(123, mapper.readValue(parser, Number.class));
 }

 @Test
 public void testSerializeBigDecimal() throws Exception {
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
 mapper.writeValue(generator, bigDecimal);
 // Verify the serialized number is correct
 JsonParser parser = mapper.getFactory().createParser(generator.asString());
 assertEquals(bigDecimal, mapper.readValue(parser, BigDecimal.class));
 }

 @Test
 public void testSerializeBigInteger() throws Exception {
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
 mapper.writeValue(generator, bigInteger);
 // Verify the serialized number is correct
 JsonParser parser = mapper.getFactory().createParser(generator.asString());
 assertEquals(bigInteger, mapper.readValue(parser, BigInteger.class));
 }

 @Test
 public void testSerializeLong() throws Exception {
 long longValue = 1234567890123456789L;
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
 mapper.writeValue(generator, longValue);
 // Verify the serialized number is correct
 JsonParser parser = mapper.getFactory().createParser(generator.asString());
 assertEquals(longValue, mapper.readValue(parser, Long.TYPE));
 }

 @Test
 public void testSerializeDouble() throws Exception {
 double doubleValue = 123.456;
 ObjectWriter objectWriter = mapper.writer();
 objectWriter.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
 String serializedDouble = objectWriter.writeValueAsString(doubleValue);
 JsonParser parser = mapper.getFactory().createParser(serializedDouble);
 assertEquals(doubleValue, mapper.readValue(parser, Double.TYPE));
 }

 @Test
 public void testSerializeFloat() throws Exception {
 float floatValue = 123.45f;
 ObjectWriter objectWriter = mapper.writer();
 objectWriter.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
 String serializedFloat = objectWriter.writeValueAsString(floatValue);
 JsonParser parser = mapper.getFactory().createParser(serializedFloat);
 assertEquals(floatValue, mapper.readValue(parser, Float.TYPE), 0.001f);
 }

 @Test
 public void testSerializeInteger() throws Exception {
 Integer integer = 123;
 ObjectWriter objectWriter = mapper.writer();
 objectWriter.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
 String serializedInteger = objectWriter.writeValueAsString(integer);
 JsonParser parser = mapper.getFactory().createParser(serializedInteger);
 assertEquals(integer, mapper.readValue(parser, Integer.TYPE));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidType() throws Exception {
 String invalidType = "invalidType";
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
 mapper.writeValue(generator, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNull() throws Exception {
 JsonParser parser = mapper.getFactory().createParser(mapper.getFactory().createGenerator().writeNumber(null).asString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyString() throws Exception {
 JsonParser parser = mapper.getFactory().createParser(" ");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidFormat() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("\"invalidFormat\"");
 }

 @Test
 public void testContextualSerialization() throws Exception {
 ObjectMapper mapperWithFormat = new ObjectMapper();
 JsonFormat.Shape shape = JsonFormat.Value.builderShape(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN).build();
 mapperWithFormat.setDefaultPropertyFormat(shape);
 ObjectWriter objectWriter = mapperWithFormat.writer();
 objectWriter.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
 String serializedBigDecimal = objectWriter.writeValueAsString(new BigDecimal("123.456"));
 JsonParser parser = mapperWithFormat.getFactory().createParser(serializedBigDecimal);
 assertEquals(new BigDecimal("123.456"), mapperWithFormat.readValue(parser, BigDecimal.class));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSchemaFactory().findSchemaVisitor(NumberSerializer.class, null);
 assertNotNull(visitor);
 }
}