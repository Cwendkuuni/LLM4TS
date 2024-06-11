package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

/**
 * Tests for {@link DateTimeSerializerBase} class.
 */
public class DateTimeSerializerBaseTest {

 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

 /**
 * Tests constructor with {@code null}, {@code null} and {@code null} arguments.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullArguments() {
 DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, null, null);
 }

 /**
 * Tests constructor with valid arguments.
 */
 @Test
 public void testConstructorValidArguments() {
 Class<Long> longType = Long.TYPE;
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(longType, true, null);
 }

 /**
 * Tests {@link #withFormat(Boolean, DateFormat)} method with {@code null} argument.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testWithFormatNullArgument() {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.TYPE, true, null);
 serializer.withFormat(Boolean.FALSE, null);
 }

 /**
 * Tests {@link #withFormat(Boolean, DateFormat)} method with valid arguments.
 */
 @Test
 public void testWithFormatValidArguments() throws JsonProcessingException {
 Class<Long> longType = Long.TYPE;
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(longType, true, null);
 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 DateTimeSerializerBase<Long> formattedSerializer = serializer.withFormat(Boolean.FALSE, format);
 ObjectWriter writer = new ObjectMapper().writerWithRootValue(SerializerProviders.baseInstance());
 String json = writer.writeValueAsString(new Date(System.currentTimeMillis()));
 JsonParser parser = new ObjectMapper().readerWithRootValue(JsonParser.Std.class).parse(json);
 long deserializedValue = formattedSerializer.deserialize(parser, Long.class);
 assertEquals(System.currentTimeMillis(), deserializedValue);
 }

 /**
 * Tests {@link #createContextual(SerializerProvider, BeanProperty)} method with {@code null} arguments.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateContextualNullArguments() throws JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.TYPE, true, null);
 serializer.createContextual(null, null);
 }

 /**
 * Tests {@link #createContextual(SerializerProvider, BeanProperty)} method with valid arguments.
 */
 @Test
 public void testCreateContextualValidArguments() throws JsonMappingException {
 Class<Long> longType = Long.TYPE;
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(longType, true, null);
 BeanProperty property = new BeanProperty("date", longType);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(Shape.STRING);
 format.setPattern("yyyy-MM-dd HH:mm:ss z");
 JsonFormat formatAnnotation = JsonFormat.value(format);
 property.addAnnotation(formatAnnotation);
 JsonSerializer<?> serializerAnnotated = serializer.createContextual(SerializerProviders.baseInstance(), property);
 assertNotSame(serializer, serializerAnnotated);
 }

 /**
 * Tests {@link #serialize(T, JsonGenerator, SerializerProvider)} method with {@code null} arguments.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullArguments() throws IOException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.TYPE, true, null);
 serializer.serialize(null, null, null);
 }

 /**
 * Tests {@link #serialize(T, JsonGenerator, SerializerProvider)} method with valid arguments and serialization as a timestamp.
 */
 @Test
 public void testSerializeTimestamp() throws IOException, JsonGenerationException {
 Class<Long> longType = Long.TYPE;longType.getDeclaredField("serialVersionUID");
 longType.setAccessible(true);
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(longType, true, null);
 JsonGenerator gen = new TestJsonGenerator();
 serializer.serialize(new Date(System.currentTimeMillis()), gen, SerializerProviders.baseInstance());
 assertTrue(gen.asStartArrayToken().isPopulated());
 assertTrue(gen.asNumberValue().isIntegralNumber());
 assertEquals(System.currentTimeMillis(), gen.asNumberValue().longValue());
 gen.close();
 }

 /**
 * Tests {@link #serialize(T, JsonGenerator, SerializerProvider)} method with valid arguments and serialization as a string.
 */
 @Test
 public void testSerializeString() throws IOException, JsonGenerationException {
 Class<Long> longType = Long.TYPE;longType.getDeclaredField("serialVersionUID");
 longType.setAccessible(true);
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(longType, false, null);
 JsonGenerator gen = new TestJsonGenerator();
 ObjectWriter writer = new ObjectMapper().writerWithRootValue(SerializerProviders.baseInstance());
 String json = writer.writeValueAsString(new Date(System.currentTimeMillis()));
 JsonParser parser = new ObjectMapper().readerWithRootValue(JsonParser.Std.class).parse(json);
 ObjectCodec codec = new ObjectMapper().codec();
 ObjectReader reader = codec.unbinding().findValueDeserializer(parser, Long.class);
 serializer.serialize(new Date(System.currentTimeMillis()), gen, SerializerProviders.baseInstance());
 String expectedJson = DATE_FORMAT.format(new Date(System.currentTimeMillis()));
 assertEquals(expectedJson, gen.getOutputContent().toString());
 gen.close();
 }

 /**
 * Tests {@link #acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} method with valid arguments and serialization as a number.
 */
 @Test
 public void testAcceptJsonFormatVisitorNumeric() throws JsonMappingException {
 Class<Long> longType = Long.TYPE;longType.getDeclaredField("serialVersionUID");
 longType.setAccessible(true);
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(longType, true, null);
 ObjectWriter writer = new ObjectMapper().writerWithRootValue(SerializerProviders.baseInstance());
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(writer, writer.getConfig());
 serializer.acceptJsonFormatVisitor(visitor, longType);
 assertTrue(writer.getConfig().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
 }

 /**
 * Tests {@link #acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} method with valid arguments and serialization as a string.
 */
 @Test
 public void testAcceptJsonFormatVisitorString() throws JsonMappingException {
 Class<Long> longType = Long.TYPE;longType.getDeclaredField("serialVersionUID");
 longType.setAccessible(true);
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(longType, false, null);
 ObjectWriter writer = new ObjectMapper().writerWithRootValue(SerializerProviders.baseInstance());
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(writer, writer.getConfig());
 serializer.acceptJsonFormatVisitor(visitor, longType);
 assertFalse(writer.getConfig().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
 }
}

class TestJsonGenerator extends JsonGenerator {
 @Override
 public void writeStartArray() throws IOException {
 super.writeStartArray();
 }

 @Override
 public void writeNumber(long number) throws IOException {
 super.writeNumber(number);
 }
}