package com.example.numberserializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper(JsonMapperFactory.builder()
         .addModule(new SimpleModule().addSerializer(Number.class, NumberSerializer.instance))
         .build());

 @Test
 public void testSerializeSmallInteger() throws JsonGenerationException, JsonMappingException {
     NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
     Number number = 1;
     JsonGenerator jsonGenerator = null;
     SerializerProvider serializerProvider = null;

     jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.File("output.json"));
     serializerProvider = objectMapper.getSerializerProvider();

     numberSerializer.serialize(number, jsonGenerator, serializerProvider);
     jsonGenerator.flush();
     jsonGenerator.close();

     JsonParser jsonParser = objectMapper.getFactory().createParser(new java.io.File("output.json"));

     assertEquals(Integer.toString(number.intValue()), jsonParser.getText());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
     NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
     Number number = new BigInteger("12345678901234567890");

     JsonGenerator jsonGenerator = null;
     SerializerProvider serializerProvider = null;

     jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.File("output.json"));
     serializerProvider = objectMapper.getSerializerProvider();

     numberSerializer.serialize(number, jsonGenerator, serializerProvider);
     jsonGenerator.flush();
     jsonGenerator.close();

     JsonParser jsonParser = objectMapper.getFactory().createParser(new java.io.File("output.json"));

     assertEquals(number.toString(), jsonParser.getText());
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
     NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
     Number number = new BigDecimal("123.456");

     JsonGenerator jsonGenerator = null;
     SerializerProvider serializerProvider = null;

     jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.File("output.json"));
     serializerProvider = objectMapper.getSerializerProvider();

     numberSerializer.serialize(number, jsonGenerator, serializerProvider);
     jsonGenerator.flush();
     jsonGenerator.close();

     JsonParser jsonParser = objectMapper.getFactory().createParser(new java.io.File("output.json"));

     assertEquals(number.toString(), jsonParser.getText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, JsonMappingException {
     NumberSerializer numberSerializer = new NumberSerializer(String.class);
     String str = "hello";

     JsonGenerator jsonGenerator = null;
     SerializerProvider serializerProvider = null;

     jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.File("output.json"));
     serializerProvider = objectMapper.getSerializerProvider();

     numberSerializer.serialize(str, jsonGenerator, serializerProvider);
 }

 @Test
 public void testDeserializeSmallInteger() throws JsonGenerationException, JsonMappingException {
     Number number = null;

     JsonParser jsonParser = objectMapper.getFactory().createParser(NumberSerializerTest.class.getResource("/input.json"));

     number = objectMapper.readValue(jsonParser, Number.class);

     assertEquals(1, number.intValue());
 }

 @Test
 public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException {
     Number number = null;

     JsonParser jsonParser = objectMapper.getFactory().createParser(NumberSerializerTest.class.getResource("/input_big_integer.json"));

     number = objectMapper.readValue(jsonParser, Number.class);

     assertEquals(new BigInteger("12345678901234567890"), number);
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException {
     Number number = null;

     JsonParser jsonParser = objectMapper.getFactory().createParser(NumberSerializerTest.class.getResource("/input_big_decimal.json"));

     number = objectMapper.readValue(jsonParser, Number.class);

     assertEquals(new BigDecimal("123.456"), number);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnsupportedType() throws JsonGenerationException, JsonMappingException {
     JsonParser jsonParser = objectMapper.getFactory().createParser(NumberSerializerTest.class.getResource("/input_string.json"));

     objectMapper.readValue(jsonParser, Number.class);
 }
}