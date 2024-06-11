package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testSerializeBigDecimal() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonGenerator.setCodec(objectMapper.getSerializerProvider().findValueSerializer(BigDecimal.class));

 BigDecimal bigDecimal = new BigDecimal("123.456");
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("123.456", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeBigInteger() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonGenerator.setCodec(objectMapper.getSerializerProvider().findValueSerializer(BigInteger.class));

 BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("123456789012345678901234567890", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeLong() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 long longValue = 1234567890L;
 numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("1234567890", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeDouble() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 double doubleValue = 123.456;
 numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("123.456", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeFloat() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 float floatValue = 123.45f;
 numberSerializer.serialize(floatValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("123.45", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeInteger() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 int integerValue = 123;
 numberSerializer.serialize(integerValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("123", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeByte() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 byte byteValue = 127;
 numberSerializer.serialize(byteValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("127", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializeShort() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 short shortValue = 127;
 numberSerializer.serialize(shortValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 JsonParser jsonParser = new JsonFactory().createParser(System.in);
 assertEquals("127", jsonParser.getValueAsString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnsupportedType() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

 String stringValue = "test";
 numberSerializer.serialize(stringValue, jsonGenerator, objectMapper.getSerializerProvider());
 }
}