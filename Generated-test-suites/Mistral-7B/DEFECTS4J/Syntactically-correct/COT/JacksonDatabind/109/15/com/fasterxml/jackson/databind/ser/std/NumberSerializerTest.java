package com.example.numberserializer;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer.NullValueSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import junit.framework.Assert;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class NumberSerializerTest {

 private ObjectMapper objectMapper;
 private JsonParser parser;
 private JsonGenerator generator;
 private SerializerProvider provider;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 provider = objectMapper.getSerializerProvider();
 parser = Mockito.mock(JsonParser.class);
 generator = Mockito.mock(JsonGenerator.class);
 }

 @Test
 public void test_createContextual_returnsThis_whenFormatIsNull() {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JsonFormat<Number> format = null;
 assertEquals(serializer, serializer.createContextual(provider, null));
 }

 @Test
 public void test_createContextual_returnsThis_whenFormatShapeIsString() {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JsonFormat<Number> format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 assertNotNull(serializer.createContextual(provider, null));
 assertEquals(ToStringSerializer.instance, serializer.createContextual(provider, null));
 }

 @Test
 public void test_serialize_serializesBigDecimal() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 BigDecimal number = new BigDecimal("3.14");
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("3.14", serializedNumber);
 }

 @Test
 public void test_serialize_serializesBigInteger() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 BigInteger number = BigInteger.valueOf(5L);
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("5", serializedNumber);
 }

 @Test
 public void test_serialize_serializesLong() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 long number = 42L;
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("42", serializedNumber);
 }

 @Test
 public void test_serialize_serializesDouble() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 double number = 3.14;
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("3.14", serializedNumber);
 }

 @Test
 public void test_serialize_serializesFloat() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 float number = 3.14f;
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("3.14", serializedNumber);
 }

 @Test
 public void test_serialize_serializesInteger() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 int number = 42;
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("42", serializedNumber);
 }

 @Test
 public void test_serialize_serializesByte() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 byte number = 42;
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("42", serializedNumber);
 }

 @Test
 public void test_serialize_serializesShort() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 short number = 42;
 serializer.serialize(number, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("42", serializedNumber);
 }

 @Test
 public void test_serialize_fallsBackToString_whenInputIsNotSupportedNumberType() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 String numberString = "not a number";
 serializer.serialize(numberString, generator, provider);
 String serializedNumber = generator.getOutputBuffer().toString();
 Assert.assertEquals("\"not a number\"", serializedNumber);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_serialize_throwsException_whenInputIsNull() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 serializer.serialize(null, generator, provider);
 }

 @Test
 public void test_getSchema_returnsSchemaNodeForIntegerType() {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JsonNode schema = serializer.getSchema(provider, TypeFactory.defaultInstance.constructType(int.class));
 assertEquals("integer", schema.asText());
 }

 @Test
 public void test_getSchema_returnsSchemaNodeForNumberType() {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JsonNode schema = serializer.getSchema(provider, TypeFactory.defaultInstance.constructType(Number.class));
 assertEquals("number", schema.asText());
 }

 @Test
 public void test_acceptJsonFormatVisitor_visitsIntFormatForIntegerType() throws JsonMappingException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JavaType typeHint = TypeFactory.defaultInstance.constructType(int.class);
 serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), typeHint);
 }

 @Test
 public void test_acceptJsonFormatVisitor_visitsFloatFormatForBigDecimalType() throws JsonMappingException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 TypeFactory factory = TypeFactory.defaultInstance;
 JavaType typeHint = factory.constructType(BigDecimal.class);
 serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), typeHint);
 }

 @Test
 public void test_acceptJsonFormatVisitor_throwsExceptionWhenUnhandledType() throws JsonMappingException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JavaType typeHint = TypeFactory.defaultInstance.constructType(String.class);
 try {
 serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), typeHint);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void test_serialize_throwsNullPointerException_whenGeneratorIsNull() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 serializer.serialize(new BigDecimal("3.14"), null, provider);
 }

 @Test(expected = NullPointerException.class)
 public void test_serialize_throwsNullPointerException_whenProviderIsNull() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 serializer.serialize(new BigDecimal("3.14"), generator, null);
 }
}