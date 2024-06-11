package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class DateTimeSerializerBaseTest {

 private static class TestDate {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
 private Date date;
 }

 private ObjectMapper objectMapper;
 private SerializerProvider serializerProvider;
 private DeserializerProvider deserializerProvider;
 private JsonGenerator jsonGenerator;
 private JsonParser jsonParser;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 serializerProvider = objectMapper.getSerializerProvider();
 deserializerProvider = objectMapper.getDeserializerProvider();
 jsonGenerator = mock(JsonGenerator.class);
 jsonParser = mock(JsonParser.class);
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException {
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
 dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat);
 BeanProperty property = mock(BeanProperty.class);
 Annotated annotated = mock(Annotated.class);
 JsonFormat.Value jsonFormat = mock(JsonFormat.Value.class);
 when(annotated.getAnnotation(JsonFormat.class)).thenReturn(jsonFormat);
 when(property.getMember()).thenReturn(annotated);
 when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
 when(jsonFormat.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));
 when(jsonFormat.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 when(jsonFormat.hasLocale()).thenReturn(false);
 when(jsonFormat.hasPattern()).thenReturn(true);

 JsonSerializer<Date> serializer = dateTimeSerializer.createContextual(serializerProvider, property);
 assertEquals(DateTimeSerializerBase.class, serializer.getClass());
 assertEquals(Boolean.FALSE, serializer.getFormat().booleanValue());
 assertEquals(dateFormat, serializer.getFormat());
 }

 @Test
 public void testCreateContextualWithNumericFormat() throws JsonProcessingException {
 DateFormat dateFormat = StdDateFormat.INSTANCE;
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 BeanProperty property = mock(BeanProperty.class);
 Annotated annotated = mock(Annotated.class);
 JsonFormat.Value jsonFormat = mock(JsonFormat.Value.class);
 when(annotated.getAnnotation(JsonFormat.class)).thenReturn(jsonFormat);
 when(property.getMember()).thenReturn(annotated);
 when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
 when(jsonFormat.getTimeZone()).thenReturn(TimeZone.getDefault());
 when(jsonFormat.hasPattern()).thenReturn(false);
 when(jsonFormat.hasLocale()).thenReturn(false);

 JsonSerializer<Date> serializer = dateTimeSerializer.createContextual(serializerProvider, property);
 assertEquals(DateTimeSerializerBase.class, serializer.getClass());
 assertEquals(Boolean.TRUE, serializer.getFormat().booleanValue());
 assertEquals(null, serializer.getFormat());
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 TestDate testDate = new TestDate();
 testDate.date = new Date(0L);
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

 when(jsonGenerator.setCodec(any(JsonSerializer.class))).thenAnswer(i -> i.getArguments()[0]);
 when(jsonGenerator.writeObject(any(Object.class))).thenAnswer(i -> i.getArguments()[0]);

 dateTimeSerializer.serialize(testDate.date, jsonGenerator, serializerProvider);
 assertEquals("null", jsonGenerator.getOutputContent().toString());

 testDate.date = new Date(System.currentTimeMillis());
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

 when(jsonGenerator.setCodec(any(JsonSerializer.class))).thenAnswer(i -> i.getArguments()[0]);
 when(jsonGenerator.writeStringValue("2023-03-22T15:16:17.123Z")).thenReturn(null);

 dateTimeSerializer.serialize(testDate.date, jsonGenerator, serializerProvider);
 verify(jsonGenerator).setCodec(eq(dateTimeSerializer));
 verify(jsonGenerator).writeStringValue("2023-03-22T15:16:17.123Z");
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getDefaultVisibilityChecker()
 .findSerializerVisitors(JsonFormatTypes.DATE, new TypeReference<JsonNode>() {});

 dateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
 assertEquals("date", visitor.getWriter().getSerializerProvider().getRootValueSerializer().getType().getSimpleName());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 JsonNode expectedSchema = objectMapper.createObjectNode().put("type", "string");
 assertEquals(expectedSchema, dateTimeSerializer.getSchema(serializerProvider, Date.class));
 }

 @Test
 public void testIsEmpty() throws JsonProcessingException {
 TestDate testDate = new TestDate();
 DateTimeSerializerBase<TestDate> dateTimeSerializer = new DateTimeSerializerBase<TestDate>(TestDate.class, false, null);
 assertEquals(true, dateTimeSerializer.isEmpty(testDate));

 testDate.date = new Date(System.currentTimeMillis());
 assertEquals(false, dateTimeSerializer.isEmpty(testDate));
 }
}