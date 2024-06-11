package com.example.junit.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

/**
 * Test class for NumberSerializers.
 */
public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 /**
 * Test method for {@link NumberSerializers#addAll(Map)}.
 */
 @Test
 public void testAddAll() throws Exception {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(6, allDeserializers.size());
 }

 /**
 * Test method for {@link ShortSerializer#serialize(Object, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testShortSerializerSerialize() throws Exception {
 Object shortValue = Short.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 ShortSerializer shortSerializer = new ShortSerializer();
 shortSerializer.serialize(shortValue, gen, mapper.getSerializerProvider());

 assertEquals("-128", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link ShortSerializer#serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)}.
 */
 @Test
 public void testShortSerializerSerializeWithType() throws Exception {
 Object shortValue = Short.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 ShortSerializer shortSerializer = new ShortSerializer();
 shortSerializer.serializeWithType(shortValue, gen, mapper.getSerializerProvider(), typeSerializer);

 assertEquals("\"-128\"", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link IntegerSerializer#serialize(Object, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testIntegerSerializerSerialize() throws Exception {
 Object integerValue = Integer.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 integerSerializer.serialize(integerValue, gen, mapper.getSerializerProvider());

 assertEquals("-2147483648", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link IntegerSerializer#serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)}.
 */
 @Test
 public void testIntegerSerializerSerializeWithType() throws Exception {
 Object integerValue = Integer.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 integerSerializer.serializeWithType(integerValue, gen, mapper.getSerializerProvider(), typeSerializer);

 assertEquals("\"-2147483648\"", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link IntLikeSerializer#serialize(Object, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testIntLikeSerializerSerialize() throws Exception {
 Object intLikeValue = Byte.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 intLikeSerializer.serialize(intLikeValue, gen, mapper.getSerializerProvider());

 assertEquals("-128", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link IntLikeSerializer#serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)}.
 */
 @Test
 public void testIntLikeSerializerSerializeWithType() throws Exception {
 Object intLikeValue = Byte.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();intLikeSerializer.serializeWithType(intLikeValue, gen, mapper.getSerializerProvider(), typeSerializer);

 assertEquals("\"-128\"", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link LongSerializer#serialize(Object, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testLongSerializerSerialize() throws Exception {
 Object longValue = Long.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 LongSerializer longSerializer = new LongSerializer(Long.class);
 longSerializer.serialize(longValue, gen, mapper.getSerializerProvider());

 assertEquals("-9223372036854775808", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link LongSerializer#serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)}.
 */
 @Test
 public void testLongSerializerSerializeWithType() throws Exception {
 Object longValue = Long.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 LongSerializer longSerializer = new LongSerializer(Long.class);
 longSerializer.serializeWithType(longValue, gen, mapper.getSerializerProvider(), typeSerializer);

 assertEquals("\"-9223372036854775808\"", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link FloatSerializer#serialize(Object, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testFloatSerializerSerialize() throws Exception {
 Object floatValue = Float.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 FloatSerializer floatSerializer = new FloatSerializer();
 floatSerializer.serialize(floatValue, gen, mapper.getSerializerProvider());

 assertEquals("NaN", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link FloatSerializer#serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)}.
 */
 @Test
 public void testFloatSerializerSerializeWithType() throws Exception {
 Object floatValue = Float.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 FloatSerializer floatSerializer = new FloatSerializer();
 floatSerializer.serializeWithType(floatValue, gen, mapper.getSerializerProvider(), typeSerializer);

 assertEquals("\"NaN\"", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link DoubleSerializer#serialize(Object, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testDoubleSerializerSerialize() throws Exception {
 Object doubleValue = Double.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
 doubleSerializer.serialize(doubleValue, gen, mapper.getSerializerProvider());

 assertEquals("NaN", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link DoubleSerializer#serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)}.
 */
 @Test
 public void testDoubleSerializerSerializeWithType() throws Exception {
 Object doubleValue = Double.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
 doubleSerializer.serializeWithType(doubleValue, gen, mapper.getSerializerProvider(), typeSerializer);

 assertEquals("\"NaN\"", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link ShortSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with {@link JsonFormat}.
 */
 @Test
 public void testShortSerializerSerializeJsonFormat() throws Exception {
 Object shortValue = Short.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonFormat jsonFormat = new JsonFormat.Builder()
         .withShape(JsonFormat.Shape.STRING)
         .build();
 ShortSerializer shortSerializer = new ShortSerializer();
 mapper.writerWithView(shortSerializer.getClass())
         .writeValue(gen, shortValue);

 assertEquals("-128", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link IntegerSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with {@link JsonFormat}.
 */
 @Test
 public void testIntegerSerializerSerializeJsonFormat() throws Exception {
 Object integerValue = Integer.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonFormat jsonFormat = new JsonFormat.Builder()
         .withShape(JsonFormat.Shape.STRING)
         .build();
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 mapper.writerWithView(integerSerializer.getClass())
         .writeValue(gen, integerValue);

 assertEquals("-2147483648", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link IntLikeSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with {@link JsonFormat}.
 */
 @Test
 public void testIntLikeSerializerSerializeJsonFormat() throws Exception {
 Object intLikeValue = Byte.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonFormat jsonFormat = new JsonFormat.Builder()
         .withShape(JsonFormat.Shape.STRING)
         .build();
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 mapper.writerWithView(intLikeSerializer.getClass())
         .writeValue(gen, intLikeValue);

 assertEquals("-128", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link LongSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with {@link JsonFormat}.
 */
 @Test
 public void testLongSerializerSerializeJsonFormat() throws Exception {
 Object longValue = Long.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonFormat jsonFormat = new JsonFormat.Builder()
         .withShape(JsonFormat.Shape.STRING)
         .build();
 LongSerializer longSerializer = new LongSerializer(Long.class);
 mapper.writerWithView(longSerializer.getClass())
         .writeValue(gen, longValue);

 assertEquals("-9223372036854775808", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link FloatSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with {@link JsonFormat}.
 */
 @Test
 public void testFloatSerializerSerializeJsonFormat() throws Exception {
 Object floatValue = Float.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonFormat jsonFormat = new JsonFormat.Builder()
         .withShape(JsonFormat.Shape.STRING)
         .build();
 FloatSerializer floatSerializer = new FloatSerializer();
 mapper.writerWithView(floatSerializer.getClass())
         .writeValue(gen, floatValue);

 assertEquals("NaN", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link DoubleSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with {@link JsonFormat}.
 */
 @Test
 public void testDoubleSerializerSerializeJsonFormat() throws Exception {
 Object doubleValue = Double.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonFormat jsonFormat = new JsonFormat.Builder()
         .withShape(JsonFormat.Shape.STRING)
         .build();
 DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
 mapper.writerWithView(doubleSerializer.getClass())
         .writeValue(gen, doubleValue);

 assertEquals("NaN", gen.toString());
 gen.close();
 }

 /**
 * Test method for {@link ShortSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with exception.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testShortSerializerSerializeException() throws Exception {
 Object shortValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 ShortSerializer shortSerializer = new ShortSerializer();
 shortSerializer.serialize(shortValue, gen, mapper.getSerializerProvider());
 }

 /**
 * Test method for {@link IntegerSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with exception.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIntegerSerializerSerializeException() throws Exception {
 Object integerValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 integerSerializer.serialize(integerValue, gen, mapper.getSerializerProvider());
 }

 /**
 * Test method for {@link IntLikeSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with exception.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIntLikeSerializerSerializeException() throws Exception {
 Object intLikeValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 intLikeSerializer.serialize(intLikeValue, gen, mapper.getSerializerProvider());
 }

 /**
 * Test method for {@link LongSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with exception.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testLongSerializerSerializeException() throws Exception {
 Object longValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 LongSerializer longSerializer = new LongSerializer(Long.class);
 longSerializer.serialize(longValue, gen, mapper.getSerializerProvider());
 }

 /**
 * Test method for {@link FloatSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with exception.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testFloatSerializerSerializeException() throws Exception {
 Object floatValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 FloatSerializer floatSerializer = new FloatSerializer();
 floatSerializer.serialize(floatValue, gen, mapper.getSerializerProvider());
 }

 /**
 * Test method for {@link DoubleSerializer#serialize(Object, JsonGenerator, SerializerProvider)} with exception.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testDoubleSerializerSerializeException() throws Exception {
 Object doubleValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
 doubleSerializer.serialize(doubleValue, gen, mapper.getSerializerProvider());
 }
}