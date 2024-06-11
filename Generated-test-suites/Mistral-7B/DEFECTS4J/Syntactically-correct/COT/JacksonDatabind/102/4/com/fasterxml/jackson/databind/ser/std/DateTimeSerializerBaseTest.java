package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateTimeSerializerBaseBuilder;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializerBase;
 private SerializerProvider serializerProvider;
 private JsonParser jsonParser;
 private JsonGenerator jsonGenerator;

 @Before
 public void setup() {
 serializerProvider = mock(SerializerProvider.class);
 jsonParser = mock(JsonParser.class);
 jsonGenerator = mock(JsonGenerator.class);
 dateTimeSerializerBase = new DateTimeSerializerBaseBuilder<Date>(Date.class, false, null)
 .build();
 }

 @Test
 public void testCreateContextual() throws JsonMappingException, IOException {
 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz");
 DateFormat expectedDateFormat = dateFormat;
 when(serializerProvider.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
 when(serializerProvider.findValueSerializer(Date.class)).thenReturn(null);
 when(serializerProvider.findValueSerializer(String.class)).thenReturn(null);
 when(serializerProvider.findBeanProperty("testProperty")).thenReturn(null);
 when(serializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 when(serializerProvider.getLocale()).thenReturn(Locale.ENGLISH);
 when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getDefault());

 DateTimeSerializerBase<Date> actualDateSerializer = dateTimeSerializerBase.createContextual(serializerProvider, null);
 assertEquals(expectedDateFormat, actualDateSerializer._customFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateContextualWithNullProvider() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> actualDateSerializer = dateTimeSerializerBase.createContextual(null, null);
 }

 @Test
 public void testIsEmpty() {
 Date date = new Date(0L);
 assertEquals(false, dateTimeSerializerBase.isEmpty(serializerProvider, date));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 assertEquals("number", dateTimeSerializerBase.getSchema(serializerProvider, Date.class.getType()));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.getProvider()).thenReturn(serializerProvider);
 dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, Date.class.getType());
 }

 @Test
 public void testSerialize() throws IOException, JsonMappingException, ParseException {
 Date date = new Date();
 String expectedString = "25-Jan-2023 14:30:23 EST";
 when(jsonGenerator.writeString(AnyType.class, anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
 when(jsonGenerator.writeNumber(AnyType.class, anyLong())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0).longValue());
 when(jsonGenerator.writeNull()).thenReturn(null);
 when(jsonParser.getValueAsString()).thenReturn(expectedString);
 when(jsonParser.getValueAsNumber()).thenReturn(0L);

 dateTimeSerializerBase.serialize(date, jsonGenerator, serializerProvider);
 assertEquals(expectedString, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeAsString() throws IOException {
 Date date = new Date();
 when(jsonGenerator.writeString(anyString())).thenAnswer(invocationOnMock -> "");

 dateTimeSerializerBase._serializeAsString(date, jsonGenerator, serializerProvider);
 assertEquals("", jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeWithCustomFormat() throws IOException, JsonMappingException, ParseException {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 dateTimeSerializerBase = new DateTimeSerializerBaseBuilder<Date>(Date.class, false, customFormat)
 .build();
 Date date = new Date();
 when(jsonGenerator.writeString(anyString())).thenAnswer(invocationOnMock -> customFormat.format(date));

 dateTimeSerializerBase.serialize(date, jsonGenerator, serializerProvider);
 assertEquals(customFormat.format(date), jsonGenerator.getOutputString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithNullProvider() throws IOException, JsonMappingException {
 dateTimeSerializerBase.serialize(new Date(), jsonGenerator, null);
 }

 @Test
 public void testSerializeWithNullValue() {
 dateTimeSerializerBase.serialize(null, jsonGenerator, serializerProvider);
 assertEquals("null", jsonGenerator.getOutputString());
 }
}