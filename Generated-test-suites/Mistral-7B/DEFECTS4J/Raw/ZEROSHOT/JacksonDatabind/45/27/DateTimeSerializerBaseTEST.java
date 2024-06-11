package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest extends BaseMapTest {

 private static final long TEST_TIMESTAMP = 1_000_000_000L;
 private static final String TEST_DATE_ISO8601 = "2022-02-01";
 private static final String TEST_DATE_FORMAT = "dd-MM-yyyy";

 @SuppressWarnings("rawtypes")
 private static final DateTimeSerializerBase DATE_TIME_SERIALIZER = new DatetimeSerializerBaseForTest();

 @SuppressWarnings("rawtypes")
 private static final DateTimeSerializerBase TIMESTAMP_SERIALIZER = new DatetimeSerializerBaseForTest(Boolean.TRUE, null);

 @SuppressWarnings("rawtypes")
 private static final DateTimeSerializerBase CUSTOM_DATE_FORMAT_SERIALIZER = new DatetimeSerializerBaseForTest(Boolean.FALSE, new SimpleDateFormat(TEST_DATE_FORMAT));

 @SuppressWarnings("rawtypes")
 private static final DateTimeSerializerBase DATE_FORMAT_SERIALIZER_WITH_LOCALE_AND_TIMEZONE = new DatetimeSerializerBaseForTest(Boolean.FALSE, new SimpleDateFormat(TEST_DATE_FORMAT), new Locale("fr", "FR"), TimeZone.getTimeZone("Europe/Paris"));

 @SuppressWarnings("rawtypes")
 private static final Object TEST_DATE_OBJECT = new Object() {
     private long timestamp;

     public long getTimestamp() {
         return timestamp;
     }

     public void setTimestamp(long timestamp) {
         this.timestamp = timestamp;
     }
 };

 @SuppressWarnings("rawtypes")
 private static final Object TEST_DATE_TIME_OBJECT = new Object() {
     private java.util.Date date;

     public java.util.Date getDate() {
         return date;
     }

     public void setDate(java.util.Date date) {
         this.date = date;
     }
 };

 @SuppressWarnings("rawtypes")
 @org.junit.Before
 public void setup() {
     when(objectMapper.getAnnotationIntrospector()).thenReturn(new AnnotationIntrospector());
 }

 @org.junit.Test
 public void testWithFormat() throws JsonProcessingException, IOException {
     DateTimeSerializerBase serializer = DATE_TIME_SERIALIZER;

     DateTimeSerializerBase timestampSerializer = serializer.withFormat(Boolean.TRUE, null);
     assertEquals(timestampSerializer.getClass(), TIMESTAMP_SERIALIZER.getClass());

     DateTimeSerializerBase customFormatSerializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat(TEST_DATE_FORMAT));
     assertEquals(customFormatSerializer.getClass(), CUSTOM_DATE_FORMAT_SERIALIZER.getClass());
 }

 @org.junit.Test
 public void testIsEmpty() throws JsonProcessingException {
     assertEquals(true, DATE_TIME_SERIALIZER.isEmpty(TEST_DATE_OBJECT));
     assertEquals(true, DATE_TIME_SERIALIZER.isEmpty(TEST_DATE_TIME_OBJECT));
     assertEquals(true, DATE_TIME_SERIALIZER.isEmpty(new Object()));
     assertEquals(true, DATE_TIME_SERIALIZER.isEmpty(null));
     assertEquals(false, DATE_TIME_SERIALIZER.isEmpty(TEST_DATE_OBJECT, mapper));
     assertEquals(false, DATE_TIME_SERIALIZER.isEmpty(TEST_DATE_TIME_OBJECT, mapper));
 }

 @org.junit.Test
 public void testGetSchema() throws JsonProcessingException {
     assertEquals("number", DATE_TIME_SERIALIZER.getSchema(mapper, Object.class).getClassifier().get());
     assertEquals("string", CUSTOM_DATE_FORMAT_SERIALIZER.getSchema(mapper, Object.class).getClassifier().get());
 }

 @org.junit.Test
 public void testCreateContextual() throws JsonMappingException {
     JsonFormat annotation = mock(JsonFormat.class);
     Annotated property = mock(Annotated.class);
     BeanProperty beanProperty = mock(BeanProperty.class);
     when(property.getMember()).thenReturn(beanProperty);
     when(beanProperty.getName()).thenReturn("testProperty");
     when(annotation.getShape()).thenReturn(JsonFormat.Shape.STRING);
     when(annotation.getLocale()).thenReturn(Locale.ENGLISH);
     when(annotation.getTimeZone()).thenReturn(TimeZone.getDefault());
     when(annotation.getPattern()).thenReturn(TEST_DATE_FORMAT);

     ObjectMapper mapper = new ObjectMapper();
     ObjectCodec objectCodec = mapper.getSerializationConfig().getDefaultOutputCodec();
     JsonSerializerProvider serializers = mapper.getSerializerProvider();
     serializers.setNullValueSerializer(mapper.getDefaultSerializerProvider().getNullValueSerializer());
     serializers.setNullValueSerializer(new JsonSerializer<Object>() {
         @Override
         public void serializeValues(Map<String, Object> pMap, JsonGenerator pGenerator, SerializerProvider pProvider)
             throws IOException, JsonProcessingException {
             pGenerator.writeNull();
         }
     });
     when(mapper.getSerializerProvider()).thenReturn(serializers);

     DateTimeSerializerBase serializer = DATE_TIME_SERIALIZER;
     JsonSerializer<Object> contextualSerializer = serializer.createContextual(objectCodec, property);

     assertEquals(CUSTOM_DATE_FORMAT_SERIALIZER.getClass(), contextualSerializer.getClass());

     when(annotation.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

     contextualSerializer = serializer.createContextual(objectCodec, property);

     assertEquals(TIMESTAMP_SERIALIZER.getClass(), contextualSerializer.getClass());
 }

 @org.junit.Test
 public void testSerialize() throws JsonProcessingException {
     JsonGenerator gen = mock(JsonGenerator.class);
     when(gen.writeNumber(TEST_TIMESTAMP)).thenAnswer(invocation -> TEST_TIMESTAMP);
     when(gen.writeString(TEST_DATE_ISO8601)).thenAnswer(invocation -> TEST_DATE_ISO8601);
     when(gen.writeNull()).thenAnswer(invocation -> {});

     ObjectMapper mapper = new ObjectMapper();
     when(mapper.getSerializerProvider().findValueSerializer(Long.TYPE, null)).thenReturn(new LongSerializer());
     when(mapper.getSerializerProvider().findValueSerializer(String.class, null)).thenReturn(StringSerializer.instance);

     DATE_TIME_SERIALIZER.serialize(TEST_DATE_OBJECT, gen, mapper);
     assertEquals(TEST_TIMESTAMP, gen.getCurrentValueAsLong());

     TEST_DATE_TIME_OBJECT.setDate(new Date(TEST_TIMESTAMP));
     DATE_TIME_SERIALIZER.serialize(TEST_DATE_TIME_OBJECT, gen, mapper);
     assertEquals(TEST_DATE_ISO8601, gen.getOutputValueAsString());

     DATE_TIME_SERIALIZER.serialize(null, gen, mapper);
     gen.flush();
     assertEquals("null", gen.getOutputValueAsString());
 }

 @org.junit.Test
 public void testSerializeWithCustomFormat() throws JsonProcessingException {
     JsonGenerator gen = mock(JsonGenerator.class);
     when(gen.writeString(TEST_DATE_ISO8601)).thenAnswer(invocation -> TEST_DATE_ISO8601);

     ObjectMapper mapper = new ObjectMapper();
     when(mapper.getSerializerProvider().findValueSerializer(Object.class, new SimpleType(Object.class)))
             .thenReturn(CUSTOM_DATE_FORMAT_SERIALIZER);

     TEST_DATE_TIME_OBJECT.setDate(new Date(TEST_TIMESTAMP));
     DATE_TIME_SERIALIZER.serialize(TEST_DATE_TIME_OBJECT, gen, mapper);
     assertEquals(TEST_DATE_ISO8601, gen.getOutputValueAsString());
 }

 @org.junit.Test
 public void testSerializeWithLocaleAndTimeZone() throws JsonProcessingException {
     JsonGenerator gen = mock(JsonGenerator.class);
     when(gen.writeString(TEST_DATE_ISO8601)).thenAnswer(invocation -> TEST_DATE_ISO8601);

     ObjectMapper mapper = new ObjectMapper();
     when(mapper.getSerializerProvider().findValueSerializer(Object.class, new SimpleType(Object.class)))
             .thenReturn(DATE_FORMAT_SERIALIZER_WITH_LOCALE_AND_TIMEZONE);

     TEST_DATE_TIME_OBJECT.setDate(new Date(TEST_TIMESTAMP));
     DATE_TIME_SERIALIZER.serialize(TEST_DATE_TIME_OBJECT, gen, mapper);
     assertEquals(TEST_DATE_ISO8601, gen.getOutputValueAsString());
 }

 private static class DatetimeSerializerBaseForTest extends DateTimeSerializerBase<Object> {

     public DatetimeSerializerBaseForTest() {
         this(false, null);
     }

     public DatetimeSerializerBaseForTest(boolean useTimestamp, DateFormat customFormat) {
         super(Object.class, useTimestamp, customFormat);
     }

     @Override
     protected long _timestamp(Object value) {
         return TEST_TIMESTAMP;
     }

     @Override
     protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
             boolean asNumber) throws JsonMappingException {
         visitor.visitDateSerializer(this, typeHint);
     }

     @Override
     public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
             throws IOException {
         // noop
     }
 }

 private static class LongSerializer extends StdSerializer<Long> {

     public LongSerializer() {
         this(null);
     }

     public LongSerializer(Class<Long> t) {
         this(t, null);
     }

     public LongSerializer(Class<Long> t, SerializerProvider p) {
         super(t, p);
     }

     @Override
     public void serializeValues(Map<String, Object> pMap, JsonGenerator pGen, SerializerProvider pProvider)
             throws IOException, JsonProcessingException {
         pGen.writeNumber(pMap.get(getName()));
     }
 }

 private static class StringSerializer extends StdSerializer<String> {

     public static final StringSerializer instance = new StringSerializer();

     public StringSerializer() {
         this(null);
     }

     public StringSerializer(Class<String> t) {
         this(t, null);
     }

     public StringSerializer(Class<String> t, SerializerProvider p) {
         super(t, p);
     }

     @Override
     public void serializeValues(Map<String, Object> pMap, JsonGenerator pGen, SerializerProvider pProvider)
             throws IOException, JsonProcessingException {
         pGen.writeString(pMap.get(getName()).toString());
     }
 }
}