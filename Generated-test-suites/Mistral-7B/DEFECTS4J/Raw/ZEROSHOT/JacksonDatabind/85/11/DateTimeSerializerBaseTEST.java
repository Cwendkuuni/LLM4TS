package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import junit.framework.JUnit4TestAdapter;

@RunWith(JUnit4TestAdapter.class)
public class DateTimeSerializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testDateTimeSerializerBaseConstruction() {
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 };
 SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
 DateTimeSerializerBase<LocalDateTime> testedSerializer = new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, Boolean.FALSE, null);
 assertEquals(testedSerializer.getClass(), serializer.getClass());
 assertEquals(serializers, testedSerializer.getProvider());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDateTimeSerializerBaseWithFormat() {
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 };
 DateTimeSerializerBase<LocalDateTime> testedSerializer = new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, Boolean.FALSE, null)
 .withFormat(Boolean.FALSE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 assertEquals(testedSerializer.getClass(), serializer.getClass());
 assertNotSame(testedSerializer, serializer);
 assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), testedSerializer._customFormat);
 }

 @Test
 public void testSerialize() throws IOException, JsonProcessingException {
 LocalDateTime dateTime = LocalDateTime.of(2023, 3, 15, 16, 30);
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 };
 serializer.serialize(dateTime, gen, MAPPER.getSerializerProvider());
 gen.flush();
 String result = new String(gen.getOutputBuffer().toString(), "UTF-8");
 assertEquals("\"2023-03-15T16:30:00.000Z\"", result);
 }

 @Test
 public void testSerializeWithCustomFormat() throws IOException, JsonProcessingException {
 LocalDateTime dateTime = LocalDateTime.of(2023, 3, 15, 16, 30);
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 }.withFormat(Boolean.FALSE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 serializer.serialize(dateTime, gen, MAPPER.getSerializerProvider());
 gen.flush();
 String result = new String(gen.getOutputBuffer().toString(), "UTF-8");
 assertEquals("\"2023-03-15 16:30:00\"", result);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 };
 serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance.constructType(LocalDateTime.class));
 Mockito.verify(visitor).visitStringFormat(eq(TypeFactory.defaultInstance.constructType(LocalDateTime.class)), any(), eq(JsonValueFormat.DATE_TIME));
 }

 @Test
 public void testAcceptJsonFormatVisitorWithFormat() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 }.withFormat(Boolean.FALSE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance.constructType(LocalDateTime.class));
 Mockito.verify(visitor).visitStringFormat(eq(TypeFactory.defaultInstance.constructType(LocalDateTime.class)), eq("yyyy-MM-dd HH:mm:ss"), any());
 }

 @Test
 public void testIsEmpty() {
 LocalDateTime nullDateTime = null;
 LocalDateTime emptyDateTime = LocalDateTime.MIN;
 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 };
 assertTrue(serializer.isEmpty(nullDateTime));
 assertTrue(serializer.isEmpty(emptyDateTime));
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 }.getSchema(MAPPER.getSerializerProvider(), LocalDateTime.class.getType());
 assertEquals("string", schema.getClass().getName());
 }
}