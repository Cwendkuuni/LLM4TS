import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.fasterxml.jackson.databind.JsonSerializer.StandardSerializers.BigDecimalSerializer;
import static com.fasterxml.jackson.databind.JsonSerializer.StandardSerializers.BigIntegerSerializer;

public class NumberSerializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws JsonGenerationException, JsonMappingException, IOException {
 Number number = 10;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(number, generator, null);
 assertEquals("10", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
 BigDecimal bigDecimal = new BigDecimal("123.456");
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(bigDecimal, generator, null);
 assertEquals("123.456", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
 BigInteger bigInteger = BigInteger.valueOf(987654321);
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(bigInteger, generator, null);
 assertEquals("987654321", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
 Long longNumber = 456L;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(longNumber, generator, null);
 assertEquals("456", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
 Double doubleNumber = 123.456;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(doubleNumber, generator, null);
 assertEquals("123.456", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
 Float floatNumber = 123.5f;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(floatNumber, generator, null);
 assertEquals("123.5", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException, IOException {
 Integer integerNumber = 123;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(integerNumber, generator, null);
 assertEquals("123", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, JsonMappingException, IOException {
 Byte byteNumber = (byte) 127;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(byteNumber, generator, null);
 assertEquals("127", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, JsonMappingException, IOException {
 Short shortNumber = (short) 32767;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(shortNumber, generator, null);
 assertEquals("32767", generator.getOutputBuffer().toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, JsonMappingException, IOException {
 String unsupportedType = "unsupportedType";
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializer(Number.class);

 serializer.serialize(unsupportedType, generator, null);
 }

 @Test
 public void testParseNumber() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("10");
 Number number = mapper.readValue(parser, Number.class);
 assertEquals(10, number.intValue());
 }

 @Test
 public void testParseBigDecimal() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("\"123.456\"");
 Number number = mapper.readValue(parser, Number.class);
 assertEquals(new BigDecimal("123.456"), number);
 }

 @Test
 public void testParseBigInteger() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("12345678901234567890");
 Number number = mapper.readValue(parser, Number.class);
 assertEquals(new BigInteger("12345678901234567890"), number);
 }

 @Test(expected = JsonMappingException.class)
 public void testParseUnsupportedType() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("\"unsupportedType\"");
 mapper.readValue(parser, Number.class);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 JsonSerializerProvider serializerProvider = mapper.getSerializerProvider();
 NumberSerializer serializer = NumberSerializer.instance;
 BeanProperty property = new BeanProperty("testProperty", Number.class);
 JsonFormat format = new JsonFormat();
 JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
 jsonFormatValue.setShape(JsonFormat.Shape.STRING);
 format.setFormatOverrides(Collections.singletonMap(property, jsonFormatValue));
 assertEquals(serializer, serializer.createContextual(serializerProvider, property));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 assertEquals("number", serializer.getSchema(null, Number.class).toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultSerializerProvider().getSerializerProvider().getJsonParser().getConfig().getFormatVisitor();
 visitor.expectNumberFormat(Number.class);
 serializer.acceptJsonFormatVisitor(visitor, Number.class);
 }
}