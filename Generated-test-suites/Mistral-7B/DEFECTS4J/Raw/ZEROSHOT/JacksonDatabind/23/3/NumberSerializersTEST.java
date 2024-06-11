package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer value = 123;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 int result = parser.readValueAs(int.class);
 assertEquals(value, result);
 }

 @Test
 public void testIntegerSerializerWithJsonFormat() throws Exception {
 Integer value = 123;
 JsonFormat intFormat = new JsonFormat.NumberFormat();
 intFormat.setShape(JsonFormat.Shape.INT);
 value.getClass().getDeclaredField("value").setAccessible(true);
 value.getClass().getSuperclass().getDeclaredField("numberType").setAccessible(true);
 value.getClass().getSuperclass().getDeclaredField("_schemaType").setAccessible(true);
 value.getClass().getSuperclass().getDeclaredField("_isInt").setAccessible(true);
 value.getClass().getSuperclass().getDeclaredField("_numberType").set(value, intFormat.getNumberType());
 value.getClass().getDeclaredField("_schemaType").set(value, "integer");
 value.getClass().getDeclaredField("_isInt").set(value, true);
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 int result = parser.readValueAs(int.class);
 assertEquals(value, result);
 }

 @Test
 public void testIntegerSerializerAcceptJsonFormatVisitor() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("123");
 JsonFormatVisitorWrapper visitor = parser.getCodec().getJsonParser().acceptJsonFormatVisitor(parser.getCodec(), null);
 JsonIntegerFormatVisitor intVisitor = visitor.expectIntegerFormat(null);
 assertTrue(intVisitor != null);
 intVisitor.numberType(JsonParser.NumberType.INT);
 }

 @Test
 public void testIntegerSerializerCreateContextual() throws Exception {
 Object testObject = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.INT)
 int intValue = 123;
 }};
 JsonSerializer<?> serializer = mapper.findValueSerializer(Integer.class, null);
 assertEquals(serializer.getClass(), IntegerSerializer.class);
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long value = 123L;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 long result = parser.readValueAs(long.class);
 assertEquals(value, result);
 }

 @Test
 public void testLongSerializerAcceptJsonFormatVisitor() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("123");
 JsonFormatVisitorWrapper visitor = parser.getCodec().getJsonParser().acceptJsonFormatVisitor(parser.getCodec(), null);
 JsonNumberFormatVisitor longVisitor = visitor.expectNumberFormat(null);
 assertTrue(longVisitor != null);
 longVisitor.numberType(JsonParser.NumberType.LONG);
 }

 @Test
 public void testLongSerializerCreateContextual() throws Exception {
 Object testObject = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 long longValue = 123L;
 }};
 JsonSerializer<?> serializer = mapper.findValueSerializer(Long.class, null);
 assertFalse(serializer.getClass(), LongSerializer.class);
 }

 @Test
 public void testShortSerializer() throws Exception {
 Short value = (short) 123;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 short result = parser.readValueAs(short.class);
 assertEquals(value, result);
 }

 @Test
 public void testShortSerializerAcceptJsonFormatVisitor() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("123");
 JsonFormatVisitorWrapper visitor = parser.getCodec().getJsonParser().acceptJsonFormatVisitor(parser.getCodec(), null);
 JsonIntegerFormatVisitor shortVisitor = visitor.expectIntegerFormat(null);
 assertTrue(shortVisitor != null);
 shortVisitor.numberType(JsonParser.NumberType.INT);
 }

 @Test
 public void testShortSerializerCreateContextual() throws Exception {
 Object testObject = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.INT)
 short shortValue = 123;
 }};
 JsonSerializer<?> serializer = mapper.findValueSerializer(Short.class, null);
 assertEquals(serializer.getClass(), ShortSerializer.class);
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float value = 123.45f;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 float result = parser.readValueAs(float.class);
 assertEquals(value, result, 0.001f);
 }

 @Test
 public void testFloatSerializerAcceptJsonFormatVisitor() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("123.45");
 JsonFormatVisitorWrapper visitor = parser.getCodec().getJsonParser().acceptJsonFormatVisitor(parser.getCodec(), null);
 JsonNumberFormatVisitor floatVisitor = visitor.expectNumberFormat(null);
 assertTrue(floatVisitor != null);
 floatVisitor.numberType(JsonParser.NumberType.FLOAT);
 }

 @Test
 public void testFloatSerializerCreateContextual() throws Exception {
 Object testObject = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.FLOAT)
 float floatValue = 123.45f;
 }};
 JsonSerializer<?> serializer = mapper.findValueSerializer(Float.class, null);
 assertEquals(serializer.getClass(), FloatSerializer.class);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 Double value = 123.45;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 double result = parser.readValueAs(double.class);
 assertEquals(value, result, 0.00001);
 }

 @Test
 public void testDoubleSerializerAcceptJsonFormatVisitor() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("123.45");
 JsonFormatVisitorWrapper visitor = parser.getCodec().getJsonParser().acceptJsonFormatVisitor(parser.getCodec(), null);
 JsonNumberFormatVisitor doubleVisitor = visitor.expectNumberFormat(null);
 assertTrue(doubleVisitor != null);
 doubleVisitor.numberType(JsonParser.NumberType.DOUBLE);
 }

 @Test
 public void testDoubleSerializerCreateContextual() throws Exception {
 Object testObject = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 double doubleValue = 123.45;
 }};
 JsonSerializer<?> serializer = mapper.findValueSerializer(Double.class, null);
 assertEquals(serializer.getClass(), DoubleSerializer.class);
 }
}