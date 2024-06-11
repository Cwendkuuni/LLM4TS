package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longDateTimeSerializer;
 private DateTimeSerializerBase<String> stringDateTimeSerializer;

 @Before
 public void setup() throws Exception {
 JsonParser jsonParser = mock(JsonParser.class);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializers = newSerializerProvider();
 ObjectMapper objectMapper = new ObjectMapper();
 longDateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null) {
 @Override
 protected long _timestamp(Long value) {
 return value.longValue();
 }

 @Override
 public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 gen.writeNumber(value);
 }
 };

 stringDateTimeSerializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {
 private final StdDateFormat df = new StdDateFormat();

 @Override
 protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
 throws JsonMappingException {
 visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
 }

 @Override
 protected void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 df.format(value, gen);
 }
 };
 }

 @Test
 public void testSerializationAsTimestamp() throws IOException, JsonProcessingException {
 SerializerProvider serializers = newSerializerProvider();
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 longDateTimeSerializer.serialize(1L, jsonGenerator, serializers);
 verify(jsonGenerator).writeNumber(1L);
 }

 @Test
 public void testSerializationAsString() throws IOException, JsonProcessingException {
 SerializerProvider serializers = newSerializerProvider();
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 when(jsonGenerator.getFormatName()).thenReturn(dateFormat.toPattern());
 stringDateTimeSerializer.serialize("2022-03-15 14:30:45.123 Z", jsonGenerator, serializers);
 verify(jsonGenerator).writeString("2022-03-15 14:30:45.123 Z");
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException, JsonProcessingException {
 SerializerProvider serializers = newSerializerProvider();
 serializers.findFormatables().clear();
 SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 DateTimeSerializerBase<String> customSerializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, customDateFormat) {
 @Override
 protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
 throws JsonMappingException {
 visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
 }
 };
 String date = "2022-03-15 14:30:45";
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 customSerializer.serialize(date, jsonGenerator, serializers);
 verify(jsonGenerator).writeString(date);
 }

 @Test
 public void testDeserializationAsTimestamp() throws IOException, JsonProcessingException {
 SerializerProvider serializers = newSerializerProvider();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsLong()).thenReturn(1L);
 ObjectMapper objectMapper = new ObjectMapper();
 Long value = longDateTimeSerializer.deserialize(jsonParser, Long.class, serializers);
 assertEquals(1L, value.longValue());
 }

 @Test
 public void testDeserializationAsString() throws IOException, JsonProcessingException {
 SerializerProvider serializers = newSerializerProvider();
 SerializerProvider serializers2 = newSerializerProvider();
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("2022-03-15 14:30:45.123 Z");
 when(jsonParser.getCodec()).thenReturn(objectMapper.getCodec());
 when(serializers.findValueSerializer(Object.class)).thenReturn(stringDateTimeSerializer);
 ObjectMapper objectMapper2 = new ObjectMapper();
 objectMapper2.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String value = longDateTimeSerializer.deserialize(jsonParser, String.class, serializers2);
 assertEquals("2022-03-15 14:30:45.123 Z", value);
 }

 private SerializerProvider newSerializerProvider() {
 SerializerProvider serializers = new StandardSerializerProvider();
 serializers.setNullValueSerializer(new StandardSerializer(null.getClass(), null));
 return serializers;
 }

}