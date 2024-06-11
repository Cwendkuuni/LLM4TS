package com.yourpackage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

 @Mock
 private JsonGenerator mockJsonGenerator;
 @Mock
 private SerializerProvider mockSerializerProvider;
 @Mock
 private JsonParser mockJsonParser;
 @Mock
 private TypeSerializer mockTypeSerializer;

 @InjectMocks
 private NumberSerializer numberSerializer;

 @Test
 public void testSerializeBigDecimal() throws IOException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 when(mockJsonGenerator.writeNumber(bigDecimal)).thenReturn(null);

 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 objectWriter.writeValueAsString(new Object() {
 @JsonSerialize(using = NumberSerializer.class)
 private Number number = bigDecimal;
 }, mockJsonGenerator);

 assertEquals("123.45", numberSerializer.serialize(bigDecimal, mockJsonGenerator, mockSerializerProvider).getValueAsString());
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 BigInteger bigInteger = new BigInteger("12345");
 when(mockJsonGenerator.writeNumber(bigInteger)).thenReturn(null);

 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 objectWriter.writeValueAsString(new Object() {
 @JsonSerialize(using = NumberSerializer.class)
 private Number number = bigInteger;
 }, mockJsonGenerator);

 assertEquals("12345", numberSerializer.serialize(bigInteger, mockJsonGenerator, mockSerializerProvider).getValueAsString());
 }

 @Test
 public void testSerializeInt() throws IOException {
 int intValue = 123;
 when(mockJsonGenerator.writeNumber(intValue)).thenReturn(null);

 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 objectWriter.writeValueAsString(new Object() {
 @JsonSerialize(using = NumberSerializer.class)
 private Number number = intValue;
 }, mockJsonGenerator);

 assertEquals("123", numberSerializer.serialize(intValue, mockJsonGenerator, mockSerializerProvider).getValueAsString());
 }

 @Test
 public void testSerializeDouble() throws IOException {
 double doubleValue = 123.45;
 when(mockJsonGenerator.writeNumber(doubleValue)).thenReturn(null);

 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 objectWriter.writeValueAsString(new Object() {
 @JsonSerialize(using = NumberSerializer.class)
 private Number number = doubleValue;
 }, mockJsonGenerator);

 assertEquals("123.45", numberSerializer.serialize(doubleValue, mockJsonGenerator, mockSerializerProvider).getValueAsString());
 }

 @Test
 public void testSerializeFloat() throws IOException {
 float floatValue = 123.45f;
 when(mockJsonGenerator.writeNumber(floatValue)).thenReturn(null);

 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 objectWriter.writeValueAsString(new Object() {
 @JsonSerialize(using = NumberSerializer.class)
 private Number number = floatValue;
 }, mockJsonGenerator);

 assertEquals("123.45", numberSerializer.serialize(floatValue, mockJsonGenerator, mockSerializerProvider).getValueAsString());
 }

 @Test
 public void testSerializeOtherNumberTypes() throws IOException {
 when(mockJsonGenerator.writeNumber((Object) null)).thenThrow(IllegalArgumentException.class);

 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 objectWriter.writeValueAsString(new Object() {
 @JsonSerialize(using = NumberSerializer.class)
 private Number number;
 }, mockJsonGenerator);

 Number number = new Long(123);
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new Short((short) 123);
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new Byte((byte) 123);
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new BigDecimal("123.45");
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123.45", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new BigInteger("12345");
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("12345", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new Double(123.45);
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123.45", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new Float(123.45f);
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123.45", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());

 number = new Integer(123);
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider).getValueAsString());
 }

 @Test
 public void testCreateContextualSerializerForBigDecimal() {
 JsonSerializer<?> serializer = numberSerializer.createContextual(mockSerializerProvider, null);
 assertEquals(ToStringSerializer.instance, serializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualSerializerForOtherNumberTypes() {
 numberSerializer.createContextual(mockSerializerProvider, null);
 }

 @Test
 public void testGetSchema() {
 JsonSchema schema = numberSerializer.getSchema(mockSerializerProvider, Number.class);
 assertEquals("number", schema.getType().getRawClass().getSimpleName());
 }

 @Test
 public void testAcceptJsonFormatVisitorForIntType() {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, JsonType.construct(Integer.class, null));
 // We cannot test the visitor implementation here, as it depends on Jackson's internal implementation
 }

 @Test
 public void testAcceptJsonFormatVisitorForBigDecimalType() {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, JsonType.construct(BigDecimal.class, null));
 // We cannot test the visitor implementation here, as it depends on Jackson's internal implementation
 }
}