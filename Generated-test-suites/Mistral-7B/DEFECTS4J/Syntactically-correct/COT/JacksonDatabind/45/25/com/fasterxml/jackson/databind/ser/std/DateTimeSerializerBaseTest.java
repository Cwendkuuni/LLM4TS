package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonValueFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> dateTimeSerializer;
 private SerializerProvider serializers;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 serializers = mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 // Test with null property
 JsonFormat format1 = null;
 BeanProperty property1 = mock(BeanProperty.class);
 when(property1.getMember()).thenReturn(null);
 when(serializers.getAnnotationIntrospector().findFormat(ArgumentMatchers.any(Annotated.class)))
 .thenReturn(format1);
 assertEquals(dateTimeSerializer, dateTimeSerializer.createContextual(serializers, property1));

 // Test with JsonFormat annotation
 JsonFormat format2 = mock(JsonFormat.class);
 when(format2.getShape()).thenReturn(JsonFormat.Shape.STRING);
 when(format2.getTimeZone()).thenReturn(TimeZone.getDefault());
 when(format2.getLocale()).thenReturn(Locale.getDefault());
 when(format2.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 BeanProperty property2 = mock(BeanProperty.class);
 when(property2.getMember()).thenReturn(mock(Long.class));
 when(serializers.getAnnotationIntrospector().findFormat(ArgumentMatchers.any(Annotated.class)))
 .thenReturn(format2);
 DateFormat expectedFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 expectedFormat2.setTimeZone(TimeZone.getDefault());
 expectedFormat2.setLocale(Locale.getDefault());
 DateTimeSerializerBase<Long> expectedSerializer2 = new DateTimeSerializerBase<Long>(Long.TYPE, false, expectedFormat2);
 assertEquals(expectedSerializer2, dateTimeSerializer.createContextual(serializers, property2));
 }

 @Test
 public void testIsEmpty() throws JsonProcessingException {
 Long value1 = null;
 Long value2 = new Long(0);
 assertNull(dateTimeSerializer.serialize(value1, mock(JsonGenerator.class), serializers));
 assertNull(dateTimeSerializer.serialize(value2, mock(JsonGenerator.class), serializers));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode expectedSchema = createSchemaNode("string", true);
 assertEquals(expectedSchema, dateTimeSerializer.getSchema(serializers, Long.TYPE));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);
 visitor.setProvider(serializers);
 when(visitor.getProvider()).thenReturn(serializers);
 dateTimeSerializer.acceptJsonFormatVisitor(visitor, typeHint);
 }

 @Test
 public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
 Calendar calendar = Calendar.getInstance();
 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
 calendar.setTime(java.util.Date.from(java.time.Instant.now()));
 Date date = calendar.getTime();
 Long value = new Long(date.getTime());
 String expected = format.format(date);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeString(ArgumentMatchers.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
 when(gen.writeNumber(ArgumentMatchers.longValue(ArgumentMatchers.eq(0L)))).thenReturn(null);
 assertEquals(expected, dateTimeSerializer.serialize(value, gen, serializers).getValueAsString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
 Calendar calendar = Calendar.getInstance();
 SimpleDateFormat invalidFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
 calendar.setTime(java.util.Date.from(java.time.Instant.now()));
 Date date = calendar.getTime();
 Long value = new Long(date.getTime());
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeString(ArgumentMatchers.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
 when(gen.writeNumber(ArgumentMatchers.longValue(ArgumentMatchers.eq(0L)))).thenReturn(null);
 dateTimeSerializer.withFormat(Boolean.FALSE, invalidFormat);
 dateTimeSerializer.serialize(value, gen, serializers);
 }
}